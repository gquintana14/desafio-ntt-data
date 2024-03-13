package com.ntt.crud.service;

import com.ntt.crud.dto.LoginDTO;
import com.ntt.crud.dto.ResponseDTO;
import com.ntt.crud.dto.UsuariosDTO;
import com.ntt.crud.model.Usuarios;

import java.util.List;

public interface UsuariosService {
    LoginDTO loadUserByUser(Usuarios user);
    ResponseDTO createNewUser(Usuarios user);
    ResponseDTO modifyUser(Usuarios user);
    void deleteUser(Long id);
    List<UsuariosDTO> getAllUsers();
}
