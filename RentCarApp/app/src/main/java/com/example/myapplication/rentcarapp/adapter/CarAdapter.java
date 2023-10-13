package com.example.myapplication.rentcarapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private List<Car> cars;
    private RecyclerViewInterface recyclerViewInterface;

    public CarAdapter(List<Car> cars, RecyclerViewInterface recyclerViewInterface){
        this.cars = cars;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = cars.get(position);
        Picasso.get().load(car.getPhoto()).into(holder.photoCar);
        holder.modelCar.setText(car.getModel());
        holder.priceCar.setText(car.getPrice() + "/Day");
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photoCar;
        TextView modelCar, priceCar;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            photoCar = itemView.findViewById(R.id.photoCar);
            modelCar = itemView.findViewById(R.id.modelCar);
            priceCar = itemView.findViewById(R.id.priceCar);

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
