package com.drogueria.ventas.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "ventas")
@Data
public class Venta {
    @Id
    private String id;

    private LocalDateTime fecha_venta;
    private int cantidad;
    private String medicamento;
    private double valor_unitario;
    private double valor_total;

}
