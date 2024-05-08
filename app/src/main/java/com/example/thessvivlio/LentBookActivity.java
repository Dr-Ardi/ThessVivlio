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

public class LentBookActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BookDatabase bookDatabase;
    private BookRepository bookRepository;

    String user= "";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lentbook);

        db = FirebaseFirestore.getInstance();
        bookDatabase = BookDatabase.getDatabase(this);
        bookRepository = new BookRepository(this);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("title")) {

            title = intent.getStringExtra("title");
            user = intent.getStringExtra("user");
            getBook(title,user);
            getId(title);
        }


    }

    BookEntity book;
    private void getBook(String title, String user){
        LiveData<BookEntity> bookedBookLiveData = bookDatabase.bookDao().getBookByEnglishTitle(title, user);
        bookedBookLiveData.observe(this, new Observer<BookEntity>() {
            @Override
            public void onChanged(BookEntity bookedBook) {
                if (bookedBook != null) {
                    book = bookedBook;
                    applyBookInfo(bookedBook);
                }
                else{
                    System.out.println("Book is null");
                }
            }
        });
    }

    String id="";

    private void getId(String title){
        if (db != null) {
            db.collection("books")
                    .whereEqualTo("englishTitle", title)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = document.getId();
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

    private void applyBookInfo(BookEntity book){
        TextView bookTitle = findViewById(R.id.booktitle);
        bookTitle.setText("Title: " + book.getEnglishTitle());

        TextView gtitle = findViewById(R.id.gtitle);
        gtitle.setText("Greek Title: " + book.getGreekTitle());

        TextView author = findViewById(R.id.author);
        author.setText("Author: " + book.getAuthor());

        TextView isbn = findViewById(R.id.isbn);
        isbn.setText("ISBN: " + book.getIsbn());

        TextView whoBor = findViewById(R.id.user);
        whoBor.setText("User: " + book.getUser());

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
        Intent adminScreen = new Intent(this, AdminScreenActivity.class);
        startActivity(adminScreen);
    }

    public void returnBook(View v){

        DocumentReference bookRef = db.collection("books").document(id);

        bookRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int av = documentSnapshot.getLong("availableCopies").intValue();
                av++;

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

        bookRepository.delete(book);

        Intent adminScreen = new Intent(this, AdminScreenActivity.class);
        startActivity(adminScreen);

    }

    public void lendBook(View v){
        LiveData<BookEntity> bookedBookLiveData = bookDatabase.bookDao().getBookByEnglishTitle(title, user);
        bookedBookLiveData.observe(this, new Observer<BookEntity>() {
            @Override
            public void onChanged(BookEntity bookedBook) {
                if (bookedBook != null) {
                    if (!bookedBook.isBorrowed()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, 14);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        bookedBook.setTrueRetDate(String.valueOf(day) + "/" + String.valueOf(month));

                        bookedBook.setBorrowed(true);

                        bookRepository.update(bookedBook);

                        Intent adminScreen = new Intent(LentBookActivity.this, AdminScreenActivity.class);
                        startActivity(adminScreen);
                    }
                    else {
                        TextView alr = findViewById(R.id.already);
                        alr.setText("Already Borrowed");
                        alr.setTextColor(0xFFFF0000);
                    }
                } else {
                    System.out.println("Book is null");
                }
            }
        });
    }

}