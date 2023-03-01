package com.ascarrent.domain.enums;

public enum RoleType {

    ROLE_CUSTOMER("Customer"),
    ROLE_ADMIN("Administrator");

    private String name;

    // Set name filed of ROLE_CUSTOMER as Customer
    // Set name filed of ROLE_ADMIN as Administrator
    private RoleType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }


}
