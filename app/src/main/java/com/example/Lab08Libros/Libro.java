package com.example.Lab08Libros;

public class Libro {
    String title;
    String authors;
    String publisher;
    String publishedDate;
    String thumbnail;
    String textSnippet;
    String saleability;
    String infoLink;

    public Libro(String title, String authors, String publisher, String publishedDate, String thumbnail, String textSnippet, String saleability, String infoLink) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.thumbnail = thumbnail;
        this.textSnippet = textSnippet;
        this.saleability = saleability;
        this.infoLink = infoLink;
    }
}
