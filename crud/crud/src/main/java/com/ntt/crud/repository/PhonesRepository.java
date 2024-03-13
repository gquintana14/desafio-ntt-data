package com.ntt.crud.repository;

import com.ntt.crud.model.Phones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhonesRepository extends JpaRepository<Phones, Integer> {

    @Modifying
    @Query(value = "delete from PHONES where ID_USUARIO = ?1", nativeQuery = true)
    void deletePhones(@Param("idUsuarios") Long idUsuarios);

}
