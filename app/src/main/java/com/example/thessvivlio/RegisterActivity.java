package com.example.thessvivlio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "RegisterActivity";

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();


        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
    }

    public void register(View v){

        TextView err = findViewById(R.id.error);

        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
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
                                err.setText("User "+ username+ " already exists");
                                err.setTextColor(0xFFFF0000);
                            } else {
                                if( !email.isEmpty() ){
                                    db.collection("users")
                                            .whereEqualTo("email", email)
                                            .limit(1)
                                            .get()
                                            .addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    QuerySnapshot querySnapshot2 = task2.getResult();
                                                    if (querySnapshot2 != null && !querySnapshot2.isEmpty()) {
                                                        err.setText("Email is already being used");
                                                        err.setTextColor(0xFFFF0000);
                                                    } else {
                                                        createAccount(username,email, password);
                                                        Intent menu = new Intent(this, MenuActivity.class);
                                                        menu.putExtra("username", username);
                                                        startActivity(menu);
                                                    }
                                                }
                                            });
                                }
                                else{
                                    createAccount(username," ", password);
                                    Intent menu = new Intent(this, MenuActivity.class);
                                    menu.putExtra("username", username);
                                    startActivity(menu);
                                }
                            }
                        }
                    });
        } else {
            err.setText("Fill in all fields");
            err.setTextColor(0xFFFF0000);
        }
    }

    private void createAccount(String username, String email, String password) {

        User user = new User(username, email, password);
        addDetails(user);

    }

    private void addDetails(User user){
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                    }
                });
    }

    public void openLog(View v){

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

    }

}