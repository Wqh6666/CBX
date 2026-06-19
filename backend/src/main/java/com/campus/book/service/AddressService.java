package com.campus.book.service;

import com.campus.book.dto.AddressDTO;
import com.campus.book.entity.Address;
import java.util.List;

public interface AddressService {
    List<Address> getAddresses(Long userId);
    Address getAddressById(Long id);
    Long createAddress(Long userId, AddressDTO dto);
    void updateAddress(Long id, AddressDTO dto);
    void deleteAddress(Long id);
}
