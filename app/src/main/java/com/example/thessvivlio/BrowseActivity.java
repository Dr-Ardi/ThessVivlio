package com.example.thessvivlio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Intent browse = getIntent();

        if (browse != null && browse.hasExtra("username")) {

            user = browse.getStringExtra("username");
        }

        db = FirebaseFirestore.getInstance();

        LinearLayout list = findViewById(R.id.browseList);

        ImageButton imageButton = findViewById(R.id.imageButton);
        EditText editText = findViewById(R.id.editTextText);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editText.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    filterBooks(searchText, list);
                } else {
                    getAllBooks(list);
                }
            }
        });


        getAllBooks(list);
    }

    private void filterBooks(String searchText, LinearLayout list) {
        db.collection("books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Book> unfilteredBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            unfilteredBooks.add(book);
                        }
                        List<Book> filteredBooks = new ArrayList<>();
                        for (Book book : unfilteredBooks) {
                            if (book.getEnglishTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                                    book.getGreekTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                                    book.getAuthor().toLowerCase().contains(searchText.toLowerCase())) {
                                filteredBooks.add(book);
                            }
                        }
                        list.removeAllViews();
                        displayBooks(filteredBooks, list);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void getAllBooks(LinearLayout list){
        db.collection("books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Book> theBooks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            theBooks.add(book);
                        }
                        displayBooks(theBooks, list);
                    } else {
                        System.out.println("Database Error");
                    }
                });
    }

    private void displayBooks(List<Book> theBooks, LinearLayout layout) {
        for (Book book : theBooks) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 0);
            textView.setPadding(0, 10, 0, 10);
            textView.setText(book.getEnglishTitle());
            textView.setTextSize(28);
            textView.setTextColor(0xFF1E1E1E);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BrowseActivity.this, BookActivity.class);
                    intent.putExtra("title", book.getEnglishTitle());
                    intent.putExtra("username", user);
                    startActivity(intent);
                }
            });
            layout.addView(textView);
        }
    }

    public void openMenu(View v){
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("username", user);
        startActivity(menu);
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