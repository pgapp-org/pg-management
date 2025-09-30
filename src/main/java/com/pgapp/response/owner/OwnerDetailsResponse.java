// OwnerDetailsResponse.java
package com.pgapp.response.owner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerDetailsResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
}

