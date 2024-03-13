package com.ntt.crud.service.impl;

import com.ntt.crud.dto.LoginDTO;
import com.ntt.crud.dto.PhonesDTO;
import com.ntt.crud.dto.ResponseDTO;
import com.ntt.crud.dto.UsuariosDTO;
import com.ntt.crud.exception.CrudException;
import com.ntt.crud.jwt.JwtService;
import com.ntt.crud.model.Phones;
import com.ntt.crud.model.Usuarios;
import com.ntt.crud.repository.PhonesRepository;
import com.ntt.crud.repository.UsuariosRepository;
import com.ntt.crud.service.PhonesService;
import com.ntt.crud.service.UsuariosService;
import com.ntt.crud.utils.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuariosServiceImpl implements UsuariosService {
    private static final Logger logger = LoggerFactory.getLogger(UsuariosServiceImpl.class);

    @Autowired
    private UsuariosRepository userRepository;

    @Autowired
    private PhonesRepository phonesRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public LoginDTO loadUserByUser(Usuarios user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            Usuarios userModel = userRepository.getByUsername(user.getUsername());
            userModel.setUltimoIngreso(getDate());
            userRepository.save(userModel);
            LoginDTO loginDTO = modelToDtoUser(userModel);
            String token = jwtService.getToken(userModel);
            loginDTO.setToken(token);
            return loginDTO;
        } catch (ExpiredJwtException e) {
            logger.error(Constants.TOKEN_EXPIRED_MSG);
            throw new CrudException(Constants.TOKEN_EXPIRED_MSG);
        } catch (AuthenticationException e) {
            logger.error(Constants.BAD_CREDENTIALS_MSG);
            throw new CrudException(e.getMessage());
        }

    }

    @Override
    public ResponseDTO createNewUser(Usuarios user) {
        try{
            verifyMail(user.getCorreo());
            checkUser(user.getCorreo());
            verifyPassword(user.getPassword());
            userRepository.save(getUsuarios(user));
            savePhones(user);

            return userToResponseDTO(user);
        }catch (AuthenticationException e) {
            logger.error(Constants.BAD_CREDENTIALS_MSG);
            throw new CrudException(e.getMessage());
        }
    }

    @Transactional
    public ResponseDTO modifyUser(Usuarios user) {
        try{
            validateUser(user.getCorreo());
            verifyPassword(user.getPassword());
            Usuarios userModel = userRepository.getUserByCorreo(user.getCorreo());
            userRepository.save(getModifyUsuarios(userModel));

            return userToResponseDTO(user);
        }catch (AuthenticationException e) {
            logger.error(Constants.BAD_CREDENTIALS_MSG);
            throw new CrudException(e.getMessage());
        }
    }

    @Override
    public List<UsuariosDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user-> modelMapper.map(user,UsuariosDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id){
        if (userRepository.findById(id).isEmpty()){
            logger.error(Constants.USER_NOT_FOUND);
            throw new CrudException(Constants.USER_NOT_FOUND);
        }else {
            phonesRepository.deletePhones(id);
            userRepository.deleteById(id);
        }
    }

    private void savePhones(Usuarios usuarios){
        usuarios.setId(userRepository.getIdByUsername(usuarios.getUsername()));
        usuarios.getPhones().forEach(phones -> {
            phones.setUsuarios(usuarios);
            phonesRepository.save(phones);
        });

    }


    private Usuarios getUsuarios(Usuarios user){
        user.setUsername(user.getCorreo());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setFechaCreacion(getDate());
        user.setPhones(user.getPhones());

        return user;
    }

    private Usuarios getModifyUsuarios(Usuarios user){
        user.setUsername(user.getCorreo());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setFechaActualizacion(getDate());
        user.setPhones(user.getPhones());

        return user;
    }

    private void verifyMail(String correo){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!correo.matches(regex)){
            throw new CrudException(Constants.MAIL_NOT_FOUND);
        }
    }

    private void verifyPassword(String pass){
        String regex = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{5,16}$";
        if (!pass.matches(regex)){
            throw new CrudException(Constants.BAD_FORMAT_MSG);
        }
    }

    private void checkUser(String correo){
        if (userRepository.getByCorreo(correo).isPresent()){
            logger.error(Constants.USER_EXIST_MSG);
            throw new CrudException(Constants.USER_EXIST_MSG);
        }
    }

    private void validateUser(String correo){
        if (userRepository.getByCorreo(correo).isEmpty()){
            logger.error(Constants.USER_NOT_FOUND);
            throw new CrudException(Constants.USER_NOT_FOUND);
        }
    }

    private Date getDate(){
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateFormat = output.format(new Date());
        return new Date(dateFormat);
    }

    private LoginDTO modelToDtoUser(Usuarios modelUser) {
        LoginDTO user = new LoginDTO();
        user.setNombre(modelUser.getNombre());
        user.setCorreo(modelUser.getCorreo());
        user.setUltimoIngreso(getDate());
        return user;
    }

    private ResponseDTO userToResponseDTO(Usuarios usuarios){
        ResponseDTO dto = new ResponseDTO();
        dto.setId(usuarios.getId());
        dto.setFechaCreacion(usuarios.getFechaCreacion());
        dto.setFechaActualizacion(usuarios.getFechaActualizacion());
        dto.setUltimoIngreso(usuarios.getUltimoIngreso());
        dto.setToken(jwtService.getToken(usuarios));
        dto.setActive(true);

        return dto;
    }



}
