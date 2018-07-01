package com.solution.alnahar.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solution.alnahar.quizapp.common.Common;
import com.solution.alnahar.quizapp.model.Question;

import java.util.Collections;

public class StartGameActivity extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions_db_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        btnPlay=findViewById(R.id.btn_play);


        database = FirebaseDatabase.getInstance();
        questions_db_ref = database.getReference("Questions");

        loadQuestions(Common.categoryId);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartGameActivity.this,PlayGameActivity.class));
                finish();
            }
        });



    }

    private void loadQuestions(String categoryId) {

        // first clear arrayList if its already filled

        if (Common.questionList.size()>0)
            Common.questionList.clear();



        questions_db_ref.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Question question=snapshot.getValue(Question.class);

                    Common.questionList.add(question);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // random list

        Collections.shuffle(Common.questionList);
    }


}
