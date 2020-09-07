package com.example.managementsystem.util.exception;

public class ResourceKeyValueAlreadyExistsException extends RuntimeException {

    public ResourceKeyValueAlreadyExistsException(String key, String value) {
        super("{" +
                "\"field\": {" +
                "\"key\": \"" + key + "\"," +
                "\"value\": \"" + value + "\"" +
                "}" +
                "\"message\": \"Resource key[" + key + "] value [" + value + "] already exists\"" +
                "}");
    }
}
