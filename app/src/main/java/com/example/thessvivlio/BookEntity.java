package com.example.thessvivlio;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookEntity {
    @PrimaryKey(autoGenerate = true)
    int id;

    private String greekTitle;
    private String englishTitle;
    private String author;
    private String isbn;
    private String cover;
    private boolean borrowed = false;
    private int returnDate;

    private String trueRetDate;
    private String user;

    public BookEntity() {}
    public BookEntity(int id) {
        this.id = id;
    }

    public BookEntity(int id, String englishTitle, String cover) {
        this.id = id;
        this.englishTitle = englishTitle;
        this.cover = cover;
    }

    public BookEntity(int id, String greekTitle, String englishTitle, String author, String isbn, String cover) {
        this.id = id;
        this.greekTitle = greekTitle;
        this.englishTitle = englishTitle;
        this.author = author;
        this.isbn = isbn;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public int getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(int returnDate) {
        this.returnDate = returnDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTrueRetDate() {
        return trueRetDate;
    }

    public void setTrueRetDate(String trueRetDate) {
        this.trueRetDate = trueRetDate;
    }
}
