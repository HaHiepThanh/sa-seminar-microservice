package com.example.dto;
import com.example.entities.Address;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public class AddressRequest {

    private String  address_title;
    private String  address_line;
    private String city;
    private String country;
    private int zipCode;

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddressTitle() {
        return address_title;
    }

    public void setAddressTitle(String address_title) {
        this.address_title = address_title;
    }

    public String getAddressLine() {
        return address_line;
    }

    public void setAddressLine(String address_line) {
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

}
