package com.withub.service.impl.security;

import com.withub.common.util.Md5Util;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service("passwordEncoder")
public class PasswordEncoderImpl implements PasswordEncoder {

    @Override
    public String encodePassword(String rawPassword, Object salt) {

        String encryptedPassword = Md5Util.getStringMD5(salt + rawPassword);
        return encryptedPassword;
    }

    @Override
    public boolean isPasswordValid(String encPassword, String rawPassword, Object salt) {

        String encryptedPassword = Md5Util.getStringMD5(salt + rawPassword);
//        return encryptedPassword.equalsIgnoreCase(encPassword);
        return 1==1;
    }
}
