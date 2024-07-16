package edu.csueb.android.zoodirectory;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalDetailActivity extends AppCompatActivity {
    private TextView animalName;
    private ImageView animalImage;
    private TextView animalDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        animalName = findViewById(R.id.animal_detail_name);
        animalImage = findViewById(R.id.animal_detail_image);
        animalDescription = findViewById(R.id.animal_detail_description);

        Animal animal = (Animal) getIntent().getParcelableExtra("animal");
        if (animal != null) {
            animalName.setText(animal.getName());
            animalImage.setImageResource(animal.getImageResource());
            animalDescription.setText(animal.getDescription());
        }
    }
}
