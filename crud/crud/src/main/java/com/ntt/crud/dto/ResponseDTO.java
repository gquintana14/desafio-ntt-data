package com.ntt.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private Long id;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Date ultimoIngreso;
    private String token;
    private boolean isActive;
}
