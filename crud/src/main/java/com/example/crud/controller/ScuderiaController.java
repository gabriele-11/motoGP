package com.example.crud.controller;


import com.example.crud.dto.create.CreateScuderiaDTO;
import com.example.crud.dto.create.response.ScuderiaResponseDTO;
import com.example.crud.dto.PilotaDTO;
import com.example.crud.dto.ScuderiaDTO;
import com.example.crud.model.Scuderia;
import com.example.crud.service.ScuderiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scuderie")
public class ScuderiaController {

    private final ScuderiaService scuderiaService;
    private final ModelMapper modelMapper;

    public ScuderiaController(ScuderiaService scuderiaService, ModelMapper modelMapper) {
        this.scuderiaService = scuderiaService;
        this.modelMapper = modelMapper;

    }

    @Operation(summary = "Get all Scuderie")
    @ApiResponse(responseCode = "200")
    @GetMapping("/")
    public ResponseEntity<Page<ScuderiaDTO>> getScuderia(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Scuderia> scuderie = scuderiaService.findAll(pageRequest);



        List<ScuderiaDTO> scuderiaDTOList = scuderie.getContent().stream()
                .map(scuderia -> {
                    ScuderiaDTO dto = new ScuderiaDTO();
                    dto.setId(scuderia.getId());
                    dto.setNomeScuderia(scuderia.getScuderiaName());
                    return dto;
                })
                .toList();

        Page<ScuderiaDTO> scuderiaDTOPage = new PageImpl<>(scuderiaDTOList, pageRequest, scuderie.getTotalElements());

        return new ResponseEntity<>(scuderiaDTOPage, HttpStatus.OK);
    }

    @Operation(summary = "Crea una nuova scuderia")
    @ApiResponse(responseCode = "201", description = "Scuderia creata correttamente")
    @PostMapping("/")
    public ResponseEntity<ScuderiaDTO> createScuderia(@Validated @RequestBody CreateScuderiaDTO createScuderiaDTO) {
        Scuderia scuderiaToCreate = modelMapper.map(createScuderiaDTO, Scuderia.class);
        scuderiaToCreate.setId(UUID.randomUUID());
        scuderiaToCreate.setScuderiaName(createScuderiaDTO.getNomeScuderia());
        Scuderia createdScuderia = this.scuderiaService.save(scuderiaToCreate);
        ScuderiaDTO createdScuderiaDTO = modelMapper.map(createdScuderia, ScuderiaDTO.class);
        return new ResponseEntity<>(createdScuderiaDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Trova una scuderia con i Piloti associati")
    @GetMapping("/{id}")
    public ResponseEntity<ScuderiaResponseDTO> getScuderiaById(@PathVariable UUID id) {
        try {

            Scuderia scuderia = scuderiaService.findByIdWithPiloti(id);
            ScuderiaResponseDTO scuderiaResponseDTO = new ScuderiaResponseDTO();
            scuderiaResponseDTO.setNomeScuderia(scuderia.getScuderiaName());

            List<PilotaDTO> pilotaDTOList = scuderia.getPiloti().stream()
                    .map(pilota -> modelMapper.map(pilota, PilotaDTO.class)).toList();

            scuderiaResponseDTO.setPiloti(pilotaDTOList);
            return ResponseEntity.ok(scuderiaResponseDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete Scuderia")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        try {
            scuderiaService.delete(id);
            return ResponseEntity.ok("Scuderia eliminata");
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException d) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
