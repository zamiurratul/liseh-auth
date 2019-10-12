package com.liseh.auth.persistence.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientRegistrationDto {
    private String username;
    private String password;
    private String email;
}
