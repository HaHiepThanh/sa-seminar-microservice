package com.example.service;

import com.example.dto.AddressRequest;
import com.example.entities.Address;
import com.example.repository.AddressRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // ===== GET BY USER =====
    public List<Address> getByUserId(String userId) {
        return addressRepository.findByUserId(userId);
    }

    // ===== CREATE ADDRESS =====
    public void addAddress(String userId, AddressRequest req) {

        Address address = new Address();

        address.setId(UUID.randomUUID().toString());
        address.setUserId(userId);
        address.setAddressTitle(req.getAddressTitle());
        address.setAddressLine(req.getAddressLine());
        address.setCity(req.getCity());
        address.setCountry(req.getCountry());
        address.setZipCode(req.getZipCode());
        address.setIsDefault(false);

        // nếu set default = true → reset cái cũ
        if (address.getIsDefault()) {
            addressRepository.findByUserId(userId).forEach(a -> {
                a.setIsDefault(false);
                addressRepository.update(a);
            });
        }

        addressRepository.save(address);
    }

    public void updateAddress(String userId, String addressId, AddressRequest request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        address.setAddressTitle(request.getAddressTitle());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        addressRepository.update(address);
    }

    // ======================
    // DELETE ADDRESS
    // ======================
    public void deleteAddress(String userId, String addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("Not allowed");
        }

        addressRepository.delete(address);
    }

    // ===== SET DEFAULT =====
    @Transactional
    public void setDefault(String userId, String addressId) {

        List<Address> list = addressRepository.findByUserId(userId);

        for (Address a : list) {
            boolean isTarget = addressId.equals(a.getId());

            if (Boolean.TRUE.equals(a.getIsDefault()) != isTarget) {
                a.setIsDefault(isTarget);
                addressRepository.update(a);
            }
        }
    }
}