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
    final static long TIMEOUT = 7000;

    int progressValue = 0;

    CountDownTimer mCountDownTimer;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer = 0;

    // firebase
    FirebaseDatabase database;
    DatabaseReference question_db_ref;

    ProgressBar progressBar;
    ImageView question_imageView;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        database = FirebaseDatabase.getInstance();
        question_db_ref = database.getReference("Questions");


        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.totalQuestion);
        question_text = findViewById(R.id.question_text);

        progressBar = findViewById(R.id.progressbar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        mCountDownTimer.cancel();
        if (index < totalQuestion)  // still have question in list
        {

            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                // choose correct answer
                score += 10;
                correctAnswer++;
                showQuestion(++index); // next question
            } else {

                // choose wrong answer
                Intent intent = new Intent(PlayGameActivity.this, DoneActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("SCORE", score);
                dataBundle.putInt("TOTAL", totalQuestion);
                dataBundle.putInt("CORRECT", correctAnswer);

                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();

            }

            txtScore.setText(score);
        }


    }

    private void showQuestion(int index) {

        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d", "%d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;
            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                Picasso.with(this)
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_imageView);

                question_imageView.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else
            {
                question_text.setText(Common.questionList.get(index).getQuestion());

                question_imageView.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDownTimer.start();
        }
      else
          {
                // if it is final question

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


    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion=Common.questionList.size();
        mCountDownTimer=new CountDownTimer(TIMEOUT,INTERVAL) {
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

        showQuestion(++index);
    }
}
