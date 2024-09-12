package com.example.crud.dto;

import java.util.UUID;

public class ScuderiaDTO {

    private UUID id;
    private String nomeScuderia;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeScuderia() {
        return nomeScuderia;
    }

    public void setNomeScuderia(String nomeScuderia) {
        this.nomeScuderia = nomeScuderia;
    }
}
