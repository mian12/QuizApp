package com.solution.alnahar.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.solution.alnahar.quizapp.model.QuestionScore;
import com.solution.alnahar.quizapp.model.Ranking;
import com.solution.alnahar.quizapp.viewHolder.RankingViewHolder;
import com.solution.alnahar.quizapp.viewHolder.ScoreDetailViewHolder;

public class ScoreDetailActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference questionScore_db_ref;

    RecyclerView recyclerView_scoreDetail;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<QuestionScore,ScoreDetailViewHolder> adapter;




    String viewUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);


        database=FirebaseDatabase.getInstance();
        questionScore_db_ref=database.getReference("Question_score");

        recyclerView_scoreDetail=findViewById(R.id.recycler_scoreDetail);
        recyclerView_scoreDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_scoreDetail.setHasFixedSize(true);



        if (getIntent()!=null)
            viewUser=getIntent().getExtras().getString("viewHolder");
        if (!viewUser.isEmpty())
            loadScoreDetail(viewUser);

    }

    private void loadScoreDetail(String viewUser) {


        Query query =questionScore_db_ref .orderByChild("user").equalTo(viewUser);


        FirebaseRecyclerOptions<QuestionScore> options = new FirebaseRecyclerOptions.Builder<QuestionScore>()
                .setQuery(query, QuestionScore.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position, @NonNull QuestionScore model) {

                holder.txt_name.setText(model.getCategoryName());
                holder.txt_score.setText(model.getScore()+"");

            }

            @NonNull
            @Override
            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ScoreDetailActivity.this).inflate(R.layout.row_score_detail,parent, false);

                return new ScoreDetailViewHolder(view);
            }
        };

        recyclerView_scoreDetail.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }
}
