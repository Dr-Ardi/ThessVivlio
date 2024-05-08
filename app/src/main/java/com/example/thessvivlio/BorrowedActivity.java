package com.example.thessvivlio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BorrowedActivity extends AppCompatActivity {

    private BookDatabase bookDatabase;

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        Intent borroweds = getIntent();

        if (borroweds != null && borroweds.hasExtra("username")) {

            user = borroweds.getStringExtra("username");
        }

        bookDatabase = BookDatabase.getDatabase(this);

        LinearLayout booked = findViewById(R.id.bookedBooks);
        LinearLayout borrowed = findViewById(R.id.borrowedBooks);


        observeBookedBooks(booked);
        observeBorrowedBooks(borrowed);

    }

    private void observeBookedBooks(final LinearLayout bookedLayout) {
        LiveData<List<BookEntity>> bookedBooksLiveData = bookDatabase.bookDao().getAllBookedBooks();
        bookedBooksLiveData.observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(List<BookEntity> bookedBooks) {
                if (bookedBooks != null && !bookedBooks.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookedLayout.removeAllViews();
                            for (final BookEntity book : bookedBooks) {
                                if(book.getUser() != null && book.getUser().contentEquals(user)){
                                    ImageView imageView = new ImageView(BorrowedActivity.this);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    layoutParams.setMargins(0, 0, 1, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    String coverUrl = book.getCover();
                                    Picasso.get().load(coverUrl).into(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(BorrowedActivity.this, BorrowedBookActivity.class);
                                            intent.putExtra("title", book.getEnglishTitle());
                                            intent.putExtra("username", user);
                                            startActivity(intent);
                                        }
                                    });
                                    bookedLayout.addView(imageView);
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    private void observeBorrowedBooks(final LinearLayout borrowedLayout) {
        LiveData<List<BookEntity>> borrowedBooksLiveData = bookDatabase.bookDao().getAllBorrowedBooks();
        borrowedBooksLiveData.observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(List<BookEntity> borrowedBooks) {
                if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            borrowedLayout.removeAllViews();
                            for (final BookEntity book : borrowedBooks) {
                                if(book.getUser() != null && book.getUser().contentEquals(user)){
                                    ImageView imageView = new ImageView(BorrowedActivity.this);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    layoutParams.setMargins(0, 0, 1, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    String coverUrl = book.getCover();
                                    Picasso.get().load(coverUrl).into(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(BorrowedActivity.this, BorrowedBookActivity.class);
                                            intent.putExtra("title", book.getEnglishTitle());
                                            intent.putExtra("username", user);
                                            startActivity(intent);
                                        }
                                    });
                                    borrowedLayout.addView(imageView);
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    public void openBrowse(View v){
        Intent browse = new Intent(this, BrowseActivity.class);
        browse.putExtra("username", user);
        startActivity(browse);
    }

    public void openMenu(View v){
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("username", user);
        startActivity(menu);
    }

    public void libInfo(View v){
        Intent lib = new Intent(this, LibraryActivity.class);
        lib.putExtra("username", user);
        startActivity(lib);
    }
}