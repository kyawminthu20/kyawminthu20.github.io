package edu.csueb.android.zoodirectory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private List<Animal> animalList;
    private Context context;

    public AnimalAdapter(Context context, List<Animal> animalList) {
        this.context = context.getApplicationContext();
        this.animalList = animalList;
        for (Animal animal : animalList) {
            Log.d("AnimalAdapter", "Animal: " + animal.getName());
        }
    }

    public class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView animalName;
        ImageView animalImage;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            animalName = itemView.findViewById(R.id.animal_name);
            animalImage = itemView.findViewById(R.id.animal_image);
        }
    }

    @NonNull
    @Override
    public AnimalAdapter.AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_list_item, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.AnimalViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.animalName.setText(animal.getName());
        holder.animalImage.setImageResource(animal.getImageResource());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnimalDetailActivity.class);
                intent.putExtra("animal", animal);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        Log.d("AnimalAdapter", "Item count inside adapter: " + animalList.size());
        Log.d("AnimalAdapter", "Binding position: " + position);
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }
}
