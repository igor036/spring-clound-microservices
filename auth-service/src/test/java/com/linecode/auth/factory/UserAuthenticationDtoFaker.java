package com.linecode.auth.factory;

import com.linecode.auth.dto.UserAuthenticationDto;

public class UserAuthenticationDtoFaker implements FakeFactory<UserAuthenticationDto> {

    @Override
    public UserAuthenticationDto buildFakeInstance() {
        return UserAuthenticationDto
            .builder()
            .username(FAKER.name().username())
            .password(FAKER.code().ean8())
            .build();
    }  
}
