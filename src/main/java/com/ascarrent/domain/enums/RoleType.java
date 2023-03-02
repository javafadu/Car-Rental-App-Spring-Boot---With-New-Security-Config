package com.ascarrent.domain.enums;

public enum RoleType {

    ROLE_CUSTOMER("Customer"),
    ROLE_ADMIN("Administrator");

    private String name;

    // Set name field of ROLE_CUSTOMER as Customer
    // Set name field of ROLE_ADMIN as Administrator
    private RoleType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }


}
