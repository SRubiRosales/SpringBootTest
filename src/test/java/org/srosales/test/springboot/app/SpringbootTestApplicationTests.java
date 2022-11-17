package org.srosales.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.srosales.test.springboot.app.exceptions.DineroInsuficienteException;
import org.srosales.test.springboot.app.models.Banco;
import org.srosales.test.springboot.app.models.Cuenta;
import org.srosales.test.springboot.app.repositories.BancoRepository;
import org.srosales.test.springboot.app.repositories.CuentaRepository;
import org.srosales.test.springboot.app.services.CuentaService;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {
	@MockBean
	CuentaRepository cuentaRepository;
	@MockBean
	BancoRepository bancoRepository;

	@Autowired
	CuentaService service;

	@BeforeEach
	void setUp() {
	}

	@Test
	void contextLoads() {
		//Given
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());
		//When
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino= service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		//Then
		service.transferir(1L, 2L, new BigDecimal("100"), 1L);
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino= service.revisarSaldo(2L);
		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		int total = service.revisarTotalTransferencias(1L);
		assertEquals(1, total);
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads2() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());
		//When
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino= service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteException.class, ()-> {
			service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino= service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		int total = service.revisarTotalTransferencias(1L);
		assertEquals(0, total);
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));
		verify(bancoRepository, times(1)).findById(1L);
		verify(bancoRepository, never()).save(any(Banco.class));

		verify(cuentaRepository, times(5)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads3() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1 == cuenta2);
		assertEquals("Sharon", cuenta1.getPersona());
		assertEquals("Sharon", cuenta2.getPersona());

		verify(cuentaRepository, times(2)).findById(1L);
	}
}
