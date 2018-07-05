package com.solution.alnahar.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.solution.alnahar.quizapp.common.Common;
import com.solution.alnahar.quizapp.model.QuestionScore;

public class DoneActivity extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTextResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference questionScore_db_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        questionScore_db_ref = database.getReference("Question_score");

        txtResultScore = findViewById(R.id.txt_totalScore);
        getTextResultQuestion = findViewById(R.id.txt_totalQuestion);
        progressBar = findViewById(R.id.progressbar_Done);

        btnTryAgain = findViewById(R.id.btnTryAgain);


        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoneActivity.this, HomeActivity.class));
                finish();
            }
        });

        // get data from  bundle and set to view

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("Score : %d", score));
            getTextResultQuestion.setText(String.format("Passed : %d / %d", correctAnswer, totalQuestion));

            progressBar.setProgress(correctAnswer);
            progressBar.setMax(totalQuestion);


            // upload  to db

            QuestionScore object=new QuestionScore();

            object.setQuestion_score(String.format("%s_%s",Common.currentUser.getUserName(),Common.categoryId));

            object.setUser(Common.currentUser.getUserName());

            object.setScore(String.valueOf(score));



            questionScore_db_ref.child(String.format("%s_%s", Common.currentUser.getUserName(),Common.categoryId))
                    .setValue(object);




        }


    }
}
