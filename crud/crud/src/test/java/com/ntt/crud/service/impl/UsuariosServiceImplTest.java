package com.ntt.crud.service.impl;

import com.ntt.crud.jwt.JwtService;
import com.ntt.crud.model.Phones;
import com.ntt.crud.model.Usuarios;
import com.ntt.crud.repository.PhonesRepository;
import com.ntt.crud.repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuariosServiceImplTest {

    @InjectMocks
    private UsuariosServiceImpl usuariosService;

    @Mock
    private Usuarios usuarios;

    @Mock
    private Phones phones;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuariosRepository usuariosRepository;

    @Mock
    private PhonesRepository phonesRepository;

    @Mock
    private JwtService jwtService;

    @Test
    void loadUserByUser() {
        Usuarios user = new Usuarios();
        user.setUsername("jperez@ntt.com");
        user.setPassword("Password24");
        Mockito.when(jwtService.getToken(ArgumentMatchers.any())).thenReturn("");
        Mockito.when(usuariosRepository.getByUsername(ArgumentMatchers.anyString())).thenReturn(usuarios);
        usuariosService.loadUserByUser(user);
    }

    @Test
    void createNewUser() {
        List<Phones> phonesList = new ArrayList<>();
        Usuarios user = new Usuarios();
            user.setUsername("jperez@ntt.com");
            user.setCorreo("jperez@ntt.com");
            user.setPassword("Password24");
            Phones cel = new Phones();
                cel.setNumber("12312432");
                cel.setCityCode("1");
                cel.setCountryCode("569");
            phonesList.add(phones);
            user.setPhones(phonesList);
        usuariosService.createNewUser(user);
    }

    @Test
    void modifyUser() {
        List<Phones> phonesList = new ArrayList<>();
        Usuarios user = new Usuarios();
        user.setUsername("jperez@ntt.com");
        user.setCorreo("jperez@ntt.com");
        user.setPassword("Password24");
        Phones cel = new Phones();
        cel.setNumber("123123432");
        cel.setCityCode("1");
        cel.setCountryCode("569");
        phonesList.add(phones);
        user.setPhones(phonesList);
        Optional<Usuarios> userOp = Optional.ofNullable(Mockito.mock(Usuarios.class));
        Mockito.when(usuariosRepository.getByCorreo(ArgumentMatchers.anyString())).thenReturn(userOp);
        Mockito.when(usuariosRepository.getUserByCorreo(ArgumentMatchers.anyString())).thenReturn(user);
        usuariosService.modifyUser(user);
    }

    @Test
    void getAllUsers() {
        usuariosService.getAllUsers();
    }

    @Test
    void deleteUser() {
        Optional<Usuarios> userOp = Optional.ofNullable(Mockito.mock(Usuarios.class));
        Mockito.when(usuariosRepository.findById(ArgumentMatchers.anyLong())).thenReturn(userOp);
        usuariosService.deleteUser(1L);
    }
}