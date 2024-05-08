package com.example.thessvivlio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BorrowedBookActivity extends AppCompatActivity {

    private BookDatabase bookDatabase;

    private BookDao bookDao;


    String user= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowedbook);

        bookDatabase = BookDatabase.getDatabase(this);
        bookDao = bookDatabase.bookDao();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("title")) {

            String title = intent.getStringExtra("title");
            user = intent.getStringExtra("username");
            getBook(title);
        }


    }
    private void getBook(String title){
        LiveData<BookEntity> bookedBookLiveData = bookDatabase.bookDao().getBookByEnglishTitle(title, user);
        bookedBookLiveData.observe(this, new Observer<BookEntity>() {
            @Override
            public void onChanged(BookEntity bookedBook) {
                if (bookedBook != null) {
                    applyBookInfo(bookedBook);
                }
            }
        });
    }

    private void applyBookInfo(BookEntity book){
        TextView bookTitle = findViewById(R.id.booktitle);
        bookTitle.setText("Title: " + book.getEnglishTitle());

        TextView gtitle = findViewById(R.id.gtitle);
        gtitle.setText("Greek Title: " + book.getGreekTitle());

        TextView author = findViewById(R.id.author);
        author.setText("Author: " + book.getAuthor());

        TextView isbn = findViewById(R.id.isbn);
        isbn.setText("ISBN: " + book.getIsbn());

        if(book.isBorrowed()){
            TextView retDate = findViewById(R.id.retDate);
            retDate.setText("Ret. date: " + book.getTrueRetDate());
        }

        LinearLayout layout = findViewById(R.id.coverplace);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(layoutParams);
        String coverUrl = book.getCover();
        Picasso.get().load(coverUrl).into(imageView);
        layout.addView(imageView);
    }

    public void close(View v){
        Intent borrowed = new Intent(this, BorrowedActivity.class);
        borrowed.putExtra("username", user);
        startActivity(borrowed);
    }

}