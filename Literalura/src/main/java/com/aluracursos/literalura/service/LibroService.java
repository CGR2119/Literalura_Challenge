package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Idiomas;
import com.aluracursos.literalura.model.Libros;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepositorio;

    public Optional<Libros> buscarLibroPorTitulo(String titulo) {
        return libroRepositorio.findByTituloIgnoreCase(titulo);
    }

    public Libros guardarLibro(Libros libro) {
        return libroRepositorio.save(libro);
    }

    public List<Libros> listaLibrosRegistrados() {
        return libroRepositorio.findAll();
    }

    public List<Libros> buscarLibrosPorAutorId(Long id) {
        return libroRepositorio.buscarLibrosPorAutorId(id);
    }

    public List<Libros> buscarLibroPorIdiomas(Idiomas nombreIdioma) {
        return libroRepositorio.findByIdiomas(nombreIdioma);
    }
}
