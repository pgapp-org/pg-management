//package com.pgapp.controller;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//
//import com.pgapp.entity.Owner;
//import com.pgapp.repository.OwnerRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final OwnerRepository ownerRepo;
//
//    public AuthController(OwnerRepository ownerRepo) {
//        this.ownerRepo = ownerRepo;
//    }
//
//    @PostMapping("/google")
//    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) throws Exception {
//        String idTokenString = body.get("idToken");
//
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
//                .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com"))
//                .build();
//
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (idToken != null) {
//            Payload payload = idToken.getPayload();
//            String email = payload.getEmail();
//            String name = (String) payload.get("name");
//
//            Owner owner = ownerRepo.findByEmail(email)
//                    .orElseGet(() -> ownerRepo.save(
//                            Owner.builder()
//                                    .email(email)
//                                    .name(name)
//                                    .createdAt(LocalDateTime.now())
//                                    .build()
//                    ));
//
//            return ResponseEntity.ok(owner); // ✅ return owner info for frontend
//        } else {
//            return ResponseEntity.status(401).body("Invalid Google ID Token");
//        }
//    }
//}
