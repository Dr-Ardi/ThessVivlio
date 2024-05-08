package com.example.thessvivlio;

public class Book {
    private String greekTitle;
    private String englishTitle;
    private String author;
    private String genre;
    private int publicationYear;
    private String isbn;
    private String cover;
    private boolean isBestseller;
    private int availableCopies;

    public Book() {}
    public Book(String greekTitle, String englishTitle, String author, String genre, int publicationYear, String isbn, String coverUrl, boolean isBestseller, int availableCopies) {
        this.greekTitle = greekTitle;
        this.englishTitle = englishTitle;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.cover = cover;
        this.isBestseller = isBestseller;
        this.availableCopies = availableCopies;
    }

    public String getGreekTitle() {
        return greekTitle;
    }

    public void setGreekTitle(String greekTitle) {
        this.greekTitle = greekTitle;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isBestseller() {
        return isBestseller;
    }

    public void setBestseller(boolean bestseller) {
        isBestseller = bestseller;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
