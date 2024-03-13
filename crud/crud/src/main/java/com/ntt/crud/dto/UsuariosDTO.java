package com.ntt.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosDTO {
    private Long id;
    private String nombre;
    private String correo;
    private List<PhonesDTO> phones;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Date ultimoIngreso;
    private String token;
    private boolean isActive;
}
