package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.dto.AddressDTO;
import com.campus.book.entity.Address;
import com.campus.book.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public Result<List<Address>> getAddresses() {
        Long userId = (Long) StpUtil.getLoginId();
        List<Address> addresses = addressService.getAddresses(userId);
        return Result.success(addresses);
    }

    @PostMapping
    public Result<Long> createAddress(@Valid @RequestBody AddressDTO dto) {
        Long userId = (Long) StpUtil.getLoginId();
        Long id = addressService.createAddress(userId, dto);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    public Result<Void> updateAddress(@PathVariable Long id, @RequestBody AddressDTO dto) {
        addressService.updateAddress(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return Result.success();
    }
}
