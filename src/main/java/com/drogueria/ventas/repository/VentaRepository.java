package com.drogueria.ventas.repository;

import com.drogueria.ventas.entity.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VentaRepository extends MongoRepository<Venta, String> {

}
