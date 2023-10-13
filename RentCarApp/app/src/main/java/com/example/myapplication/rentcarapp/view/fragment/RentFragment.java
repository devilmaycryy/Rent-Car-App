package com.example.myapplication.rentcarapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.adapter.RecyclerViewInterface;
import com.example.myapplication.rentcarapp.adapter.RentAdapter;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;
import com.example.myapplication.rentcarapp.view.activity.RentDetailActivity;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.util.List;

public class RentFragment extends Fragment implements RecyclerViewInterface {
    RecyclerView rentList;
    CarViewModel carViewModel;
    List<Rent> clientRents;
    RentAdapter rentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rentList = view.findViewById(R.id.rentList);
        initData();
    }


    private void initData(){
        carViewModel.getClientRents().observe(requireActivity(), rents -> {
            if(rents != null){
                initRecyclerView(rents);
            }
        });
    }

    private void initRecyclerView(List<Rent> rents){
        clientRents = rents;
        rentAdapter = new RentAdapter(rents, this);
        rentList.setHasFixedSize(true);
        rentList.setAdapter(rentAdapter);
        rentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireActivity(), RentDetailActivity.class);
        intent.putExtra("Rent", clientRents.get(position));
        startActivity(intent);
    }
}