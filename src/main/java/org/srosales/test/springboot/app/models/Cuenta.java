package org.srosales.test.springboot.app.models;

import org.srosales.test.springboot.app.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.text.Bidi;

public class Cuenta {
    private Long id;
    private String persona;
    private BigDecimal saldo;

    public Cuenta() {
    }

    public Cuenta(Long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        this.saldo = this.saldo.subtract(monto);
    }

    public void credito(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.saldo.add(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Saldo insuficiente en la cuenta");
        }
        this.saldo = nuevoSaldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cuenta cuenta = (Cuenta) o;

        if (id != null ? !id.equals(cuenta.id) : cuenta.id != null) return false;
        if (persona != null ? !persona.equals(cuenta.persona) : cuenta.persona != null) return false;
        return saldo != null ? saldo.equals(cuenta.saldo) : cuenta.saldo == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (persona != null ? persona.hashCode() : 0);
        result = 31 * result + (saldo != null ? saldo.hashCode() : 0);
        return result;
    }
}
