package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.service.AutorService;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LibroService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    public static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroService libroServicio;
    private AutorService autorServicio;

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroServicio = libroService;
        this.autorServicio = autorService;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {

            try {
                String menu = """
                --------------
                **********Catálogo de libros en Literalura**********
                1.- Buscar libro por título
                2.- Libros registrados
                3.- Autores registrados
                4.- Autores vivos en un año determinado
                5.- Libros por idioma
                6.- Estadísticas de libros por número de descargas
                0.- Salir
                --------------
                
                Elija la opción a través de su número:""";

                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {

                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listaLibrosRegistrados();
                        break;
                    case 3:
                        listaAutoresRegistrados();
                        break;
                    case 4:
                        buscarAutoresVivosPorAnio();
                        break;
                    case 5:
                        listaLibrosPorIdioma();
                        break;
                    case 6:
                        estadisticasLibrosPorNumDescargas();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida. Favor de introducir un número del menú.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Opción inválida. Favor de introducir un número del menú.");
                teclado.nextLine();
            }
        }
    }


    private DatosResultados obtenerDatosResultados(String tituloLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ", "%20"));
        var datos = conversor.obtenerDatos(json, DatosResultados.class);
        return datos;
    }

    //Metodo para buscar información de un libro por el título
    private void buscarLibroPorTitulo() {

        System.out.print("¿Qué libro deseas buscar?");
        var tituloLibro = teclado.nextLine().toUpperCase();

        Optional<Libros> libroRegistrado = libroServicio.buscarLibroPorTitulo(tituloLibro);

        if (libroRegistrado.isPresent()) {
            System.out.println("El libro que buscas ya está registrado.");
        } else {
            var datos = obtenerDatosResultados(tituloLibro);
            if (datos.listaLibros().isEmpty()){
                System.out.println("No se encontró el libro que buscas en Gutendex API.");
            } else {
                DatosLibros datosLibros = datos.listaLibros().get(0);
                DatosAutores datosAutores = datosLibros.autores().get(0);
                String idioma = datosLibros.idiomas().get(0);
                Idiomas idiomas = Idiomas.fromString(idioma);

                Libros libro = new Libros(datosLibros);
                libro.setIdiomas(idiomas);

                Optional<Autores> autorRegistrado = autorServicio.buscarAutorRegistrado(datosAutores.nombre());


                if (autorRegistrado.isPresent()) {
                    System.out.println("El autor ya está registrado.");
                    Autores autorExiste = autorRegistrado.get();
                    libro.setAutor(autorExiste);
                } else {

                    Autores autor = new Autores(datosAutores);
                    autor = autorServicio.guardarAutor(autor);
                    libro.setAutor(autor);
                    autor.getLibros().add(libro);
                }

                try {

                    libroServicio.guardarLibro(libro);
                    System.out.println("\nLibro encontrado.\n");
                    System.out.println(libro+"\n");
                    System.out.println("Libro guardado.\n");
                } catch (DataIntegrityViolationException e){
                    System.out.println("El libro ya está registrado.");
                }
            }
        }

    }

    //Metodo para listar los libros registrados
    private void listaLibrosRegistrados() {

        List<Libros> libros = libroServicio.listaLibrosRegistrados();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados.");
        } else {
            System.out.println("Los libros registrados son los siguientes:\n");

            libros.stream()
                    .sorted(Comparator.comparing(Libros::getTitulo))
                    .forEach(System.out::println);
        }

    }

    //Metodo para listar los autores con sus libros registrados
    private void listaAutoresRegistrados() {

        //Obtiene en la base de datos todos los autores registrados
        List<Autores> autores = autorServicio.listaAutoresRegistrados();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
        } else {

            System.out.println("Los autores registrados son los siguientes:\n");
            for (Autores autor : autores) {

                List<Libros> librosPorAutorId = libroServicio.buscarLibrosPorAutorId(autor.getId());

                System.out.println("----- AUTOR -----");
                System.out.println("Autor: "+autor.getNombre());
                System.out.println("Fecha de Nacimiento: "+autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecido: "+autor.getFechaDeFallecimiento());

                if (librosPorAutorId.isEmpty()) {
                    System.out.println("No se encontraron libros registrados para este autor.");
                } else {

                    String librosRegistrados = librosPorAutorId.stream()
                            .map(Libros::getTitulo)
                            .collect(Collectors.joining(", "));
                    System.out.println("Libros: ["+librosRegistrados+"]");
                    System.out.println("-----------------\n");
                }
            }
        }

    }

    //Metodo para buscar los autores vivos por el año determinado
    private void buscarAutoresVivosPorAnio() {

        System.out.print("Escribe el año vivo del autor(es) que desea buscar: ");
        var anioDelAutor = teclado.nextInt();

        List<Autores> buscarAutoresPorAnio = autorServicio.buscarAutoresVivosPorAnio(anioDelAutor);

        if (buscarAutoresPorAnio.isEmpty()) {
            System.out.println("No se encontraron autores vivos por el año buscado.");
        } else {
            System.out.printf("El autor o los autores vivos del año %d son los siguientes:\n", anioDelAutor);
            for (Autores autoresVivos : buscarAutoresPorAnio) {
                List<Libros> librosAutoresVivosPorId = libroServicio.buscarLibrosPorAutorId(autoresVivos.getId());

                System.out.println("----- AUTOR -----");
                System.out.println("Autor: " + autoresVivos.getNombre());
                System.out.println("Fecha de Nacimiento: " + autoresVivos.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecido: " + autoresVivos.getFechaDeFallecimiento());

                if (librosAutoresVivosPorId.isEmpty()) {
                    System.out.println("No se encontraron libros registrados para este autor.");
                } else {
                    String librosRegistrados = librosAutoresVivosPorId.stream()
                            .map(Libros::getTitulo)
                            .collect(Collectors.joining(", "));
                    System.out.println("Libros: [" + librosRegistrados + "]");
                    System.out.println("-----------------\n");
                }
            }
        }

    }

    //Metodo para listar libros por idioma
    private void listaLibrosPorIdioma() {

        System.out.println("""
                Estos son los idiomas disponibles:
                - es -> Español
                - en -> Inglés
                - fr -> Francés
                - pt -> Portugués
                """);

        System.out.print("Escribe el idioma abreviado para buscar los libros: ");
        var nombreIdioma = teclado.nextLine();


        try {
            List<Libros> buscarLibrosPorIdioma = libroServicio.buscarLibroPorIdiomas(Idiomas.fromString(nombreIdioma));

            if (buscarLibrosPorIdioma.isEmpty()) {
                System.out.println("No se encontraron los libros del idioma buscado.");
            } else {
                System.out.printf("Los libros del idioma '%s' son los siguientes:\n", nombreIdioma);
                buscarLibrosPorIdioma.forEach(l -> System.out.print(l.toString()));
            }
        } catch (Exception e) {
            System.out.println("Opción inválida. Favor de escribir un idioma abreviado del menú.");
        }

    }

    //Metodo para listar estadísticas de los libros por número de descargas
    private void estadisticasLibrosPorNumDescargas() {

        List<Libros> todosLosLibros = libroServicio.listaLibrosRegistrados();

        if (todosLosLibros.isEmpty()){

            System.out.println("No se encontraron libros registrados.");
        } else {
            System.out.println("Estadísticas de los libros por número de descargas:\n");
            DoubleSummaryStatistics est = todosLosLibros.stream()
                    .filter(libro -> libro.getNumeroDeDescargas() > 0)
                    .collect(Collectors.summarizingDouble(Libros::getNumeroDeDescargas));
            System.out.println("Cantidad media de descargas: " + est.getAverage());
            System.out.println("Cantidad máxima de descargas: "+ est.getMax());
            System.out.println("Cantidad mínima de descargas: " + est.getMin());
        }

    }



}