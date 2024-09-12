package com.example.crud.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scuderia")
@Schema(title = "scuderia Model")
public class Scuderia {

    @Id
    @Column(name = "id_scuderia")
    private UUID id;

    @Column(name = "nome")
    private String scuderiaName;

    @OneToMany(mappedBy = "scuderiaId")
    private List<Pilota> piloti;



    public String getScuderiaName() {
        return scuderiaName;
    }

    public void setScuderiaName(String nome) {
        this.scuderiaName = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Pilota> getPiloti() {
        return piloti;
    }

    public void setPiloti(List<Pilota> piloti) {
        this.piloti = piloti;
    }
}
