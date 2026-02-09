package com.bemicroservices.user_service.constant;

import lombok.Getter;




public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_PM"),
    CUSTOMER("ROLE_CUSTOMER");

    @Getter
    private final String name;

    RoleType(String name) {
        this.name = name;
    }
}
