package com.example.dto;
import com.example.entities.Address;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import io.micronaut.core.annotation.Introspected;

@Introspected
@Serdeable
public class UserProfileResponse {
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String address_line;
    private String city;
    private String country;
    private Boolean is_default;
    private String address_title;
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getAddressTitle() {
        return address_title;
    }

    public void setAddress_title(String address_title) {
        this.address_title = address_title;
    }

    public String getAddressLine() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public Boolean getIsDefault() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
