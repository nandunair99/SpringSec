package com.narola.springSecurityJPA.exception;

import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public class UserNotFoundException extends AuthenticationException {
    private String viewName;

    private Map<String, String> errorList;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, String> getErrorList() {
        return errorList;
    }

    public void setErrorList(Map<String, String> errorList) {
        this.errorList = errorList;
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}