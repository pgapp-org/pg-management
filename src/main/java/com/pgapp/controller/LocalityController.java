package com.pgapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
//
//@RestController
//@RequestMapping("/api/localities")
//@CrossOrigin(origins = "http://localhost:4200")
//public class LocalityController {
//
//    private static final Map<String, List<Map<String, String>>> cityLocalities = new HashMap<>();
//
//    static {
//        // ✅ Bengaluru Localities
//        cityLocalities.put("Bangalore", List.of(
//                Map.of("name", "Maruthi Nagar", "image", "/assets/localities/maruthi.jpg"),
//                Map.of("name", "Veerannapalya", "image", "/assets/localities/veeranna.jpg"),
//                Map.of("name", "Koramangala", "image", "/assets/localities/koramangala.jpg"),
//                Map.of("name", "Whitefield", "image", "/assets/localities/whitefield.jpg")
//        ));
//
//        // ✅ Hyderabad Localities
//        cityLocalities.put("hyderabad", List.of(
//                Map.of("name", "Gachibowli", "image", "/assets/localities/gachibowli.jpg"),
//                Map.of("name", "Madhapur", "image", "/assets/localities/madhapur.jpg"),
//                Map.of("name", "Kondapur", "image", "/assets/localities/kondapur.jpg")
//        ));
//
//        // ✅ Chennai Localities
//        cityLocalities.put("chennai", List.of(
//                Map.of("name", "T. Nagar", "image", "/assets/localities/tnagar.jpg"),
//                Map.of("name", "Velachery", "image", "/assets/localities/velachery.jpg"),
//                Map.of("name", "OMR", "image", "/assets/localities/omr.jpg")
//        ));
//    }
//
//    @GetMapping("/{city}")
//    public List<Map<String, String>> getLocalitiesByCity(@PathVariable String city) {
//        return cityLocalities.getOrDefault(city.toLowerCase(), List.of());
//    }
//  }

@RestController
@RequestMapping("/api/pgs/localities")
@CrossOrigin(origins = "http://localhost:4200")
public class LocalityController {

    // 🔹 Hardcoded list of localities (replace with DB later)
    private static final Map<String, List<Map<String, String>>> cityLocalities = new HashMap<>();

    static {
        cityLocalities.put("Bangalore", Arrays.asList(
                Map.of("name", "BTM Layout", "image", "http://localhost:8080/images/btm.jpg"),
                Map.of("name", "electronic city", "image", "http://localhost:8080/images/electronic city.jpg"),
                Map.of("name", "Nagawara", "image", "http://localhost:8080/images/Nagawara.jpg"),
                Map.of("name", "silkboard", "image", "http://localhost:8080/images/silkboard.jpg")
        ));

        cityLocalities.put("Hyderabad", Arrays.asList(
                Map.of("name", "Madhapur", "image", "/images/madhapur.jpg"),
                Map.of("name", "Gachibowli", "image", "/images/gachibowli.jpg")
        ));
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<Map<String, String>>> getLocalitiesByCity(@PathVariable String city) {
        List<Map<String, String>> localities = cityLocalities.get(city);
        if (localities == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(localities);
    }
}
