package com.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", insertable = false, updatable = false)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "address_line")
    private String addressLine;

    private String city;
    private String country;

    @Column(name = "address_title")
    private String addressTitle;

    @Column(name = "zip_code")
    private Integer zipCode;

    @Column(name = "is_default")
    private Boolean isDefault;

    // ===== Getter & Setter =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAddressTitle() { return addressTitle; }
    public void setAddressTitle(String addressTitle) { this.addressTitle = addressTitle; }

    public Integer getZipCode() { return zipCode; }
    public void setZipCode(Integer zipCode) { this.zipCode = zipCode; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}