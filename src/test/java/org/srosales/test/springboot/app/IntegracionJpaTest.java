package org.srosales.test.springboot.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.srosales.test.springboot.app.models.Cuenta;
import org.srosales.test.springboot.app.repositories.CuentaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Sharon", cuenta.get().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Sharon");
        assertTrue(cuenta.isPresent());
        assertEquals("Sharon", cuenta.get().getPersona());
        assertEquals("1000.00", cuenta.get().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rubi");
        assertThrows(NoSuchElementException.class, cuenta::get);
        assertTrue(!cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }
}
