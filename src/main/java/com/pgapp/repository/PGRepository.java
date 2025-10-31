package com.pgapp.repository;

import com.pgapp.entity.PG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PGRepository extends JpaRepository<PG, Long> {
    List<PG> findByOwnerId(Long ownerId);

    Optional<PG> findByName(String name);
    Optional<PG> findById(Long id);
    Optional<PG> findByNameIgnoreCase(String name);

    //    List<PG> findByNameContainingIgnoreCase(String name);
//
//    List<PG> findByAddressContainingIgnoreCase(String address);
//
//    List<PG> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String name, String address);
//
//
//    // Advanced search with filters
//    @Query("SELECT DISTINCT pg FROM PG pg JOIN pg.floors f JOIN f.rooms r " +
//            "WHERE (:address IS NULL OR LOWER(pg.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
//            "AND (:minPrice IS NULL OR r.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
//            "AND (r.capacity - r.occupiedBeds) > 0")
//    List<PG> searchWithFilters(
//            @Param("address") String address,
//            @Param("minPrice") Double minPrice,
//            @Param("maxPrice") Double maxPrice
//    );
//
    List<PG> findByCityIgnoreCase(String city);
    List<PG> findByCityAndAreaIgnoreCase(String city, String area);
}
