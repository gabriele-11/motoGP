package com.example.crud.ServiceTest;

import com.example.crud.model.Pilota;
import com.example.crud.model.Scuderia;
import com.example.crud.repository.ScuderiaRepository;
import com.example.crud.service.ScuderiaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Nested
class ScuderiaTest {

    @Mock
    private ScuderiaRepository scuderiaRepository;

    @InjectMocks
    private ScuderiaServiceImpl scuderiaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    void findAll()
    {
        Scuderia scuderia = new Scuderia();
        scuderia.setId(UUID.randomUUID());

        Page<Scuderia> scuderiaPage = new PageImpl<>(Arrays.asList(scuderia));

        when(scuderiaRepository.findAll(any(PageRequest.class))).thenReturn(scuderiaPage);

        Page<Scuderia> result = scuderiaRepository.findAll(PageRequest.of(0,10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());


    }

    @Test
    void testFindAll() {


        Scuderia scuderia1 = new Scuderia();
        scuderia1.setId(UUID.randomUUID());
        scuderia1.setScuderiaName("Scuderia 1");
        Scuderia scuderia2 = new Scuderia();
        scuderia2.setId(UUID.randomUUID());
        scuderia2.setScuderiaName("Scuderia 2");
        Scuderia scuderia3 = new Scuderia();
        scuderia3.setId(UUID.randomUUID());
        scuderia3.setScuderiaName("Scuderia 3");
        List<Scuderia> scuderie = List.of(scuderia1, scuderia2, scuderia3);


        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Scuderia> page = new PageImpl<>(scuderie, pageRequest, scuderie.size());

        when(scuderiaRepository.findAll(pageRequest)).thenReturn(page);

        Page<Scuderia> result = scuderiaService.findAll(pageRequest);

        assertEquals(page, result);
    }



    @Test
    void findById()
    {
        Scuderia scuderia = new Scuderia();

        when(scuderiaRepository.findById(any(UUID.class))).thenReturn(Optional.of(scuderia));

        Scuderia result = scuderiaService.findById(UUID.randomUUID());

        assertNotNull(result);

        assertEquals(scuderia.getId(), result.getId());

    }



    @Test
    void testSave() {

        Scuderia scuderiaToCreate = new Scuderia();
        scuderiaToCreate.setId(UUID.randomUUID());
        scuderiaToCreate.setScuderiaName("Ducati");
        when(scuderiaRepository.save(any(Scuderia.class))).thenReturn(scuderiaToCreate);

        Scuderia createdScuderia = scuderiaService.save(scuderiaToCreate);

        assertNotNull(createdScuderia);
        assertNotNull(createdScuderia.getScuderiaName());
        assertEquals(scuderiaToCreate.getScuderiaName(), createdScuderia.getScuderiaName());

    }

    @Test
    void testDelete() {

        UUID id = UUID.randomUUID();
        when(scuderiaRepository.existsById(id)).thenReturn(true);

        boolean result = scuderiaService.delete(id);
        assertTrue(result);
    }

    @Test
    void testFindById_NotFound()  {


        UUID id = UUID.randomUUID();
        when(scuderiaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> scuderiaService.findById(id));

    }


    @Test
    void save_WithNullFields() {

        Scuderia scuderia = new Scuderia();
        scuderia.setId(UUID.randomUUID());
        scuderia.setScuderiaName(null);

        Scuderia savedScuderia = scuderiaService.save(scuderia);

        assertNull(savedScuderia);
    }

    @Test
    void delete_WithNullFields() {
        UUID id = UUID.randomUUID();
        when(scuderiaRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> scuderiaService.delete(id));
    }

    @Test
    void delete_notPermitted() {
        UUID id = UUID.randomUUID();
        Scuderia scuderia = new Scuderia();
        scuderia.setScuderiaName(("scuderia con piloti"));

        List<Pilota> piloti = new ArrayList<>();
        piloti.add(new Pilota());
        scuderia.setPiloti(piloti);

        when(scuderiaRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("la scuderia contiene dei piloti")).when(scuderiaRepository).deleteById(id);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> { scuderiaService.delete(id);
                });

        assertNotEquals("Exeption",exception.getMessage());

    }

    @Test
    void testFindByIdWithPiloti() {
        UUID id = UUID.randomUUID();
        Scuderia scuderia = new Scuderia();
        List<Pilota> piloti = new ArrayList<>();
        scuderia.setPiloti(piloti);
        scuderia.setId(id);
        scuderia.setScuderiaName("Ducati");
        piloti.add(new Pilota());



        when(scuderiaRepository.findById(id)).thenReturn(Optional.of(scuderia));
        Scuderia result = scuderiaService.findById(id);

        assertEquals(scuderia.getId(), result.getId());
        assertEquals(1, result.getPiloti().size());

    }

    @Test
    void testFindByIdWithPilotiNotFound() {
        UUID id = UUID.randomUUID();
        when(scuderiaRepository.findById(id)).thenReturn(Optional.empty());

        try {
            scuderiaService.findByIdWithPiloti(id);
        } catch (EntityNotFoundException e) {
            assertEquals("Scuderia non trovata con ID: " + id, e.getMessage());
        }
    }

}
