// OwnerRegisterResponse.java
package com.pgapp.response.owner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerRegisterResponse {
    private boolean success;
    private String message;
    private Long ownerId;
    private String name;
}

