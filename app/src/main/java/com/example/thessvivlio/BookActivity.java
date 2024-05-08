package com.example.thessvivlio;

import android.content.Intent;
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

import java.util.Calendar;

public class BookActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BookRepository bookRepository;
    private BookDatabase bookDatabase;

    String user= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        db = FirebaseFirestore.getInstance();
        bookRepository = new BookRepository(this);
        bookDatabase = BookDatabase.getDatabase(this);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("title")) {

            String title = intent.getStringExtra("title");
            user = intent.getStringExtra("username");
            getBook(title);
        }


    }

    Book book;
    String id = "";
    private void getBook(String title){
        if (db != null) {
            db.collection("books")
                    .whereEqualTo("englishTitle", title)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                book = document.toObject(Book.class);
                                id = document.getId();
                                applyBookInfo(book);
                            }

                        }
                        else {
                            System.out.println("Database Error");
                        }
                    });
        }
        else {
            System.out.println("Database Null");
        }
    }

    private void applyBookInfo(Book book){
        TextView bookTitle = findViewById(R.id.booktitle);
        bookTitle.setText("Title: " + book.getEnglishTitle());

        TextView gtitle = findViewById(R.id.gtitle);
        gtitle.setText("Greek Title: " + book.getGreekTitle());

        TextView author = findViewById(R.id.author);
        author.setText("Author: " + book.getAuthor());

        TextView genre = findViewById(R.id.genre);
        genre.setText("Genre: " + book.getGenre());

        TextView year = findViewById(R.id.year);
        year.setText("Pub. Year: " + String.valueOf(book.getPublicationYear()));

        TextView isbn = findViewById(R.id.isbn);
        isbn.setText("ISBN: " + book.getIsbn());

        TextView copies = findViewById(R.id.retDate);
        if(book.getAvailableCopies() > 0)
            copies.setText("Av. Copies: " + String.valueOf(book.getAvailableCopies()));
        else {
            copies.setText("No Copies Left   :(");
            copies.setTextColor(0xFFFF0000);
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
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("username", user);
        startActivity(menu);
    }

    boolean key = true;

    public void borrow(View v){
        TextView noMore = findViewById(R.id.noMore);

        LiveData<BookEntity> bookedBookLiveData = bookDatabase.bookDao().getBookByEnglishTitle(book.getEnglishTitle(), user);
        bookedBookLiveData.observe(this, new Observer<BookEntity>() {
            @Override
            public void onChanged(BookEntity bookedBook) {
                if (bookedBook != null)
                    key = false;
            }
        });

        if(key){
            if(book.getAvailableCopies()>0){
                BookEntity bookEnt = new BookEntity();
                bookEnt.setEnglishTitle(book.getEnglishTitle());
                bookEnt.setGreekTitle(book.getGreekTitle());
                bookEnt.setIsbn(book.getIsbn());
                bookEnt.setAuthor(book.getAuthor());
                bookEnt.setCover(book.getCover());
                bookEnt.setUser(user);

                bookRepository.insert(bookEnt);

                DocumentReference bookRef = db.collection("books").document(id);

                bookRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int av = book.getAvailableCopies() -1;

                        bookRef.update("availableCopies", av)
                                .addOnSuccessListener(aVoid -> {
                                    System.out.println("Success");
                                })
                                .addOnFailureListener(e -> {
                                    System.out.println("Failure");
                                });
                    } else {
                        System.out.println("Not Found");
                    }
                });

                Intent borrow = new Intent(this, BorrowedActivity.class);
                borrow.putExtra("username", user);
                startActivity(borrow);
            }
            else{
                noMore.setText("No more copies left");
                noMore.setTextColor(0xFFFF0000);
            }
        }
        else{
            noMore.setText("Already borrowed this book");
            noMore.setTextColor(0xFFFF0000);
        }


    }

}