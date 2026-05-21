package com.example;

import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DatabaseHealthService {
    private static final String DB_INFO_QUERY = "SELECT DATABASE() AS db_name, VERSION() AS mysql_version";
    //Get
    private static final String CART_ITEMS_QUERY = "SELECT id, user_id, product_id, quantity FROM cart_items ORDER BY id LIMIT ?";
    private static final String CART_ITEMS_BY_USER_QUERY = "SELECT id, user_id, product_id, quantity FROM cart_items WHERE user_id = ? ORDER BY id LIMIT ?";
    private static final String CART_ITEM_BY_ID_QUERY = "SELECT id, user_id, product_id, quantity FROM cart_items WHERE id = ?";
    //Post
    private static final String CREATE_CART_ITEM_QUERY =
            "INSERT INTO cart_items (id, user_id, product_id, quantity) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
    //Put
    private static final String UPDATE_CART_ITEM_QUERY = "UPDATE cart_items SET quantity = ? WHERE id = ?";
    //Delete
    private static final String DELETE_CART_ITEM_QUERY = "DELETE FROM cart_items WHERE id = ?";

    private final DataSource dataSource;

    public DatabaseHealthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String databaseInfo() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DB_INFO_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String dbName = resultSet.getString("db_name");
                String version = resultSet.getString("mysql_version");
                return "MySQL connected. db=" + dbName + ", version=" + version;
            }
            return "MySQL connected, but database info is unavailable";
        }
    }

    public List<Map<String, Object>> cartItems(int limit) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CART_ITEMS_QUERY)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                return readCartItems(resultSet);
            }
        }
    }

    public List<Map<String, Object>> cartItemsByUser(String userId, int limit) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CART_ITEMS_BY_USER_QUERY)) {
            statement.setString(1, userId);
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                return readCartItems(resultSet);
            }
        }
    }

    public Map<String, Object> createCartItem(String id, String userId, String productId, int quantity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_CART_ITEM_QUERY)) {
            statement.setString(1, id);
            statement.setString(2, userId);
            statement.setString(3, productId);
            statement.setInt(4, quantity);
            statement.executeUpdate();
        }

        // user_id + product_id is unique, so this returns the single newly inserted row.
        List<Map<String, Object>> items = cartItemsByUser(userId, 1000);
        for (Map<String, Object> item : items) {
            if (productId.equals(item.get("productId"))) {
                return item;
            }
        }
        throw new SQLException("Created cart item could not be fetched");
    }

    public Map<String, Object> updateCartItemQuantity(String id, int quantity) throws SQLException {
        int updatedRows;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CART_ITEM_QUERY)) {
            statement.setInt(1, quantity);
            statement.setString(2, id);
            updatedRows = statement.executeUpdate();
        }

        if (updatedRows == 0) {
            return null;
        }
        return getCartItemById(id);
    }

    public boolean deleteCartItem(String id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CART_ITEM_QUERY)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public Map<String, Object> getCartItemById(String id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CART_ITEM_BY_ID_QUERY)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Map<String, Object>> items = readCartItems(resultSet);
                if (items.isEmpty()) {
                    return null;
                }
                return items.get(0);
            }
        }
    }

    private List<Map<String, Object>> readCartItems(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> items = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", resultSet.getString("id"));
            item.put("userId", resultSet.getString("user_id"));
            item.put("productId", resultSet.getString("product_id"));
            item.put("quantity", resultSet.getInt("quantity"));
            items.add(item);
        }
        return items;
    }

    public void clearCartByUser(String userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }
}
