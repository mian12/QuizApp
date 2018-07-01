package com.solution.alnahar.quizapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.solution.alnahar.quizapp.HomeActivity;
import com.solution.alnahar.quizapp.Interface.ItemClickListener;
import com.solution.alnahar.quizapp.R;
import com.solution.alnahar.quizapp.StartGameActivity;
import com.solution.alnahar.quizapp.common.Common;
import com.solution.alnahar.quizapp.model.CategoryModel;
import com.solution.alnahar.quizapp.viewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {


    RecyclerView recyclerView_category;

    FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories_db_ref;

    public CategoryFragment() {
        // Required empty public constructor

        database = FirebaseDatabase.getInstance();
        categories_db_ref = database.getReference("CategoryBackground");


    }


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);


        recyclerView_category = view.findViewById(R.id.listCategories);
        recyclerView_category.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_category.setHasFixedSize(true);


        loadCategories();


        return view;
    }

    private void loadCategories() {


        FirebaseRecyclerOptions<CategoryModel> options = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                .setQuery(categories_db_ref, CategoryModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull CategoryModel model) {


                holder.category_TextView.setText(model.getName());


                Picasso.with(getActivity())
                        .load(model.getImage())

                        .into(holder.category_Image);

                final CategoryModel object = model;


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(getActivity(), "" + object.getName(), Toast.LENGTH_SHORT).show();

                        Common.categoryId=adapter.getRef(position).getKey();

                        Intent intent=new Intent(getActivity(), StartGameActivity.class);

                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_category, parent, false);

                return new CategoryViewHolder(view);
            }
        };


        recyclerView_category.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }
}
