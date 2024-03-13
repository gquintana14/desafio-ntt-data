package com.ntt.crud.repository;

import com.ntt.crud.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByUsername(@Param("username") String username);

    Optional<Usuarios> findById(@Param("id") Long id);

    @Query("SELECT u FROM Usuarios u WHERE u.username = :username")
    Usuarios getByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuarios u WHERE u.correo = :correo")
    Usuarios getUserByCorreo(@Param("correo") String correo);

    @Query("SELECT u FROM Usuarios u WHERE u.correo = :correo")
    Optional<Usuarios> getByCorreo(@Param("correo") String correo);

    @Query("SELECT u.id FROM Usuarios u WHERE u.username = :username")
    Long getIdByUsername(@Param("username") String username);

    void deleteById(@Param("id") Long id);
}
