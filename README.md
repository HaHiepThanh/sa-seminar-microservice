# 🛍️ E-Commerce Microservices

Dự án thương mại điện tử xây dựng theo kiến trúc Microservices, sử dụng Micronaut Framework, MySQL, RabbitMQ và gRPC.

---

## 📐 Kiến trúc hệ thống

```
[Browser] → [Nginx Gateway :80]
                  │
    ┌─────────────┼──────────────────────────┐
    │             │                          │
[catalog-service:8081]  [user-service:8083]  [cart-service:8084]
[inventory-service:8082]                    [order-service:8085]
    │                                              │
[gRPC :50051]                              [RabbitMQ :5672]
                                           │             │
                                    [inventory-service] [mail-service:8086]
```

---

## 🚀 Cách 1: Chạy toàn bộ project bằng Docker (Khuyến nghị)

### Yêu cầu
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) đã được cài đặt và đang chạy

### Các bước

**Bước 1:** Clone hoặc tải về toàn bộ source code

**Bước 2:** Mở Terminal tại thư mục gốc của project (chứa file `docker-compose.yml`)

**Bước 3:** Chạy lệnh sau:
```bash
docker compose up -d
```

Docker sẽ tự động:
1. Pull tất cả images từ Docker Hub
2. Khởi động MySQL databases cho từng service và chờ chúng sẵn sàng
3. Khởi động RabbitMQ, tự động tạo Exchange và Queue
4. Khởi động tất cả Microservices
5. Khởi động Nginx Gateway

**Bước 4:** Đợi khoảng **60-90 giây** cho tất cả service healthy, sau đó truy cập:

```
http://localhost
```

### Kiểm tra trạng thái containers
```bash
docker ps
```

### Xem logs của một service cụ thể
```bash
docker logs catalog-service -f
docker logs order-service -f
```

### Dừng toàn bộ hệ thống
```bash
docker compose down
```

### Reset dữ liệu (xóa sạch DB và chạy lại từ đầu)
```bash
docker compose down -v
docker compose up -d
```

---

## 🔧 Cách 2: Chạy từng service thủ công (Dành cho Developer)

### Yêu cầu chung

| Công cụ | Phiên bản | Ghi chú |
|---------|-----------|---------|
| **JDK** | 21+ | [Eclipse Temurin 21](https://adoptium.net/) khuyến nghị |
| **Maven** | 3.9+ | Hoặc dùng `./mvnw` wrapper có sẵn trong mỗi service |
| **MySQL** | 8.0 | Chạy local hoặc qua Docker |
| **RabbitMQ** | 3.x | Chỉ cần cho `inventory-service`, `order-service`, `mail-service` |

### Chuẩn bị hạ tầng (MySQL + RabbitMQ)

Chạy các container hạ tầng trước:
```bash
docker compose up -d rabbitmq mysql-catalog mysql-inventory mysql-user mysql-cart mysql-order rabbitmq-setup
```

### Cấu hình port từng service

| Service | Port HTTP | Port gRPC |
|---------|-----------|-----------|
| catalog-service | 8081 | 50052 |
| inventory-service | 8082 | 50051 |
| user-service | 8083 | — |
| cart-service | 8084 | — |
| order-service | 8085 | — |
| mail-service | 8086 | — |

---

### 1. 📦 Catalog Service
```bash
cd ecom/catalogservice/catalogservice
./mvnw mn:run
```
**Kết nối:** MySQL tại `localhost:3306/catalog_service` · gRPC client → `inventory-service:50051`

---

### 2. 📊 Inventory Service
```bash
cd ecom/inventory
./mvnw mn:run
```
**Kết nối:** MySQL tại `localhost:3307/inventory_service` · RabbitMQ tại `localhost:5672` · gRPC server `:50051`

---

### 3. 👤 User Service
```bash
cd ecom/User/user_service
./mvnw mn:run
```
**Kết nối:** MySQL tại `localhost:3308/user_service`

---

### 4. 🛒 Cart Service
```bash
cd ecom/microservice-cart
./mvnw mn:run
```
**Kết nối:** MySQL tại `localhost:3309/cart_service`

---

### 5. 📋 Order & Payment Service
```bash
cd ecom/order-payment-service
./mvnw mn:run
```
**Kết nối:** MySQL tại `localhost:3310/order_service` · RabbitMQ tại `localhost:5672` · gRPC client → `inventory-service:50051`

---

### 6. 📧 Mail Service
```bash
cd ecom/mail-service/mail-service
./mvnw mn:run
```
**Kết nối:** RabbitMQ tại `localhost:5672` · Queue: `order-email-queue`

---

### 7. 🌐 Nginx Gateway (Frontend)

Chạy Nginx hoặc mở trực tiếp file HTML trong thư mục `ecom-fe/`:

**Option A:** Dùng Docker
```bash
docker compose up -d nginx-gateway
```

**Option B:** Dùng extension Live Server trên VS Code, mở file `ecom-fe/index.html`

> ⚠️ **Lưu ý:** Khi chạy thủ công, cần đảm bảo các service khác đã chạy trước và Nginx gateway đang proxy đúng địa chỉ trong `nginx.conf`.

---

## 🗄️ Database Schema

Mỗi service có file SQL khởi tạo riêng:

| Service | File SQL |
|---------|----------|
| Catalog | `ecom/catalogservice/catalogservice/init-catalog.sql` |
| Inventory | `ecom/inventory/init-inventory.sql` |
| User | `ecom/User/user_service/init-user.sql` |
| Cart | `ecom/microservice-cart/init-cart.sql` |
| Order | `ecom/order-payment-service/init-order.sql` |

---

## 🐰 RabbitMQ Management UI

Truy cập [http://localhost:15672](http://localhost:15672)

- **Username:** `guest`
- **Password:** `guest`

Các Queue được tạo tự động:
- `order-email-queue` — Gửi email xác nhận đơn hàng
- `inventory-queue` — Cập nhật tồn kho sau khi đặt hàng

---

## 🔑 Thông tin mặc định

| Thông tin | Giá trị |
|-----------|---------|
| MySQL root password | `Quocnhan123@` |
| RabbitMQ credentials | `guest` / `guest` |
| JWT Secret | Cấu hình tại `application.properties` của từng service |

---

## ☸️ Cách 3: Deploy lên Civo Kubernetes Engine

### Yêu cầu

| Công cụ | Ghi chú |
|---------|---------|
| [kubectl](https://kubernetes.io/docs/tasks/tools/) | CLI quản lý Kubernetes |
| [Civo CLI](https://github.com/civo/cli) | Quản lý cluster Civo |
| Tài khoản [Civo](https://www.civo.com/) | Đã có API Key |

---

### Bước 1 — Tạo Cluster trên Civo

Đăng nhập Civo Dashboard → **Kubernetes** → **Create cluster**

Hoặc dùng Civo CLI:
```bash
# Đăng nhập
civo apikey save my-key <YOUR_API_KEY>

# Tạo cluster (3 nodes)
civo kubernetes create ecom-cluster --nodes 3 --size g4s.kube.medium --wait

# Tải kubeconfig về máy
civo kubernetes config ecom-cluster --save
```

---

### Bước 2 — Xác nhận kết nối cluster

```bash
kubectl get nodes
```

Kết quả mong đợi: 3 nodes ở trạng thái `Ready`.

---

### Bước 3 — Deploy toàn bộ hệ thống

```bash
kubectl apply -f deploy.yml
```

Lệnh này sẽ tự động tạo:
- Namespace `ecom`
- Secrets chứa credentials
- ConfigMaps chứa SQL init data
- PersistentVolumeClaims cho 5 MySQL instances
- Deployments + Services cho RabbitMQ, MySQL (×5), và 6 Microservices
- Job setup RabbitMQ Exchange & Queue tự động
- LoadBalancer Service cho Nginx Gateway

---

### Bước 4 — Đợi tất cả Pods sẵn sàng

```bash
# Theo dõi trạng thái pods (Ctrl+C để thoát)
kubectl get pods -n ecom -w
```

> ⏳ Lần đầu deploy cần khoảng **3-5 phút** để MySQL khởi tạo và các Java service kết nối.

---

### Bước 5 — Lấy External IP để truy cập

```bash
kubectl get service nginx-gateway -n ecom
```

Cột `EXTERNAL-IP` là địa chỉ public do Civo cấp. Truy cập:
```
http://<EXTERNAL-IP>
```

---

### Các lệnh hữu ích

```bash
# Xem logs của một service
kubectl logs -n ecom deployment/catalog-service -f
kubectl logs -n ecom deployment/order-service -f

# Xem trạng thái tất cả resources
kubectl get all -n ecom

# Kiểm tra RabbitMQ Setup Job
kubectl logs -n ecom job/rabbitmq-setup

# Truy cập RabbitMQ Management UI (port-forward tạm thời)
kubectl port-forward -n ecom svc/rabbitmq 15672:15672
# Mở: http://localhost:15672 (guest / guest)

# Xóa toàn bộ và deploy lại sạch
kubectl delete namespace ecom
kubectl apply -f deploy.yml
```

> **Note:** Nginx Gateway sử dụng image đã build sẵn. Nếu cần cập nhật Frontend, build lại image và push lên Docker Hub trước khi deploy.

