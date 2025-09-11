package com.pgapp.service;

import com.pgapp.dto.TenantApplicationDTO;
import com.pgapp.entity.*;
import com.pgapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantApplicationService {

    private final TenantApplicationRepository applicationRepository;
    private final TenantRepository tenantRepository;
    private final PGRepository pgRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepo; // alias if needed
    private final FloorRepository floorRepository;
    // (tenantRepository and tenantRepo may be the same; keep one consistent)

//    @Transactional
//    public TenantApplicationDTO applyForRoom(Long tenantId, Long pgId, Long roomId) {
//        Tenant tenant = tenantRepository.findById(tenantId)
//                .orElseThrow(() -> new RuntimeException("Tenant not found"));
//        PG pg = pgRepository.findById(pgId)
//                .orElseThrow(() -> new RuntimeException("PG not found"));
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        TenantApplication application = TenantApplication.builder()
//                .tenant(tenant)
//                .pg(pg)
//                .room(room)
//                .status(ApplicationStatus.PENDING)
//                .build();
//
//        TenantApplication saved = applicationRepository.save(application);
//        return toDto(saved);
//    }

    @Transactional
    public TenantApplicationDTO applyForRoomByDetails(Long tenantId, String pgName, Integer floorNumber, String roomNumber) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        PG pg = pgRepository.findByName(pgName)
                .orElseThrow(() -> new RuntimeException("PG not found"));

        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), floorNumber)
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        TenantApplication application = TenantApplication.builder()
                .tenant(tenant)
                .pg(pg)
                .room(room)
                .status(ApplicationStatus.PENDING)
                .build();

        return toDto(applicationRepository.save(application));
    }


    @Transactional(readOnly = true)
    public List<TenantApplicationDTO> getApplicationsForPG(Long pgId) {
        return applicationRepository.findByPgId(pgId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TenantApplicationDTO> getApplicationsForTenant(Long tenantId) {
        return applicationRepository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TenantApplicationDTO updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        TenantApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);

        if (status == ApplicationStatus.APPROVED) {
            Room room = application.getRoom();
            if (room.getCapacity() - room.getOccupiedBeds() <= 0) {
                throw new RuntimeException("No vacant beds in this room");
            }
            // Assign tenant to PG and Room
            Tenant tenant = application.getTenant();
            tenant.setPg(application.getPg());
            tenant.setRoom(room);

            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
            roomRepository.save(room);
            tenantRepository.save(tenant);
        }

        TenantApplication saved = applicationRepository.save(application);
        return toDto(saved);
    }

    // mapping helper (must access lazy fields inside @Transactional)
    private TenantApplicationDTO toDto(TenantApplication app) {
        Room room = app.getRoom();
        return TenantApplicationDTO.builder()
                .id(app.getId())
                .status(app.getStatus() == null ? null : app.getStatus().name())

                .tenantId(app.getTenant() != null ? app.getTenant().getId() : null)
                .tenantName(app.getTenant() != null ? app.getTenant().getName() : null)

                .pgId(app.getPg() != null ? app.getPg().getId() : null)
                .pgName(app.getPg() != null ? app.getPg().getName() : null)

                .roomId(room != null ? room.getId() : null)
                .roomNumber(room != null ? room.getRoomNumber() : null)
                .roomCapacity(room != null ? room.getCapacity() : null)
                .roomOccupiedBeds(room != null ? room.getOccupiedBeds() : null)
                .roomVacantBeds(room != null && room.getCapacity() != null && room.getOccupiedBeds() != null
                        ? room.getCapacity() - room.getOccupiedBeds()
                        : null)
                .build();
    }
}
