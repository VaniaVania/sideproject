package com.project.sideproject.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
