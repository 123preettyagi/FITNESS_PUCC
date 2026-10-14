package com.example.VAHAN_API.dto;

import java.util.Date;

public class ApiResponse 
{
    
    private boolean success;
    private String message;
    private Object data;
    private Date timestamp;
    
    public ApiResponse() 
    {
        this.timestamp = new Date();
    }
    
    public ApiResponse(boolean success, String message) 
    {
        this.success = success;
        this.message = message;
        this.timestamp = new Date();
    }
    
    public ApiResponse(boolean success, String message, Object data) 
    {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
  
    
    
}