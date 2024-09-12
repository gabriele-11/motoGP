package com.example.crud.ServiceTest;

import com.example.crud.controller.PilotaController;
import com.example.crud.dto.PilotaDTO;
import com.example.crud.dto.create.CreateDTO;
import com.example.crud.dto.update.UpdateDTO;
import com.example.crud.model.Pilota;
import com.example.crud.model.Scuderia;
import com.example.crud.service.PilotaService;
import com.example.crud.service.ScuderiaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PilotaControllerTest {



    @Mock
    private PilotaService pilotaService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ScuderiaServiceImpl scuderiaServiceImpl;

    @InjectMocks
    private PilotaController pilotaController;

    private UUID pilotaId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPilotaSuccess() {

        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        Pilota pilota = new Pilota();
        pilota.setId(UUID.randomUUID());
        pilota.setScuderiaId(new Scuderia());

        List<Pilota> piloti = Collections.singletonList(pilota);
        Page<Pilota> pilotaPage = new PageImpl<>(piloti, pageRequest, piloti.size());

        when(pilotaService.findAll(pageRequest)).thenReturn(pilotaPage);
        when(modelMapper.map(pilota, PilotaDTO.class)).thenReturn(new PilotaDTO());

        ResponseEntity<Page<PilotaDTO>> response = pilotaController.getPilota(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testGetPilotaEmpty() {

        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Pilota> piloti = Collections.emptyList();
        Page<Pilota> pilotaPage = new PageImpl<>(piloti, pageRequest, 0);

        when(pilotaService.findAll(pageRequest)).thenReturn(pilotaPage);

        ResponseEntity<Page<PilotaDTO>> response = pilotaController.getPilota(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getContent().isEmpty());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    void testGetPilotaByIdSuccess() {

        UUID id = UUID.randomUUID();
        Pilota pilota = new Pilota();
        pilota.setId(id);
        Scuderia scuderia = new Scuderia();
        pilota.setScuderiaId(scuderia);

        PilotaDTO pilotaDTO = new PilotaDTO();

        try {
            when(pilotaService.findById(id)).thenReturn(pilota);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
            };

        when(modelMapper.map(pilota, PilotaDTO.class)).thenReturn(pilotaDTO);

        ResponseEntity<PilotaDTO> response = pilotaController.getPilotaById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pilotaDTO.getId(), response.getBody().getId());
    }

    @Test
    void testGetPilotaByIdNotFound() {

        UUID id = UUID.randomUUID();
        try {
            when(pilotaService.findById(id)).thenThrow(new ChangeSetPersister.NotFoundException());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }


        ResponseEntity<PilotaDTO> response = pilotaController.getPilotaById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeletePilotaSuccess() {

        UUID id = UUID.randomUUID();
        when(pilotaService.delete(id)).thenReturn(true);

        ResponseEntity<String> response = pilotaController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pilota eliminato", response.getBody());
    }

    @Test
    void testDeletePilotaNotFound() {

        UUID id = UUID.randomUUID();
        doThrow(new EntityNotFoundException()).when(pilotaService).delete(id);

        ResponseEntity<String> response = pilotaController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pilota non trovato", response.getBody());
    }


    @Test
    void testCreatePilotaSuccess() {

        CreateDTO createDto = new CreateDTO();
        createDto.setIdScuderia(UUID.randomUUID());
        Pilota pilotaToCreate = new Pilota();
        Pilota createdPilota = new Pilota();
        PilotaDTO createdPilotaDTO = new PilotaDTO();
        Scuderia scuderia = new Scuderia();

        when(modelMapper.map(createDto, Pilota.class)).thenReturn(pilotaToCreate);
        when(scuderiaServiceImpl.findById(createDto.getIdScuderia())).thenReturn(scuderia);
        when(pilotaService.save(pilotaToCreate)).thenReturn(createdPilota);
        when(modelMapper.map(createdPilota, PilotaDTO.class)).thenReturn(createdPilotaDTO);

        ResponseEntity<PilotaDTO> response = pilotaController.createPilota(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdPilotaDTO, response.getBody());
    }

    @Test
    void testUpdatePilota_Success() {
        // Setup del mock
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setNazionalita("Italiano");
        updateDTO.setModelloMoto("Modello X");

        Pilota pilotaToUpdate = new Pilota();
        pilotaToUpdate.setId(pilotaId);
        pilotaToUpdate.setNazionalita("Italiano");
        pilotaToUpdate.setModelloMoto("Modello X");

        Pilota pilotaToSave = new Pilota();
        pilotaToSave.setId(pilotaId);
        pilotaToSave.setNazionalita("Italiano");
        pilotaToSave.setModelloMoto("Modello X");

        PilotaDTO updatedPilotaDTO = new PilotaDTO();
        updatedPilotaDTO.setId(pilotaId);
        updatedPilotaDTO.setNazionalita("Italiano");
        updatedPilotaDTO.setModelloMoto("Modello X");

        when(modelMapper.map(updateDTO, Pilota.class)).thenReturn(pilotaToUpdate);
        when(pilotaService.update(pilotaToUpdate)).thenReturn(pilotaToSave);
        when(modelMapper.map(pilotaToSave, PilotaDTO.class)).thenReturn(updatedPilotaDTO);

        // Esegui il metodo da testare
        ResponseEntity<PilotaDTO> response = pilotaController.updatePilota(pilotaId, updateDTO);

        // Verifica la risposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pilotaId, response.getBody().getId());
        assertEquals("Italiano", response.getBody().getNazionalita());
    }

    @Test
    void testUpdatePilota_NotFound() {
        // Setup del mock per il caso di eccezione
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setNazionalita("Italiano");

        when(modelMapper.map(updateDTO, Pilota.class)).thenReturn(new Pilota());
        when(pilotaService.update(any(Pilota.class))).thenThrow(new EntityNotFoundException());

        // Esegui il metodo da testare
        ResponseEntity<PilotaDTO> response = pilotaController.updatePilota(pilotaId, updateDTO);

        // Verifica la risposta
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}




