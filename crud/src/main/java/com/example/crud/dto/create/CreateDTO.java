package com.example.crud.dto.create;

import java.sql.Date;
import java.util.UUID;

public class CreateDTO {

    private String nome;

    private String cognome;

    private String nazionalita;

    private Date dataNascita;

    private String modelloMoto;

    private UUID idScuderia;


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

    public UUID getIdScuderia() {
        return idScuderia;
    }

    public void setIdScuderia(UUID idScuderia) {
        this.idScuderia = idScuderia;
    }
}