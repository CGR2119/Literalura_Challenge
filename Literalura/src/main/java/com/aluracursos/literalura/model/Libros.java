package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libros {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idiomas idiomas;
    private Double numeroDeDescargas;
    @ManyToOne
    private Autores autor;


    public Libros() {}


    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = new Autores(datosLibros.autores().get(0));
        this.idiomas = Idiomas.fromString(datosLibros.idiomas().get(0));
        this.numeroDeDescargas = OptionalDouble.of(datosLibros.numeroDescargas()).orElse(0);
    }

    //Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public Idiomas getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idiomas idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    //toString
    @Override
    public String toString() {
        return "----- LIBRO -----" + '\n' +
                "Título: " + titulo + '\n' +
                "Autores: " + autor.getNombre() + '\n' +
                "Idiomas: " + idiomas + '\n' +
                "Número de Descargas: " + numeroDeDescargas + "\n" +
                "-----------------" + '\n';
    }

}