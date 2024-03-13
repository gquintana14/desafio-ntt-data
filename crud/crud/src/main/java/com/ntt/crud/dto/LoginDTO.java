package com.ntt.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String nombre;
    private String correo;
    private String token;
    private Date ultimoIngreso;

}
