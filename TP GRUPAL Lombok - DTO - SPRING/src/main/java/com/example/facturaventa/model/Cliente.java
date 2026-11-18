package com.example.facturaventa.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="cliente") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper=true, onlyExplicitlyIncluded=true)
public class Cliente extends AuditoriaApp {
    @Column(nullable=false) private String denominacion;
    @Column(unique=true) private String documento;

}
