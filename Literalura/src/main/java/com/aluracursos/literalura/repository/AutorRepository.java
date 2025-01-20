package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autores, Long> {

    //Buscar el autor por nombre
    Optional<Autores> findByNombreIgnoreCase(String nombre);

    //Busca los autores vivos por un determinado año
    @Query("SELECT a FROM Autores a WHERE a.fechaDeNacimiento < :anioAutor AND a.fechaDeFallecimiento > :anioAutor")
    List<Autores> buscarAutoresVivosPorAnio(int anioAutor);


}