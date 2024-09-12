package com.example.crud.repository;

import com.example.crud.model.Pilota;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface PilotaRepository extends JpaRepository<Pilota, UUID> {
}
