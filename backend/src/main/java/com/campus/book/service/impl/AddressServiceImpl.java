package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.common.BusinessException;
import com.campus.book.dto.AddressDTO;
import com.campus.book.entity.Address;
import com.campus.book.mapper.AddressMapper;
import com.campus.book.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    public List<Address> getAddresses(Long userId) {
        return addressMapper.selectList(new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime));
    }

    @Override
    public Address getAddressById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public Long createAddress(Long userId, AddressDTO dto) {
        // 如果是默认地址，先取消其他默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            Address exist = new Address();
            exist.setIsDefault(0);
            addressMapper.update(exist, new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId));
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setConsignee(dto.getConsignee());
        address.setPhone(dto.getPhone());
        address.setProvince(dto.getProvince() != null ? dto.getProvince() : "上海市");
        address.setCity(dto.getCity() != null ? dto.getCity() : "上海市");
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);

        addressMapper.insert(address);
        return address.getId();
    }

    @Override
    public void updateAddress(Long id, AddressDTO dto) {
        Address exist = addressMapper.selectById(id);
        if (exist == null) {
            throw new BusinessException("地址不存在");
        }

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            Address update = new Address();
            update.setIsDefault(0);
            addressMapper.update(update, new LambdaQueryWrapper<Address>().eq(Address::getUserId, exist.getUserId()));
        }

        if (dto.getConsignee() != null) exist.setConsignee(dto.getConsignee());
        if (dto.getPhone() != null) exist.setPhone(dto.getPhone());
        if (dto.getProvince() != null) exist.setProvince(dto.getProvince());
        if (dto.getCity() != null) exist.setCity(dto.getCity());
        if (dto.getDistrict() != null) exist.setDistrict(dto.getDistrict());
        if (dto.getDetail() != null) exist.setDetail(dto.getDetail());
        if (dto.getIsDefault() != null) exist.setIsDefault(dto.getIsDefault());

        addressMapper.updateById(exist);
    }

    @Override
    public void deleteAddress(Long id) {
        addressMapper.deleteById(id);
    }
}
