package com.jjclub.auth.web;

import static com.jjclub.auth.web.AuthErrorMessage.EXIST_AUTH;
import static com.jjclub.auth.web.AuthErrorMessage.EXIST_EMAIL;
import static com.jjclub.auth.web.AuthErrorMessage.NO_SUCH_AUTH;

import java.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
@Slf4j
public class AuthExceptionController {
    private static final String SERVER_NUM = "1";

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleRuntimeException(RuntimeException e) {
        log.warn("RuntimeException: {}", e);
        return ExceptionResponse.of(calculateCode(HttpStatus.BAD_REQUEST,
                NO_SUCH_AUTH.getCode()),
            e.getClass().getName(), NO_SUCH_AUTH.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException: {}", e);
        return ExceptionResponse.of(calculateCode(HttpStatus.BAD_REQUEST,
                NO_SUCH_AUTH.getCode()),
            e.getClass().getName(), NO_SUCH_AUTH.getMessage());
    }



    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleSignatureException(SignatureException e) {
        log.warn("SignatureException: {}", e);
        return ExceptionResponse.of(calculateCode(HttpStatus.BAD_REQUEST,
                NO_SUCH_AUTH.getCode()),
            e.getClass().getName(), NO_SUCH_AUTH.getMessage());
    }

    // EXIST_AUTH
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBindException(BindException e) {
        log.warn("handleBindException: {}", e);
        return ExceptionResponse.of(calculateCode(HttpStatus.BAD_REQUEST,
                EXIST_AUTH.getCode()),
            e.getClass().getName(), EXIST_AUTH.getMessage());
    }

    // NO_SUCH_AUTH
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.warn("handleInternalAuthenticationServiceException: {}", e);
        return ExceptionResponse.of(calculateCode(HttpStatus.BAD_REQUEST,
                NO_SUCH_AUTH.getCode()),
            e.getClass().getName(), NO_SUCH_AUTH.getMessage());
    }

    private String calculateCode(HttpStatus status, String code) {
        return status.value() + SERVER_NUM + code;
    }
}
