package com.Satliate.NASA.Controller;

import com.Satliate.NASA.Service.NasaEarthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NasaEarthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NasaEarthService nasaEarthService;

    @Test
    void getEarthImage() throws Exception {
        String mockResponse = "{\"id\": \"test-image\"}";
        when(nasaEarthService.getEarthImage(anyDouble(), anyDouble(), anyString(), anyDouble()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/earth/image")
                        .param("lon", "100.75")
                        .param("lat", "1.5")
                        .param("date", "2014-02-01")
                        .param("dim", "0.025"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));
    }
}
