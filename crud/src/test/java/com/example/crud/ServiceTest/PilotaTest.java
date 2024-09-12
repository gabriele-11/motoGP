package com.example.crud.ServiceTest;

import com.example.crud.model.Pilota;
import com.example.crud.repository.PilotaRepository;
import com.example.crud.service.PilotaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Nested
class PilotaServiceImplTest {

    @Mock
    private PilotaRepository pilotaRepository;

    @InjectMocks
    private PilotaServiceImpl pilotaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pilota pilota = new Pilota();
        Page<Pilota> pilotaPage = new PageImpl<>(Arrays.asList(pilota));
        when(pilotaRepository.findAll(any(PageRequest.class))).thenReturn(pilotaPage);

        Page<Pilota> result = pilotaService.findAll(PageRequest.of(0,10));

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindById() throws ChangeSetPersister.NotFoundException {
        Pilota pilota = new Pilota();
        when(pilotaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pilota));

        Pilota result = pilotaService.findById(UUID.randomUUID());

        assertNotNull(result);

    }

    @Test
    void testGetPilotaById_notFound() {


            when(pilotaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThrows(ChangeSetPersister.NotFoundException.class, () -> pilotaService.findById(UUID.randomUUID()));

        }


    @Test
    void testSave() {

        Pilota pilotaToCreate = new Pilota();
        pilotaToCreate.setId(UUID.randomUUID());
        pilotaToCreate.setNome("Franco");
        pilotaToCreate.setCognome("Franchi");
        pilotaToCreate.setNazionalita("Italia");
        pilotaToCreate.setDataNascita(Date.valueOf("1999-01-01"));
        pilotaToCreate.setNomeScuderia("Ducati");
        pilotaToCreate.setModelloMoto("Modello 1");



        when(pilotaRepository.save(any(Pilota.class))).thenReturn(pilotaToCreate);


        Pilota createdPilota = pilotaService.save(pilotaToCreate);

        assertNotNull(createdPilota);
        assertEquals(pilotaToCreate.getNome(), createdPilota.getNome());
        assertEquals(pilotaToCreate.getCognome(), createdPilota.getCognome());
        assertEquals(pilotaToCreate.getNazionalita(), createdPilota.getNazionalita());
        assertEquals(pilotaToCreate.getDataNascita(), createdPilota.getDataNascita());
        assertEquals(pilotaToCreate.getNomeScuderia(), createdPilota.getNomeScuderia());
        assertEquals(pilotaToCreate.getModelloMoto(), createdPilota.getModelloMoto());

    }


    @Test
    void testUpdate() {

        Pilota pilota = new Pilota();
        pilota.setId(UUID.randomUUID());
        when(pilotaRepository.findById(any(UUID.class))).thenReturn(Optional.of(pilota));
        when(pilotaRepository.save(any(Pilota.class))).thenReturn(pilota);

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(pilota.getId());
        Pilota result = pilotaService.update(updatedPilota);

        assertNotNull(result);
        assertEquals(pilota.getId(), result.getId());
    }

    @Test
    void testUpdate_NotFound() {

        when(pilotaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () -> pilotaService.update(updatedPilota));

    }

    @Test
    void testDelete() {

        UUID id = UUID.randomUUID();
        when(pilotaRepository.existsById(id)).thenReturn(true);

        boolean result = pilotaService.delete(id);
        assertTrue(result);

    }



    @Test
    void testDelete_NotFound() {

        UUID id = UUID.randomUUID();
        when(pilotaRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> pilotaService.delete(id));
    }



    @Test
    void update_PilotaExists_UpdatesNazionalitaIfNotNull() {
        Pilota existingPilota = new Pilota();
        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setNazionalita("Francese");

        pilotaService.update(updatedPilota);


        assertEquals(updatedPilota.getNazionalita(), existingPilota.getNazionalita());
    }

    @Test
    void update_PilotaExists_DoesNotUpdateNazionalitaIfNull() {

        Pilota existingPilota = new Pilota();

        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setNazionalita(null);


        pilotaService.update(updatedPilota);

        assertNull(existingPilota.getNazionalita());
    }



    @Test
    void update_PilotaExists_UpdatesNomeScuderiaIfNotNull() {
        Pilota existingPilota = new Pilota();
        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setNomeScuderia("Ducati");

        pilotaService.update(updatedPilota);


        assertEquals(updatedPilota.getNomeScuderia(), existingPilota.getNomeScuderia());
    }

    @Test
    void update_PilotaExists_DoesNotUpdateNomeScuderiaIfNull() {

        Pilota existingPilota = new Pilota();

        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setNomeScuderia(null);


        pilotaService.update(updatedPilota);

        assertNull(existingPilota.getNomeScuderia());
    }


    @Test
    void update_PilotaExists_UpdatesModelloMotoIfNotNull() {
        Pilota existingPilota = new Pilota();
        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setModelloMoto("Moto 1");

        pilotaService.update(updatedPilota);


        assertEquals(updatedPilota.getModelloMoto(), existingPilota.getModelloMoto());
    }

    @Test
    void update_PilotaExists_DoesNotUpdateModelloMotoIfNull() {

        Pilota existingPilota = new Pilota();

        when(pilotaRepository.findById(existingPilota.getId())).thenReturn(Optional.of(existingPilota));

        Pilota updatedPilota = new Pilota();
        updatedPilota.setId(existingPilota.getId());
        updatedPilota.setModelloMoto(null);


        pilotaService.update(updatedPilota);

        assertNull(existingPilota.getModelloMoto());
    }



}
