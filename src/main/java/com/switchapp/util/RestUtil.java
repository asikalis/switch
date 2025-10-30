package com.switchapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@Component
public class RestUtil {

    public static final String TOKEN_ERROR_401 = "You are not logged in, please log in first.";
    public static final String ERROR_403 = "You no longer have permission to access this institution.";

    public static String getMessageByCode(int code) {
        if (code == 401) {
            return TOKEN_ERROR_401;
        }
        if (code == 403) {
            return ERROR_403;
        }
        return "";
    }

    public static ResponseEntity<ResponseJson> response(HttpStatus status, String message, Object obj) {
        return ResponseEntity.status(status).body(new ResponseJson(status.value(), message, obj));
    }

    public static ResponseEntity<ResponseJson> response(ResponseJson responseJson) {
        return ResponseEntity.status(responseJson.getReturnCode()).body(responseJson);
    }

    @SuppressWarnings("rawtypes")
    public static ResponseEntity response(int status) {
        return ResponseEntity.status(status).build();
    }
}
