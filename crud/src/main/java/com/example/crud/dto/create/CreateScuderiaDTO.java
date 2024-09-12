package com.example.crud.dto.create;

import jakarta.validation.constraints.NotNull;

public class CreateScuderiaDTO {

    @NotNull
    private String nomeScuderia;

    public String getNomeScuderia() {
        return nomeScuderia;
    }

    public void setNomeScuderia(String nome) {
        this.nomeScuderia = nome;
    }
}
