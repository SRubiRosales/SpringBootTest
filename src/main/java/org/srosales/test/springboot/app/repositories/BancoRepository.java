package org.srosales.test.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.srosales.test.springboot.app.models.Banco;

import java.util.List;

public interface BancoRepository extends JpaRepository<Banco, Long> {
    //List<Banco> findAll();
    //Banco findById(Long id);
    //void update(Banco banco);
}
