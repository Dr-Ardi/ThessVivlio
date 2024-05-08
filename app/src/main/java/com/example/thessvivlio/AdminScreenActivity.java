package com.example.thessvivlio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminScreenActivity extends AppCompatActivity {

    private BookDatabase bookDatabase;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminscreen);

        bookDatabase = BookDatabase.getDatabase(this);
        bookRepository = new BookRepository(this);

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

                                    ImageView imageView = new ImageView(AdminScreenActivity.this);
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
                                            Intent intent = new Intent(AdminScreenActivity.this, LentBookActivity.class);
                                            intent.putExtra("title", book.getEnglishTitle());
                                            intent.putExtra("user", book.getUser());
                                            startActivity(intent);
                                        }
                                    });
                                    bookedLayout.addView(imageView);

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

                                    ImageView imageView = new ImageView(AdminScreenActivity.this);
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
                                            Intent intent = new Intent(AdminScreenActivity.this, LentBookActivity.class);
                                            intent.putExtra("title", book.getEnglishTitle());
                                            intent.putExtra("user", book.getUser());
                                            startActivity(intent);
                                        }
                                    });
                                    borrowedLayout.addView(imageView);

                            }
                        }
                    });
                }
            }
        });
    }
}