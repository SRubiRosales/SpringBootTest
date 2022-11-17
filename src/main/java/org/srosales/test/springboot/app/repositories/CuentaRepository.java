package org.srosales.test.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.srosales.test.springboot.app.models.Cuenta;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    //List<Cuenta> findAll();
    //Cuenta findById(Long id);
    //void update(Cuenta cuenta);
}
