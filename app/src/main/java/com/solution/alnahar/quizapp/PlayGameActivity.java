package com.solution.alnahar.quizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.solution.alnahar.quizapp.common.Common;
import com.squareup.picasso.Picasso;

public class PlayGameActivity extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 6000;

    int progressValue = 0;

    CountDownTimer mCountDownTimer;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer = 0;


    ProgressBar progressBar;
    ImageView question_imageView;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);


        question_imageView = findViewById(R.id.question_image);
        question_text = findViewById(R.id.question_text);


        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.totalQuestion);


        progressBar = findViewById(R.id.progressbar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

        totalQuestion = Common.questionList.size();




        mCountDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

                progressBar.setProgress(progressValue);
                progressValue++;

            }

            @Override
            public void onFinish() {

                mCountDownTimer.cancel();

                showQuestion(++index);
            }
        };



        showQuestion(index);

    }

    @Override
    public void onClick(View v) {


        mCountDownTimer.cancel();

        Button buttonName=(Button)v;

        String Correct_Answer = Common.questionList.get(index).getCorrectAnswer();

        if (buttonName.getText().toString().equalsIgnoreCase(Correct_Answer)) {
            // choose correct answer
            score += 10;
            correctAnswer++;
            showQuestion(++index); // next question
        } else {
            // choose wrong answer
            finalResultsActivity();

        }

        txtScore.setText(score + "");





    }



    private void showQuestion(int index) {

        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));

            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {

                String url = Common.questionList.get(index).getQuestion();

                Picasso.with(this)
                        .load(url)
                        .into(question_imageView);

                question_imageView.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            } else {
                question_text.setText(Common.questionList.get(index).getQuestion());

                question_imageView.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }


            String btnA_value = Common.questionList.get(index).getAnswerA();
            String btnB_value = Common.questionList.get(index).getAnswerB();
            String btnC_value = Common.questionList.get(index).getAnswerC();
            String btnD_value = Common.questionList.get(index).getAnswerD();

            btnA.setText(btnA_value);
            btnB.setText(btnB_value);
            btnC.setText(btnC_value);
            btnD.setText(btnD_value);

            mCountDownTimer.start();

        } else {
            // if it is final question

            finalResultsActivity();


        }
    }

    public void finalResultsActivity() {


        Intent intent = new Intent(PlayGameActivity.this, DoneActivity.class);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("SCORE", score);
        dataBundle.putInt("TOTAL", totalQuestion);
        dataBundle.putInt("CORRECT", correctAnswer);

        intent.putExtras(dataBundle);

        startActivity(intent);
        finish();


    }


}
