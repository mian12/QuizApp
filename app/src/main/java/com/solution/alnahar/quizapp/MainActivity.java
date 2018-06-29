package com.solution.alnahar.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.solution.alnahar.quizapp.model.UserModel;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtUserName, edtPassword;
    Button signIn, signUp;
    FirebaseDatabase database;
    DatabaseReference users_db_ref;

    public SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.btn_signIn);
        signUp = findViewById(R.id.btn_signUp);


        edtUserName = findViewById(R.id.editUserName);
        edtPassword = findViewById(R.id.editPassword);


//
        dialog = new SpotsDialog(MainActivity.this);
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        users_db_ref = database.getReference("Users");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                signIn(edtUserName.getText().toString(), edtPassword.getText().toString());

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSignUpAlertDialog();
            }
        });


    }


    private void showSignUpAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign up");
        alertDialog.setMessage("Please fill full information");


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_sign_up, null);
        final MaterialEditText editTextUserName = view.findViewById(R.id.editName);
        final MaterialEditText editTextPassword = view.findViewById(R.id.editPassword);
        final MaterialEditText editTextEmail = view.findViewById(R.id.editEmail);


        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // create new request


                final UserModel userModel = new UserModel(editTextUserName.getText().toString(),
                        editTextPassword.getText().toString(), editTextEmail.getText().toString());


                users_db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(editTextUserName.getText().toString()).exists()) {
                            Toast.makeText(MainActivity.this, "User already exits!!", Toast.LENGTH_SHORT).show();
                        } else {
                            users_db_ref.child(editTextUserName.getText().toString()).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(MainActivity.this, "User registration success!!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();


    }


    private void signIn(final String userName, final String password) {

        users_db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialog.dismiss();

                if (dataSnapshot.child(userName).exists()) {

                    if (!userName.isEmpty()) {
                        UserModel login = dataSnapshot.child(userName).getValue(UserModel.class);
                        if (login.getPassword().equals(password)) {

                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(MainActivity.this, "Please enter your user name", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(MainActivity.this, "User does't exits!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                dialog.dismiss();
            }
        });
    }

}
