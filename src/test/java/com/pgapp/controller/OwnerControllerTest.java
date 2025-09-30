package com.pgapp.controller;

import com.pgapp.entity.Owner;
import com.pgapp.request.owner.OwnerLoginRequest;
import com.pgapp.request.owner.OwnerRegisterRequest;
import com.pgapp.response.owner.OwnerLoginResponse;
import com.pgapp.response.owner.OwnerRegisterResponse;
import com.pgapp.service.OwnerService;
import com.pgapp.converter.owner.OwnerConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController ownerController;

    private Owner owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner = Owner.builder()
                .id(1L)
                .name("Prajwal")
                .email("prajwal@example.com")
                .phone("9876543210")
                .passwordHash("hashedpassword")
                .build();
    }

    @Test
    void testRegister() {
        OwnerRegisterRequest request = new OwnerRegisterRequest();
        request.setName("Prajwal");
        request.setEmail("prajwal@example.com");
        request.setPhone("9876543210");
        request.setPassword("password123");

        Owner entity = OwnerConverter.toEntity(request);

        when(ownerService.registerOwner(any(Owner.class))).thenReturn(owner);

        ResponseEntity<OwnerRegisterResponse> response = ownerController.register(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Prajwal", response.getBody().getName());

        verify(ownerService, times(1)).registerOwner(any(Owner.class));
    }

    @Test
    void testLogin() {
        OwnerLoginRequest request = new OwnerLoginRequest();
        request.setEmail("prajwal@example.com");
        request.setPassword("password123");

        when(ownerService.loginOwner(request.getEmail(), request.getPassword())).thenReturn(owner);

        ResponseEntity<OwnerLoginResponse> response = ownerController.login(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(owner, response.getBody().getOwner());

        verify(ownerService, times(1)).loginOwner(request.getEmail(), request.getPassword());
    }

    @Test
    void testGetOwnerById() {
        when(ownerService.getOwnerById(1L)).thenReturn(owner);

        ResponseEntity<Owner> response = ownerController.getOwnerById(1L);

        assertNotNull(response);
        assertEquals(owner, response.getBody());
        verify(ownerService, times(1)).getOwnerById(1L);
    }

    @Test
    void testGetAllOwners() {
        List<Owner> owners = Arrays.asList(owner);
        when(ownerService.getAllOwners()).thenReturn(owners);

        ResponseEntity<List<Owner>> response = ownerController.getAllOwners();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(ownerService, times(1)).getAllOwners();
    }

    @Test
    void testUpdateOwner() {
        Owner updatedOwner = Owner.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .phone("9999999999")
                .build();

        when(ownerService.updateOwner(1L, updatedOwner)).thenReturn(updatedOwner);

        ResponseEntity<Owner> response = ownerController.updateOwner(1L, updatedOwner);

        assertNotNull(response);
        assertEquals(updatedOwner, response.getBody());
        verify(ownerService, times(1)).updateOwner(1L, updatedOwner);
    }

    @Test
    void testDeleteOwner() {
        doNothing().when(ownerService).deleteOwner(1L);

        ResponseEntity<Void> response = ownerController.deleteOwner(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(ownerService, times(1)).deleteOwner(1L);
    }
}
