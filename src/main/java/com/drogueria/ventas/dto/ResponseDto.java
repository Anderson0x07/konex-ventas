package com.drogueria.ventas.dto;

import com.drogueria.ventas.entity.Venta;
import lombok.Data;

@Data
public class ResponseDto {
    private Venta venta;
    private MedicamentoDto medicamentoDto;
}
