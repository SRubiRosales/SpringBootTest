package org.srosales.test.springboot.app;

import org.srosales.test.springboot.app.models.Banco;
import org.srosales.test.springboot.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static final Cuenta CUENTA_001 = new Cuenta(1L, "Sharon", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L, "Rubi", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "Banco de prueba", 0);
    public static Optional<Cuenta> crearCuenta001() {
        return Optional.of(new Cuenta(1L, "Sharon", new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearCuenta002() {
        return Optional.of(new Cuenta(2L, "Rubí", new BigDecimal("2000")));
    }
    public static Optional<Banco> crearBanco() {
        return Optional.of(new Banco(1L, "Banco de prueba", 0));
    }
}
