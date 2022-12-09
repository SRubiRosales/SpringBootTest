package org.srosales.test.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.srosales.test.springboot.app.models.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    //List<Cuenta> findAll();
    //Cuenta findById(Long id);
    //void update(Cuenta cuenta);
    @Query("SELECT c FROM Cuenta c WHERE c.persona=?1")
    Optional<Cuenta> findByPersona(String persona);
}
