package com.drogueria.ventas.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MedicamentoDto {
    private Integer id;
    private String nombre;
    private String lab_fabrica;
    private Date fecha_fabricacion;
    private Date fecha_vencimiento;
    private int stock;
    private double valor_unitario;
}
