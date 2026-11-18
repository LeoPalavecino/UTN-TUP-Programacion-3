package com.example.facturaventa.repository;
import com.example.facturaventa.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
