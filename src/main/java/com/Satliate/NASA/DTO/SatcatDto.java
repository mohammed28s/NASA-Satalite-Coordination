package com.Satliate.NASA.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SatcatDto {  // this is the satellite information
    private Integer NORAD_CAT_ID;
    private String SATNAME;
    private String COUNTRY;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // date and time
    private LocalDateTime LAUNCH_DATE;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // date and time
    private LocalDateTime DECAY_DATE;

    private Double MASS;
    private String PURPOSE;

    // getters and setters
}
