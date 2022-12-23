package org.srosales.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.srosales.test.springboot.app.models.Cuenta;
import org.srosales.test.springboot.app.models.TransaccionDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integracion_rt")
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
    void testTransferir() throws JsonProcessingException {
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

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("date", LocalDate.now().toString());
        responseMap.put("status", "OK");
        responseMap.put("mensaje", "Transferencia realizada con éxito");
        responseMap.put("transaccion", dto);

        assertEquals(objectMapper.writeValueAsString(responseMap), json);
    }

    @Test
    @Order(2)
    void testDetalle() {
        ResponseEntity<Cuenta> response = client.getForEntity(crearUri("/api/cuentas/1"), Cuenta.class);
        Cuenta cuenta = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(cuenta);
        assertEquals("Sharon", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
        assertEquals(new Cuenta(1L, "Sharon", new BigDecimal("900.00")), cuenta);
    }

    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> response = client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(2, cuentas.size());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("Sharon", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());
        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("Rubí", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Sharon", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Rubí", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());
    }

    @Test
    @Order(4)
    void testGuardar() {
        //Given
        Cuenta cuenta = new Cuenta(null, "Pepa", new BigDecimal("3800"));
        //When
        ResponseEntity<Cuenta> response = client.postForEntity(crearUri("/api/cuentas"), cuenta, Cuenta.class);
        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Cuenta cuentaNueva = response.getBody();
        assertNotNull(cuentaNueva);
        assertEquals(3L, cuentaNueva.getId());
        assertEquals("Pepa", cuentaNueva.getPersona());
        assertEquals("3800", cuentaNueva.getSaldo().toPlainString());
    }

    @Test
    @Order(5)
    void testEliminar() {
        // 3 cuentas antes de eliminar
        ResponseEntity<Cuenta[]> response = client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(3, cuentas.size());

//        client.delete(crearUri("/api/cuentas/3"));
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = client.exchange(crearUri("/api/cuentas/{id}"), HttpMethod.DELETE, null, Void.class, pathVariables);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        // 2 cuentas antes de eliminar
        response = client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
        cuentas = Arrays.asList(response.getBody());
        assertEquals(2, cuentas.size());

        ResponseEntity<Cuenta> responseDetalle = client.getForEntity(crearUri("/api/cuentas/3"), Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND, responseDetalle.getStatusCode());
        assertFalse(responseDetalle.hasBody());
    }

    private String crearUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}