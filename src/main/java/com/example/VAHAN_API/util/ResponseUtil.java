package com.example.VAHAN_API.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil 
{
    
    public static Map<String, Object> createResponse(String key, Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put(key, value);
        return response;
    }
    
    public static Map<String, Object> createResponse(Map<String, Object> data) {
        return new HashMap<>(data);
    }
}