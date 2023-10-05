package com.hari.cloud.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Utility {
    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
}
