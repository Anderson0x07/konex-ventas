package com.drogueria.ventas.dto;

import java.time.LocalDateTime;

public class VentaDto {

    private int id;
    private LocalDateTime fecha_venta;
    private int cantidad;
    private double valor_unitario;
    private double valor_total;

    private MedicamentoDto medicamento;
}
