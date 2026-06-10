package com.Satliate.NASA.real.time.Satliate.Data.Shceduled;


import com.Satliate.NASA.real.time.Satliate.Data.Entity.Satellite;
import com.Satliate.NASA.real.time.Satliate.Data.Entity.TleRecord;
import com.Satliate.NASA.real.time.Satliate.Data.Repostiory.SatelliteRepository;
import com.Satliate.NASA.real.time.Satliate.Data.Repostiory.TleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TleFetcher {
    private final WebClient webClient = WebClient.create();
    private final SatelliteRepository satelliteRepo;
    private final TleRecordRepository tleRepo;

    @Scheduled(cron = "${app.tle.fetch-cron}", zone = "${app.tle.timezone}")
    public void fetch() {
        String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=active&FORMAT=TLE";
        String body = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
        if (body == null || body.isBlank()) return;

        String[] lines = body.split("\\r?\\n");
        for (int i = 0; i + 2 <= lines.length; i += 3) {
            String name = lines[i].trim();
            String l1 = lines[i+1].trim();
            String l2 = lines[i+2].trim();
            Integer norad = parseNoradFromLine1(l1);
            Satellite sat = satelliteRepo.findByNoradId(norad).orElseGet(() -> {
                Satellite s = new Satellite();
                s.setNoradId(norad);
                s.setName(name);
                return satelliteRepo.save(s);
            });

            OffsetDateTime epoch = parseEpochFromTle(l1);
            boolean exists = tleRepo.findFirstBySatelliteOrderByEpochDesc(sat)
                    .map(t -> t.getEpoch().equals(epoch))
                    .orElse(false);
            if (!exists) {
                TleRecord rec = new TleRecord();
                rec.setSatellite(sat);
                rec.setLine1(l1);
                rec.setLine2(l2);
                rec.setEpoch(epoch);
                rec.setFetchedAt(OffsetDateTime.now());
                rec.setSource("CelesTrak");
                tleRepo.save(rec);
            }
        }
    }

    private Integer parseNoradFromLine1(String line1) {
        try { return Integer.parseInt(line1.substring(2,7).trim()); }
        catch (Exception e) { return null; }
    }

    private OffsetDateTime parseEpochFromTle(String line1) {
        try {
            String epochStr = line1.substring(18,32).trim();
            int yy = Integer.parseInt(epochStr.substring(0,2));
            double dayOfYear = Double.parseDouble(epochStr.substring(2));
            int year = (yy >= 57) ? 1900 + yy : 2000 + yy;
            java.time.LocalDate date = java.time.LocalDate.ofYearDay(year, (int) dayOfYear);
            double fractional = dayOfYear - (int) dayOfYear;
            long seconds = (long) (fractional * 86400);
            return OffsetDateTime.of(date, java.time.LocalTime.MIDNIGHT.plusSeconds(seconds), java.time.ZoneOffset.UTC);
        } catch (Exception e) {
            return OffsetDateTime.now();
        }
    }
}