package com.example.myapplication.rentcarapp.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.view.activity.MainActivity;
import com.example.myapplication.rentcarapp.view.activity.MyCardsActivity;
import com.example.myapplication.rentcarapp.view.activity.MyProfileActivity;
import com.example.myapplication.rentcarapp.viewmodel.AuthViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    CardView myProfile, myCards, logOut;
    ShapeableImageView userAvatar;
    TextView clientUsername;
    AuthViewModel authViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAvatar = view.findViewById(R.id.userAvatar);
        clientUsername = view.findViewById(R.id.clientFullName);
        myProfile = view.findViewById(R.id.myProfile);
        myCards = view.findViewById(R.id.myCards);
        logOut = view.findViewById(R.id.logOut);
        myProfile.setOnClickListener(this::clickCard);
        myCards.setOnClickListener(this::clickCard);
        logOut.setOnClickListener(this::clickCard);
        userAvatar.setTranslationZ(50);
        initData();
    }

    private void initData(){
        authViewModel.getClient().observe(requireActivity(), client -> {
            Picasso.get().load(client.getPhoto()).into(userAvatar);
            clientUsername.setText(client.getUser());
        });
    }

    public void clickCard(View view){
        if(view.getId() == R.id.myProfile){
            transitions(MyProfileActivity.class);
        }if(view.getId() == R.id.myCards){
            transitions(MyCardsActivity.class);
        }if(view.getId() == R.id.logOut){
            authViewModel.logOut();
            transitions(MainActivity.class);
        }
    }

    private void transitions(Class<?> contextClass){
        Intent intent = new Intent(requireActivity(), contextClass);
        startActivity(intent);
    }



}