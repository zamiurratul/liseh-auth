package com.liseh.auth.persistence.dto;

import lombok.Data;

@Data
public class UserDto {
    private String userId;
    private String username;
    private String password;
}
