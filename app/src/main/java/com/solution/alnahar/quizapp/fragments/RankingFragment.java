package com.solution.alnahar.quizapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.solution.alnahar.quizapp.Interface.ItemClickListener;
import com.solution.alnahar.quizapp.Interface.RankingCallBack;
import com.solution.alnahar.quizapp.R;
import com.solution.alnahar.quizapp.common.Common;
import com.solution.alnahar.quizapp.model.QuestionScore;
import com.solution.alnahar.quizapp.model.Ranking;
import com.solution.alnahar.quizapp.viewHolder.RankingViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {


    FirebaseDatabase database;
    DatabaseReference questionScore_db_ref, ranking_db_ref;

    RecyclerView recyclerView_rankingList;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;

    int sum=0;


    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();

        return rankingFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();
        questionScore_db_ref=database.getReference("Question_score");
        ranking_db_ref=database.getReference("Ranking");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);


        // because ordrByChild  method of firebase will sort list with ascending
        // so we need revervse our recycler data
        // by layout manager

        recyclerView_rankingList=view.findViewById(R.id.recycler_ranking);
        //recyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_rankingList.setHasFixedSize(true);

        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView_rankingList.setLayoutManager(linearLayoutManager);



        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                // update to Ranking table
                ranking_db_ref.child(ranking.getUserName()).setValue(ranking);

               // showRanking(); // after upload, we will sort ranking table and show result
            }
        });



        Query query =ranking_db_ref .orderByChild("score");


        FirebaseRecyclerOptions<Ranking> options = new FirebaseRecyclerOptions.Builder<Ranking>()
                .setQuery(query, Ranking.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull Ranking model) {

                holder.txt_name.setText(model.getUserName());
                holder.txt_score.setText(model.getScore()+"");
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });



            }

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_ranking,parent, false);

                return new RankingViewHolder(view);
            }
        };

        recyclerView_rankingList.setAdapter(adapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
           adapter.stopListening();
    }


    private void showRanking() {
        ranking_db_ref.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    Ranking local=data.getValue(Ranking.class);
                    Log.e("d",local.getUserName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack) {

        questionScore_db_ref.orderByChild("user").equalTo(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    QuestionScore questionScore=data.getValue(QuestionScore.class);
                    sum+=Integer.parseInt(questionScore.getScore());
                }

                // after summery all score, we need process some variables here
                // beause firebase is async dtabase, so if proceess outside our 'some' variable value will be rest to 0
                Ranking ranking=new Ranking(userName,sum);

                callBack.callBack(ranking);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
