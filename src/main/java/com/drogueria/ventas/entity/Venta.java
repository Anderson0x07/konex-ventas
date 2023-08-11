package com.drogueria.ventas.entity;

import com.drogueria.ventas.dto.MedicamentoDto;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

/*@Entity
@Table(name = "venta")*/
@Document(collection = "ventas")
@Data
public class Venta {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime fecha_venta;
    private int cantidad;
    private double valor_unitario;
    private double valor_total;

    /*ManyToOne
    @JoinColumn(name = "id_medicamento")*/
    @Transient
    private MedicamentoDto medicamento;

}
