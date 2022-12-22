package org.srosales.test.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.srosales.test.springboot.app.models.TransaccionDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTestRestTemplateTest {
    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() {
        // Given
        TransaccionDto dto = new TransaccionDto();
        dto.setMonto(new BigDecimal("100"));
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);

        // When
        ResponseEntity<String> response = client
                .postForEntity(crearUri("/api/cuentas/transferir"), dto, String.class);
        System.out.println(port);
        String json = response.getBody();
        System.out.println(json);
        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito"));
        assertTrue(json.contains("{\"cuentaOrigenId\":1,\"cuentaDestinoId\":2,\"monto\":100,\"bancoId\":1}"));
    }

    private String crearUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}