package com.example.thessvivlio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent menu = getIntent();

        if (menu != null && menu.hasExtra("username")) {

            user = menu.getStringExtra("username");
        }

        db = FirebaseFirestore.getInstance();

        HorizontalScrollView sciFiScrollView = findViewById(R.id.horizontalScrollView4);
        LinearLayout sciFiLayout = sciFiScrollView.findViewById(R.id.scifi);

        HorizontalScrollView displayScrollView = findViewById(R.id.horizontalScrollView);
        LinearLayout displayLayout = displayScrollView.findViewById(R.id.discover);

        HorizontalScrollView bSellerScrollView = findViewById(R.id.horizontalScrollView2);
        LinearLayout bSellerLayout = bSellerScrollView.findViewById(R.id.bestSeller);

        HorizontalScrollView y23ScrollView = findViewById(R.id.horizontalScrollView3);
        LinearLayout y23Layout = y23ScrollView.findViewById(R.id.y2023);

        HorizontalScrollView horrorScrollView = findViewById(R.id.horizontalScrollView5);
        LinearLayout horrorLayout = horrorScrollView.findViewById(R.id.horror);

        HorizontalScrollView romanceScrollView = findViewById(R.id.horizontalScrollView6);
        LinearLayout romanceLayout = romanceScrollView.findViewById(R.id.romance);


        retrieveBooks(displayLayout);
        retrieveBestSellers(bSellerLayout);
        retrieveThisYearsBooks(y23Layout);
        retrieveBooksByGenre(sciFiLayout, "Science Fiction");
        retrieveBooksByGenre(horrorLayout, "Horror");
        retrieveBooksByGenre(romanceLayout, "Romance");
    }

    List<Book> theBooks;
    private void retrieveBooksByGenre(LinearLayout layout, String genre) {
        db.collection("books")
                .whereEqualTo("genre", genre)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        theBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            theBooks.add(book);
                        }
                        displayBooks(theBooks, layout);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void retrieveBooks(LinearLayout layout) {
        db.collection("books")
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        theBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            theBooks.add(book);
                        }
                        displayBooks(theBooks, layout);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void retrieveBestSellers(LinearLayout layout) {
        db.collection("books")
                .whereEqualTo("bestseller", true)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        theBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            theBooks.add(book);
                        }
                        displayBooks(theBooks, layout);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void retrieveThisYearsBooks(LinearLayout layout) {
        db.collection("books")
                .whereEqualTo("publicationYear", 2023)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        theBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            theBooks.add(book);
                        }
                        displayBooks(theBooks, layout);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void displayBooks(List<Book> theBooks, LinearLayout layout) {
        for (Book book : theBooks) {
            ImageView imageView = new ImageView(this);
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
                    Intent intent = new Intent(MenuActivity.this, BookActivity.class);
                    intent.putExtra("title", book.getEnglishTitle());
                    intent.putExtra("username", user);
                    startActivity(intent);
                }
            });
            layout.addView(imageView);
        }
    }

    public void openBrowse(View v){
        Intent browse = new Intent(this, BrowseActivity.class);
        browse.putExtra("username", user);
        startActivity(browse);
    }

    public void openBorrowed(View v){
        Intent borrowed = new Intent(this, BorrowedActivity.class);
        borrowed.putExtra("username", user);
        startActivity(borrowed);
    }

    public void libInfo(View v){
        Intent lib = new Intent(this, LibraryActivity.class);
        lib.putExtra("username", user);
        startActivity(lib);
    }
}