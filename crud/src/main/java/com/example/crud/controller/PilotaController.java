package com.example.crud.controller;

import com.example.crud.dto.create.CreateDTO;
import com.example.crud.dto.PilotaDTO;
import com.example.crud.dto.update.UpdateDTO;
import com.example.crud.model.Pilota;
import com.example.crud.model.Scuderia;
import com.example.crud.service.PilotaService;
import com.example.crud.service.ScuderiaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
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
@RequestMapping("/piloti")
public class PilotaController {

    private final PilotaService pilotaService;
    private final ModelMapper modelMapper;
    private final ScuderiaServiceImpl scuderiaServiceImpl;

    public PilotaController(PilotaService pilotaService, ModelMapper modelMapper, ScuderiaServiceImpl scuderiaServiceImpl) {
        this.pilotaService = pilotaService;
        this.modelMapper = modelMapper;
        this.scuderiaServiceImpl = scuderiaServiceImpl;
    }



    @Operation(summary = "Get all Piloti")
    @ApiResponse(responseCode = "200")
    @GetMapping("/")
    public ResponseEntity<Page<PilotaDTO>> getPilota(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size",  defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Pilota> piloti = this.pilotaService.findAll(pageRequest);

        List<PilotaDTO> pilotaDTOList = piloti.getContent().stream()
                .map(pilota -> modelMapper.map(pilota, PilotaDTO.class))
                .toList();

        Page<PilotaDTO> pilotaDTOPage = new PageImpl<>(pilotaDTOList, pageRequest, piloti.getTotalElements());
        return new ResponseEntity<>(pilotaDTOPage, HttpStatus.OK);
    }



    @Operation(summary = "Get Pilota by ID")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{id}")
    public ResponseEntity<PilotaDTO> getPilotaById(@PathVariable UUID id) {

        try {
            Pilota pilota = this.pilotaService.findById(id);
            PilotaDTO pilotaDTO = modelMapper.map(pilota, PilotaDTO.class);
            return new ResponseEntity<>(pilotaDTO, HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @Operation(summary = "Post Pilota")
    @ApiResponse(responseCode = "201", description = "Pilota creato correttamente")
    @PostMapping("/")
    public ResponseEntity<PilotaDTO> createPilota( @Validated @RequestBody CreateDTO createdDTO) {

        Pilota pilotaToCreate = modelMapper.map(createdDTO, Pilota.class);
        pilotaToCreate.setId(UUID.randomUUID());

        Scuderia scuderia = scuderiaServiceImpl.findById(createdDTO.getIdScuderia());
        pilotaToCreate.setScuderiaId(scuderia);

        Pilota createdPilota = this.pilotaService.save(pilotaToCreate);
        PilotaDTO creatdPilotaDTO = modelMapper.map(createdPilota, PilotaDTO.class);
        return new ResponseEntity<>(creatdPilotaDTO, HttpStatus.CREATED);
    }



    @Operation(summary = "Update Pilota. Modifiche consentite ai campi: Nazionalita, Scuderia e modelloMoto.")
    @PutMapping("/{id}")
    public ResponseEntity<PilotaDTO> updatePilota(@PathVariable UUID id,
                                               @Validated @RequestBody (required = false) UpdateDTO updatedDto) {

            updatedDto.setId(id);
            Pilota pilotaToUpdate = modelMapper.map(updatedDto, Pilota.class);
        try {
            Pilota pilotaToSave = pilotaService.update(pilotaToUpdate);
            PilotaDTO updatedPilotaDTO = modelMapper.map(pilotaToSave, PilotaDTO.class);
            return ResponseEntity.ok(updatedPilotaDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Delete Pilota")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        try{
            pilotaService.delete(id);
            return ResponseEntity.ok("Pilota eliminato");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pilota non trovato");
        }
    }

}
