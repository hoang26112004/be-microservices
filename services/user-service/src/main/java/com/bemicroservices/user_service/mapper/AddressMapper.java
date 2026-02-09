package com.bemicroservices.user_service.mapper;


import com.bemicroservices.user_service.dto.request.AddressRequest;
import com.bemicroservices.user_service.dto.response.AddressResponse;
import com.bemicroservices.user_service.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;


@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    Address toAddress(AddressRequest request);
    AddressResponse toAddressResponse(Address address);
    void updateAddress(AddressRequest request, @MappingTarget Address address);

    List<AddressResponse> toDtoList(List<Address> addresses);
}