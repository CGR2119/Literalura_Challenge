package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Idiomas;
import com.aluracursos.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros, Long> {
    //Busca el libro por el nombre
    Optional<Libros> findByTituloIgnoreCase(String titulo);

    //Busca los libros por el id del autor
    @Query("SELECT l FROM Libros l WHERE l.autor.id = :autorId")
    List<Libros> buscarLibrosPorAutorId(Long autorId);

    //Busca los libros por idioma
    List<Libros> findByIdiomas(Idiomas nombreIdioma);

}