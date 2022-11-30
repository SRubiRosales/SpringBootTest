package org.srosales.test.springboot.app.controllers;

import static org.springframework.http.HttpStatus.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.srosales.test.springboot.app.models.Cuenta;
import org.srosales.test.springboot.app.services.CuentaService;

@Controller
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Cuenta detail(@PathVariable(name = "id") Long id) {
        return cuentaService.findById(id);
    }
}
