package com.example.myapplication.rentcarapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;

import java.util.List;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.Holder> {
    private List<Rent> rents;
    private RecyclerViewInterface recyclerViewInterface;

    public RentAdapter(List<Rent> rents, RecyclerViewInterface recyclerViewInterface) {
        this.rents = rents;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rent_list, parent, false);
        return new Holder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Rent rent = rents.get(position);
        holder.idCar.setText("ID Car: " + rent.getCar());
        holder.issuingStation.setText("Delivery: " + rent.getStationIssuing());
        holder.returnStation.setText("Return to: " + rent.getStationReturn());
        holder.totalAmountRent.setText("Total amount: " + rent.getTotalAmount() + " ₴");
    }

    @Override
    public int getItemCount() {
        return rents.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView idCar, issuingStation, returnStation, totalAmountRent;
        public Holder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            idCar = itemView.findViewById(R.id.idCar);
            issuingStation = itemView.findViewById(R.id.issuingStation);
            returnStation = itemView.findViewById(R.id.returnStation);
            totalAmountRent = itemView.findViewById(R.id.totalAmountRent);

            itemView.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}
