package com.pgapp.converter;

import com.pgapp.converter.tenant.TenantConverter;
import com.pgapp.request.owner.BedRequest;
import com.pgapp.response.BedResponse;
import com.pgapp.entity.Bed;

public class BedConverter {

    public static Bed toEntity(BedRequest request) {
        Bed bed = new Bed();
        bed.setBedNumber(request.getBedNumber());
        bed.setOccupied(request.isOccupied());
        bed.setShortTermOccupied(request.isShortTermOccupied());
        return bed;
    }

    public static BedResponse toResponse(Bed bed) {
        BedResponse res = new BedResponse();
        res.setId(bed.getId());
        res.setBedNumber(bed.getBedNumber());
        res.setOccupied(bed.isOccupied());
        res.setShortTermOccupied(bed.isShortTermOccupied());
        if (bed.getTenant() != null) {
            res.setTenant(TenantConverter.toResponse(bed.getTenant()));
        }

        return res;
    }
}

