package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autores;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepositorio;

    public Optional<Autores> buscarAutorRegistrado(String nombre) {
        return autorRepositorio.findByNombreIgnoreCase(nombre);
    }

    public Autores guardarAutor(Autores autor) {
        return autorRepositorio.save(autor);
    }

    public List<Autores> listaAutoresRegistrados() {
        return autorRepositorio.findAll();
    }

    public List<Autores> buscarAutoresVivosPorAnio(int autorAnio) {
        return autorRepositorio.buscarAutoresVivosPorAnio(autorAnio);
    }

}