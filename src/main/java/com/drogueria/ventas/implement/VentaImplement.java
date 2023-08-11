package com.drogueria.ventas.implement;

import com.drogueria.ventas.dto.MedicamentoDto;
import com.drogueria.ventas.entity.Venta;
import com.drogueria.ventas.exception.NotFoundException;
import com.drogueria.ventas.repository.VentaRepository;
import com.drogueria.ventas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VentaImplement implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Venta> listarVentas() {

        List<Venta> ventas = ventaRepository.findAll();

        if (ventas.isEmpty()) {
            throw new NotFoundException("No hay ventas registradas");
        }

        for (int i=0; i<ventas.size();i++) {

            MedicamentoDto medicamentoDto = restTemplate.getForObject(
                    "http://medicamentos-service/api/medicamentos/" + ventas.get(i).getMedicamento(),
                    MedicamentoDto.class
            );
            ventas.get(i).setMedicamentoDto(medicamentoDto);
        }

        return ventas;
    }

    @Transactional
    @Override
    public void guardar(Venta venta) {

        MedicamentoDto medicamentoDto = restTemplate.getForObject(
                "http://medicamentos-service/api/medicamentos/" + venta.getMedicamento(),
                MedicamentoDto.class
        );

        System.out.println("ENTRE A GUARDAR");

        if(!hayStock(medicamentoDto.getStock() - venta.getCantidad())){
            throw new NotFoundException("No hay stock suficiente");
        }

        medicamentoDto.setStock(medicamentoDto.getStock() - venta.getCantidad()); // Se realiza la resta de stock cuando se genera la venta

        //venta.setMedicamentoDto(medicamentoDto);
        venta.setValor_unitario(medicamentoDto.getValor_unitario());
        venta.setValor_total(medicamentoDto.getValor_unitario() * venta.getCantidad());

        //Actualizar el medicamento en el inventario
        restTemplate.put(
                "http://medicamentos-service/api/medicamentos/"+venta.getMedicamento(),
                medicamentoDto,
                MedicamentoDto.class
        );

        ventaRepository.save(venta);
    }

    public boolean hayStock(int cantidad) {
        return (cantidad >= 0);
    }

    @Override
    public void eliminar(String id) {
        ventaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Venta no encontrada")
        );
        ventaRepository.deleteById(id);
    }


    @Override
    public Venta encontrarVenta(String id) {


        Venta venta = ventaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Venta no encontrada")
        );;

        MedicamentoDto medicamentoDto = restTemplate.getForObject(
                "http://medicamentos-service/api/medicamentos/" + venta.getMedicamento(),
                MedicamentoDto.class
        );

        venta.setMedicamentoDto(medicamentoDto);

        return venta;
    }
}
