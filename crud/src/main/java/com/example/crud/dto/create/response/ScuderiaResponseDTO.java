package com.example.crud.dto.create.response;

import com.example.crud.dto.PilotaDTO;

import java.util.List;

public class ScuderiaResponseDTO {



    private String nomeScuderia;

    private List<PilotaDTO> piloti;


    public String getNomeScuderia() {
        return nomeScuderia;
    }

    public void setNomeScuderia(String nomeScuderia) {
        this.nomeScuderia = nomeScuderia;
    }



    public List<PilotaDTO> getPiloti() {
        return piloti;
    }

    public void setPiloti(List<PilotaDTO> piloti) {
        this.piloti = piloti;
    }
}
