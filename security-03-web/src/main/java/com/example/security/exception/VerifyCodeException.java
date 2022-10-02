package com.example.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author xiaoning
 * @date 2022/10/02
 */
public class VerifyCodeException extends AuthenticationException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public VerifyCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public VerifyCodeException(String msg) {
        super(msg);
    }
}
