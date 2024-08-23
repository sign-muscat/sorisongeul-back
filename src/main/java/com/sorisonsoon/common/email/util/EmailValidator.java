package com.sorisonsoon.common.email.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
    );

    public static boolean isValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
