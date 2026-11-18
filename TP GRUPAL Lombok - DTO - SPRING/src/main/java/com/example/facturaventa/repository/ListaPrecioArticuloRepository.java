package com.example.facturaventa.repository;
import com.example.facturaventa.model.ListaPrecioArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface ListaPrecioArticuloRepository extends JpaRepository<ListaPrecioArticulo,Long> {
    Optional<ListaPrecioArticulo> findFirstByArticuloIdAndActivoTrueOrderByIdDesc(Long articuloId);
    List<ListaPrecioArticulo> findAllByActivoTrueOrderByIdDesc();
}
