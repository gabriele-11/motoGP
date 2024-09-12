package com.example.crud.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "pilota")
@Schema(description = "Pilota Model")
public class Pilota {

    @Id
    @Column(name = "id")
    @Schema(description = "ID univoco del Pilota")
    private UUID id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "nazionalità")
    @Schema(description = "nome della nazione", example = "Italia,Spagna...")
    private String nazionalita;

    @Column(name = "dt_nascita")
    @Schema(description = "nel formato yyyy-mm-dd", example = "1980-03-20")
    private Date dataNascita;

    @Column(name = "scuderia")
    private String nomeScuderia;

    @Column(name = "modello_moto")
    private String modelloMoto;


    @ManyToOne
    @JoinColumn(name = "id_scuderia", referencedColumnName = "id_scuderia")
    private Scuderia scuderiaId;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getModelloMoto() {
        return modelloMoto;
    }

    public void setModelloMoto(String modelloMoto) {
        this.modelloMoto = modelloMoto;
    }

    public String getNomeScuderia() {
        return nomeScuderia;
    }

    public void setNomeScuderia(String nomeScuderia) {
        this.nomeScuderia = nomeScuderia;
    }

    public Scuderia getScuderiaId() {
        return scuderiaId;
    }

    public void setScuderiaId(Scuderia scuderiaId) {
        this.scuderiaId = scuderiaId;
    }
}
