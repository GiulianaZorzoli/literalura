package com.alura.literalura.model;

public enum Idioma {
    ESPANOL("[es]", "Espanol"),
    INGLES("[en]", "Ingles"),
    PORTUGUES("[pt]", "Portugues"),
    FRANCES("[fr]", "Frances");

    private String idiomaGutendex ;
    private String idiomaEspanol;

    Idioma(String idiomaGutendex, String idiomaEspanol){
        this.idiomaGutendex=idiomaGutendex;
        this.idiomaEspanol=idiomaEspanol;
    }

    public  static  Idioma fromString (String text){
        for (Idioma idioma : Idioma.values()){
            if(idioma.idiomaGutendex.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw  new IllegalArgumentException("Ninguna Categoria Encontrada: "+ text);
    }

    public static  Idioma fromEspanol (String text){
        for(Idioma idioma : Idioma.values()){
            if(idioma.idiomaEspanol.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw  new IllegalArgumentException("Ninguna Categoria Encontrada: "+ text);
    }
}
