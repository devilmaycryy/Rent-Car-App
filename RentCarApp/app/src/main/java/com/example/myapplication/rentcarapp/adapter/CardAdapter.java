package com.example.myapplication.rentcarapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {
    private List<CreditCard> creditCards;

    public CardAdapter(List<CreditCard> creditCards){
        this.creditCards = creditCards;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CreditCard creditCard = creditCards.get(position);
        holder.cardNumber.setText(creditCard.getNumberCard());
        holder.cardDate.setText(creditCard.getValidDate());
        holder.nameBank.setText(creditCard.getBank());
    }

    @Override
    public int getItemCount() {
        return creditCards.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView cardNumber, cardDate, nameBank;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            cardDate = itemView.findViewById(R.id.cardDate);
            nameBank = itemView.findViewById(R.id.nameBank);
        }
    }
}
