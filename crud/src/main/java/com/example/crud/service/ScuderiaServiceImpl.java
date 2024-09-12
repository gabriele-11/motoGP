package com.example.crud.service;


import com.example.crud.model.Scuderia;
import com.example.crud.repository.ScuderiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScuderiaServiceImpl implements ScuderiaService {

    private  final ScuderiaRepository scuderiaRepository;


    public ScuderiaServiceImpl(ScuderiaRepository scuderiaRepository) {
        this.scuderiaRepository = scuderiaRepository;
    }

    public static final String TEXT = "Scuderia non trovata con ID: ";


    @Override
    public Page<Scuderia> findAll(PageRequest pageRequest) {
        return this.scuderiaRepository.findAll(pageRequest);
    }

    @Override
    public Scuderia findById(UUID id) {
        return scuderiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TEXT + id));
    }

    @Override
    public Scuderia findByIdWithPiloti(UUID id) {
        return scuderiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TEXT + id));
    }

    @Override
    public Scuderia save(Scuderia scuderia) {
        
        return scuderiaRepository.save(scuderia);
    }

    @Override
    public boolean delete(UUID id) {
        if(!scuderiaRepository.existsById(id)) {
            throw new EntityNotFoundException(TEXT + id);
        }
        deleteById(id);
        return true;
    }

    public void deleteById(UUID id) {
        scuderiaRepository.deleteById(id);
    }


}


