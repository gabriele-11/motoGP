package com.example.crud.repository;

import com.example.crud.model.Scuderia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScuderiaRepository extends JpaRepository<Scuderia, UUID> {
}
