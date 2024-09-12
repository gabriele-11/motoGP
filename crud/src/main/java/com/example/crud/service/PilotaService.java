package com.example.crud.service;

import com.example.crud.model.Pilota;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface PilotaService {

    Page<Pilota> findAll(PageRequest pageRequest);

    Pilota findById(UUID id) throws ChangeSetPersister.NotFoundException;

    Pilota save(Pilota pilota);

    Pilota update(Pilota pilota);

    boolean delete(UUID id);

}
