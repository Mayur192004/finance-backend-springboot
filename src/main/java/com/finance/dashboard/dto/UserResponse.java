package com.finance.dashboard.dto;

import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private boolean active;

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        return response;
    }
}
