package com.Satliate.NASA.Controller;

import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SatelliteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private com.Satliate.NASA.Service.SpaceTrackService spaceTrackService;

    @MockitoBean
    private com.Satliate.NASA.Service.PropagationService propagationService;

    @MockitoBean
    private com.Satliate.NASA.Service.SpaceTrackAuthService authService;

    @MockitoBean
    private com.Satliate.NASA.Service.SatelliteSyncService satelliteSyncService;

    @Autowired
    private SatelliteRepository satelliteRepo;

    @Test
    void listSatellites() throws Exception {
        mockMvc.perform(get("/api/v1/satellites/fetch"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void createSatellite() throws Exception {
        String json = """
            {
              "noradId": 12345,
              "name": "TEST-SAT",
              "manufacturer": "TestCorp",
              "massKg": 100.0,
              "purpose": "Demo"
            }
            """;

        mockMvc.perform(post("/api/v1/satellites/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TEST-SAT"));
    }

    @Test
    void getPositionWithoutTleReturnsNotFound() throws Exception {
        Satellite sat = new Satellite();
        sat.setNoradId(99999);
        sat.setName("POS-SAT");
        satelliteRepo.save(sat);

        mockMvc.perform(get("/api/v1/satellites/" + sat.getId() + "/position"))
                .andExpect(status().isNotFound());
    }
}
