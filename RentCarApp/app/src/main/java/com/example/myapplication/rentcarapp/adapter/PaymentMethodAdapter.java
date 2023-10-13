package com.example.myapplication.rentcarapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {
    private List<CreditCard> creditCards;
    private PaymentMethodInterface paymentMethodInterface;

    public PaymentMethodAdapter(List<CreditCard> creditCards, PaymentMethodInterface paymentMethodInterface) {
        this.creditCards = creditCards;
        this.paymentMethodInterface = paymentMethodInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_list, parent, false);
        return new ViewHolder(view, paymentMethodInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreditCard creditCard = creditCards.get(position);
        holder.paymentMethodBank.setText(creditCard.getBank());
        holder.paymentMethodCardNumber.setText(creditCard.getNumberCard());
    }

    @Override
    public int getItemCount() {
        return creditCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton choosePaymentMethod;
        TextView paymentMethodBank, paymentMethodCardNumber;

        public ViewHolder(@NonNull View itemView, PaymentMethodInterface paymentMethodInterface) {
            super(itemView);
            choosePaymentMethod = itemView.findViewById(R.id.choosePaymentMethod);
            paymentMethodBank = itemView.findViewById(R.id.paymentMethodBank);
            paymentMethodCardNumber = itemView.findViewById(R.id.paymentMethodCardNumber);

            choosePaymentMethod.setOnClickListener(view -> {
                if(paymentMethodInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        paymentMethodInterface.onClickRadioButton(position);
                    }
                }
            });
        }
    }
}
