package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor =  new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;


    public  Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository=libroRepository;
        this.autorRepository=autorRepository;
    }
    public void mostrarMenu() {
        int opcion = -1;
        System.out.println("¡Bienvenido/a a Literalura!");
        String menu = """
                ¿Que desea realizar?
                                
                1 - Buscar libro por nombre
                2 - Mostrar todos los libros registrados
                3 - Mostrar todos los autores registrados
                4 - Mostrar los autores vivos en un determinado año
                5 - Listar todos los libros por idioma
                0 - Salir

                """;


        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida\n");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.printf("Opción inválida. Por favor, ingrese un número del 0 al 5.\n");
            }

        }
    }

    public String getDatosLibro(){
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = scanner.nextLine();
        var response = consumoAPI.obtenerDatos(URL_BASE+"?search=%20"+nombreLibro.replace(" ", "%20"));
        return response;
    }


    private void buscarLibroPorTitulo() {
        String response = getDatosLibro();
        DatoApi datoApi = conversor.obtenerDatos(response, DatoApi.class);
        if(!datoApi.resultado().isEmpty() && datoApi != null ){
            DatoLibro libroEncontrado = datoApi.resultado().get(0);

            Libro libro = new Libro(libroEncontrado);
            Optional<Libro> libroBD = libroRepository.findByTituloContainsIgnoreCase(libro.getTitulo());
            if(libroBD.isPresent()){
                System.out.println("El libro ya esta registrado");
            }else{
                if(!libroEncontrado.autores().isEmpty()){
                    DatoAutor autorEncontrado = libroEncontrado.autores().get(0);
                    Autor autor = new Autor(autorEncontrado);
                    Optional<Autor> autorBD = autorRepository.findByNombre(autor.getNombre());

                    if(autorBD.isPresent()){
                        libro.setAutor(autorBD.get());
                        libroRepository.save(libro);
                    }else{
                        autorRepository.save(autor);
                        libro.setAutor(autor);
                        libroRepository.save(libro);
                    }
                }else{
                    System.out.println("El libro no tiene autor");
                }
            }
            System.out.println(libro);
        }else{
            System.out.println("No se encontro el libro");
        }

    }
    private void mostrarLibrosRegistrados() {
        System.out.println("Libros registrados: \n");
        libros=libroRepository.findAll();
        libros.stream().forEach(System.out::println);
    }


    private void mostrarAutoresRegistrados() {
        System.out.println("Autores registrados: \n");
        autores=autorRepository.findAll();
        autores.stream().forEach(System.out::println);
    }


    private void mostrarAutoresVivosEnAnio() {
        System.out.println("Elige el año del cual quieres ver los autores vivos: \n");
        try{
            var anio = scanner.nextInt();
            scanner.nextLine();
            autores=autorRepository.findAutoresVivosAnio(anio);
            if(autores.isEmpty()){
                System.out.println("No se encontraron autores vivos en ese año");
            }else{
                System.out.println("Autores vivos en ese año: \n");
                autores.stream().forEach(System.out::println);
            }
        }catch (InputMismatchException e){
            System.out.println("El año ingresado no es válido");
            scanner.nextLine();
        }

    }


    private void listarLibrosPorIdioma() {
        System.out.println("""
                Idiomas posibles:
                - en -> Inglés 
                - es -> Español
                - pt -> Portugues
                - fr -> Frances
                Elija el idioma por el cual quieres filtrar los libros: """);
        var idioma = scanner.nextLine();

        Idioma idiomaElegido=null;

        switch (idioma){
            case "en":
                idiomaElegido = Idioma.fromEspanol("Ingles");
                break;
            case "es":
                idiomaElegido = Idioma.fromEspanol("Espanol");
                break;
            case "pt":
                idiomaElegido = Idioma.fromEspanol("Portugues");
                break;
            case "fr":
                idiomaElegido = Idioma.fromEspanol("Frances");
                break;
            default:
                System.out.println("El idioma elegido no se encuentra en nuestro sistema.");
        }
        if(idiomaElegido!=null){
            libros = libroRepository.findLibroByIdioma(idiomaElegido);
            if(libros.isEmpty()){
                System.out.println("No hay libros en ese idioma");
            }else{
                System.out.println("Libros encontrados: ");
                libros.stream().forEach(System.out::println);
            }
        }

    }


}
