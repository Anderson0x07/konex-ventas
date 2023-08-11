package com.drogueria.ventas.repository;

import com.drogueria.ventas.entity.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VentaRepository extends MongoRepository<Venta, String> {

    List<Venta> findByMedicamento(String medicamento);
}
