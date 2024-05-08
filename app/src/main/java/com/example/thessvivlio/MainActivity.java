package com.example.thessvivlio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
//    private static final String TAG = "MainActivity";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();


        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
    }

    public void login(View v){

        TextView err = findViewById(R.id.error);

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            db.collection("users")
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    User user = document.toObject(User.class);
                                    if (user.getPassword().contentEquals(password)) {
                                        if(username.contentEquals("admin")){
                                            Intent adminScreen = new Intent(this, AdminScreenActivity.class);
                                            startActivity(adminScreen);
                                        }
                                        else {
                                            Intent menu = new Intent(this, MenuActivity.class);
                                            menu.putExtra("username", user.getUsername());
                                            startActivity(menu);
                                        }
                                    } else {
                                        err.setText("Password doesn't match");
                                        err.setTextColor(0xFFFF0000);
                                    }
                                }
                            } else {
                                err.setText("User doesn't exist");
                                err.setTextColor(0xFFFF0000);
                            }
                        } else {
                            err.setText("Error: " + task.getException().getMessage());
                            err.setTextColor(0xFFFF0000);
                        }
                    });
        } else {
            err.setText("Fill in all fields");
            err.setTextColor(0xFFFF0000);
        }

    }

    public void openReg(View v){

        Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);

    }

}