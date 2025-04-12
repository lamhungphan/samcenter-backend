package com.samcenter.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash1 = encoder.encode("123");
        String hash2 = encoder.encode("1234");

        System.out.println("Hash của 123: " + hash1);
        System.out.println("Hash của 1234: " + hash2);
    }
}
