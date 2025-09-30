// OwnerLoginResponse.java
package com.pgapp.response.owner;

import com.pgapp.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerLoginResponse {
    private boolean success;
    private String message;
    private Owner owner;
}
