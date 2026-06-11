//package com.Satliate.NASA.real.time.Satliate.Data.Controller;
//
//
//import com.Satliate.NASA.real.time.Satliate.Data.Repostiory.SatelliteRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
//import static org.slf4j.MDC.get;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class SatelliteControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private SatelliteRepository satelliteRepo;
//
//    @Test
//    void testListSatellites() throws Exception {
//        mockMvc.perform(get("/api/v1/satellites"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void testCreateSatellite() throws Exception {
//        String json = """
//            {
//              "noradId": 12345,
//              "name": "TEST-SAT",
//              "manufacturer": "TestCorp",
//              "massKg": 100.0,
//              "purpose": "Demo"
//            }
//            """;
//
//        mockMvc.perform(post("/api/v1/satellites")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("TEST-SAT"));
//    }
//
//    @Test
//    void testGetPosition() throws Exception {
//        // Insert a satellite manually for testing
//        Satellite sat = new Satellite();
//        sat.setNoradId(99999);
//        sat.setName("POS-SAT");
//        satelliteRepo.save(sat);
//
//        // Normally you'd also insert a TleRecord for this satellite
//        // For now, expect 500 or custom error until propagation is wired
//
//        mockMvc.perform(get("/api/v1/satellites/" + sat.getId() + "/position"))
//                .andExpect(status().is5xxServerError());
//    }
//}