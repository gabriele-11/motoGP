package com.example.crud.service;

import com.example.crud.model.Pilota;
import com.example.crud.repository.PilotaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PilotaServiceImpl implements PilotaService {

    public PilotaServiceImpl(PilotaRepository pilotaRepository) {

        this.pilotaRepository = pilotaRepository;

    }

    private final PilotaRepository pilotaRepository;

    @Override
    public Page<Pilota> findAll(PageRequest pageRequest) {

        return this.pilotaRepository.findAll(pageRequest);

    }

    @Override
    public Pilota findById(UUID id) throws ChangeSetPersister.NotFoundException {

        Optional<Pilota> optionalPilota = this.pilotaRepository.findById(id);
        return optionalPilota.orElseThrow(ChangeSetPersister.NotFoundException::new);

    }

    @Override
    public Pilota save(Pilota pilota) {

        return this.pilotaRepository.save(pilota);

    }


    public Pilota update(Pilota pilota) {

    Pilota existingPilota = this.pilotaRepository.findById(pilota.getId()).orElseThrow((
            ) -> new EntityNotFoundException((Exception) null));
    existingPilota.getId();
    if (pilota.getNazionalita() != null) {
        existingPilota.setNazionalita(pilota.getNazionalita());
    }
    if (pilota.getNomeScuderia() != null) {
        existingPilota.setNomeScuderia(pilota.getNomeScuderia());
    }
    if (pilota.getModelloMoto() != null) {
        existingPilota.setModelloMoto(pilota.getModelloMoto());
    }
    return pilotaRepository.save(existingPilota);

    }

    @Override
    public boolean delete(UUID id) {

        if (!pilotaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pilota non trovato");
    }
        deleteById(id);
        return true;

    }

    private void deleteById(UUID id) {
        pilotaRepository.deleteById(id);
    }
}

