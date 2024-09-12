package com.example.crud.ServiceTest;

import com.example.crud.controller.ScuderiaController;
import com.example.crud.dto.PilotaDTO;
import com.example.crud.dto.ScuderiaDTO;
import com.example.crud.dto.create.CreateScuderiaDTO;
import com.example.crud.dto.create.response.ScuderiaResponseDTO;
import com.example.crud.model.Pilota;
import com.example.crud.model.Scuderia;
import com.example.crud.service.ScuderiaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScuderiaControllerTest {


    @InjectMocks
    private ScuderiaController scuderiaController;

    @Mock
    private ScuderiaService scuderiaService;

    @Mock
    private ModelMapper modelMapper;

    private UUID scuderiaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllScuderie() {

        when(scuderiaService.findAll(PageRequest.of(0, 10))).thenReturn(
                new PageImpl<>(List.of(new Scuderia()), PageRequest.of(0, 10), 10L));

        ResponseEntity<Page<ScuderiaDTO>> response = scuderiaController.getScuderia(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }


    @Test
    void testDeleteScuderiaSuccess() {

        UUID id = UUID.randomUUID();
        when(scuderiaService.delete(id)).thenReturn(true);

        ResponseEntity<String> response = scuderiaController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Scuderia eliminata", response.getBody());
    }

    @Test
    void testDeleteScuderiaNotFound() {

        UUID id = UUID.randomUUID();
        doThrow(new EntityNotFoundException()).when(scuderiaService).delete(id);

        ResponseEntity<String> response = scuderiaController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testDeleteScuderiaConflict() {

        UUID id = UUID.randomUUID();
        doThrow(new DataIntegrityViolationException("Errore")).when(scuderiaService).delete(id);

        ResponseEntity<String> response = scuderiaController.delete(id);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testCreateScuderiaSuccess() {

        CreateScuderiaDTO createScuderiaDTO = new CreateScuderiaDTO();
        createScuderiaDTO.setNomeScuderia("Scuderia Test");

        Scuderia scuderiaToCreate = new Scuderia();
        scuderiaToCreate.setScuderiaName("Scuderia Test");

        Scuderia createdScuderia = new Scuderia();
        createdScuderia.setId(scuderiaToCreate.getId());
        createdScuderia.setScuderiaName("Scuderia Test");

        ScuderiaDTO createdScuderiaDTO = new ScuderiaDTO();
        createdScuderiaDTO.setId(createdScuderia.getId());
        createdScuderiaDTO.setNomeScuderia("Scuderia Test");

        when(modelMapper.map(createScuderiaDTO, Scuderia.class)).thenReturn(scuderiaToCreate);
        when(scuderiaService.save(scuderiaToCreate)).thenReturn(createdScuderia);
        when(modelMapper.map(createdScuderia, ScuderiaDTO.class)).thenReturn(createdScuderiaDTO);

        ResponseEntity<ScuderiaDTO> response = scuderiaController.createScuderia(createScuderiaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdScuderiaDTO.getId(), response.getBody().getId());
        assertEquals(createdScuderiaDTO.getNomeScuderia(), response.getBody().getNomeScuderia());
        assertNotNull(response.getBody().getNomeScuderia());
    }

    @Test
    void testGetScuderiaById_Success() {

        Scuderia scuderia = new Scuderia();
        scuderia.setScuderiaName("Scuderia Test");
        scuderia.setPiloti(List.of(new Pilota()));

        ScuderiaResponseDTO scuderiaResponseDTO = new ScuderiaResponseDTO();
        scuderiaResponseDTO.setNomeScuderia("Scuderia Test");
        scuderiaResponseDTO.setPiloti(List.of(new PilotaDTO()));

        when(scuderiaService.findByIdWithPiloti(scuderiaId)).thenReturn(scuderia);
        when(modelMapper.map(scuderia, ScuderiaResponseDTO.class)).thenReturn(scuderiaResponseDTO);


        ResponseEntity<ScuderiaResponseDTO> response = scuderiaController.getScuderiaById(scuderiaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Scuderia Test", response.getBody().getNomeScuderia());
    }

    @Test
    void testGetScuderiaById_NotFound() {

        when(scuderiaService.findByIdWithPiloti(scuderiaId)).thenThrow(new EntityNotFoundException());

        ResponseEntity<ScuderiaResponseDTO> response = scuderiaController.getScuderiaById(scuderiaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}


