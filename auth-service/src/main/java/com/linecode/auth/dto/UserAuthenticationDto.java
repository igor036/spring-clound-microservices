package com.linecode.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class UserAuthenticationDto {

    @NotBlank(message = "The username is required!")
    private String username;

    @NotBlank(message = "The password is required!")
    private String password;

}
