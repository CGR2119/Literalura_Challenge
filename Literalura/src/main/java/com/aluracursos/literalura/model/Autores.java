package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autores {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libros;

    //Constructor vacío
    public Autores() {}

    //Constructor con atributos
    public Autores(DatosAutores datosAutores) {
        this.nombre = datosAutores.nombre();
        this.fechaDeNacimiento = datosAutores.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutores.fechaDeFallecimiento();
    }

    //Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaNacimiento) {
        this.fechaDeNacimiento = fechaNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libros> getLibros() {
        libros = new ArrayList<>();
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }

    //toString
    @Override
    public String toString() {
        return "----- Autores -----" + '\n' +
                "\nNombre: " + nombre + '\n' +
                "Fecha de Nacimiento: " + fechaDeNacimiento + '\n' +
                "Fecha de Fallecido: " + fechaDeFallecimiento  + '\n'+
                "-------------------" + '\n';
    }

}