package com.example.thessvivlio;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(BookEntity book);

    @Delete
    void delete(BookEntity book);

    @Update
    void updateBook(BookEntity book);

    @Query("SELECT * FROM books WHERE borrowed= 0")
    LiveData<List<BookEntity>> getAllBookedBooks();

    @Query("SELECT * FROM books WHERE borrowed= 1")
    LiveData<List<BookEntity>> getAllBorrowedBooks();

    @Query("SELECT * FROM books WHERE englishTitle = :englishTitle AND user = :user")
    LiveData<BookEntity> getBookByEnglishTitle(String englishTitle, String user);

}
