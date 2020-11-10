package com.estore.authenticationauthorizationservice.util.exception;

public class ResourceKeyValueAlreadyExistsException extends RuntimeException {

    public ResourceKeyValueAlreadyExistsException(String key, String value) {
        super("{\"errors\":[{" +
                "\"field\": \"" + key + "\"," +
                "\"defaultMessage\":{\"errorCode\":\"409\",\"Message\":\"Resource key[" + key + "] value [" + value + "] already exists\"}" +
                "}]}");
    }
}
