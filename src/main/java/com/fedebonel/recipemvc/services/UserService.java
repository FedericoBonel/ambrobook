package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;

public interface UserService {

    UserDto save(UserDto userDto, String serverUrl);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    UserDto findByVerification(String verificationCode);

    void verify(UserDto user);
}
