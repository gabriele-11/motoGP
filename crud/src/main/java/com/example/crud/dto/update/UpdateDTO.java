package com.example.crud.dto.update;

import jakarta.annotation.Nullable;

import java.util.UUID;

@Nullable
public class UpdateDTO {

    private UUID id;
    private String nazionalita;
    private String scuderia;
    private String modelloMoto;


    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public String getScuderia() {
        return scuderia;
    }

    public void setScuderia(String scuderia) {
        this.scuderia = scuderia;
    }

    public String getModelloMoto() {
        return modelloMoto;
    }

    public void setModelloMoto(String modelloMoto) {
        this.modelloMoto = modelloMoto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
