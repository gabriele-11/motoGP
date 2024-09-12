package com.example.crud.service;

import com.example.crud.model.Scuderia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface ScuderiaService {


    Page<Scuderia> findAll(PageRequest pageRequest);

    Scuderia findById(UUID id);

    Scuderia findByIdWithPiloti(UUID id);

    Scuderia save(Scuderia scuderia);

    boolean delete(UUID id);
}
