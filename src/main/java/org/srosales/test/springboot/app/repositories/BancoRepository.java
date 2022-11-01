package org.srosales.test.springboot.app.repositories;

import org.srosales.test.springboot.app.models.Banco;

import java.util.List;

public interface BancoRepository {
    List<Banco> findAll();
    Banco findById(Long id);
    void update(Banco banco);
}
