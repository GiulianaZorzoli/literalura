package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer descargas;

    public Libro(){}
    public Libro(DatoLibro datoLibro){
        this.titulo=datoLibro.titulo();
        if(datoLibro.autores()!=null){
            this.autor= new Autor(datoLibro.autores().get(0));
        }else{
            this.autor=null;
        }
        this.idioma=Idioma.fromString(datoLibro.idiomas().toString().split(",")[0]);
        this.descargas=datoLibro.descargas();
    }

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

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return
                "**********LIBRO:********** \n" +
                        "titulo='" + titulo + "'\n" +
                        "autor=" + (autor != null ? autor.getNombre() : "") + "\n" +
                        "idioma=" + idioma + "\n" +
                        "descargas=" + descargas +
                        "\n************************** \n";
    }
}
