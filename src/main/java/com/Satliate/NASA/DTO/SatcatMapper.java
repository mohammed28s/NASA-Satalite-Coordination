package com.Satliate.NASA.DTO;


import com.Satliate.NASA.Entity.Satellite;

public class SatcatMapper {

    public static Satellite toEntity(SatcatDto dto) {
        Satellite sat = new Satellite();
        sat.setNoradId(dto.getNORAD_CAT_ID());
        sat.setName(dto.getSATNAME());
        sat.setManufacturer(dto.getCOUNTRY());
        sat.setPurpose(dto.getPURPOSE());
        sat.setMassKg(dto.getMASS());

        // Directly set LocalDateTime
        sat.setLaunchDate(dto.getLAUNCH_DATE());

        // DECAY_DATE is ignored since your entity doesn’t have it
        return sat;
    }
}
