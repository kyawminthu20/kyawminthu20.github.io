package edu.csueb.android.zoodirectory;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        animalList = new ArrayList<>();
        animalList.add(new Animal("Lion", R.drawable.lion, "The lion is a species in the family Felidae."));
        animalList.add(new Animal("Tiger", R.drawable.tiger, "The tiger is the largest living cat species."));
        animalList.add(new Animal("Elephant", R.drawable.elephant, "Elephants are the largest existing land animals."));
        animalList.add(new Animal("Giraffe", R.drawable.giraffe, "The giraffe is the tallest living terrestrial animal."));
        animalList.add(new Animal("Zebra", R.drawable.zebra, "Zebras are African equids with distinctive black-and-white striped coats."));

        animalAdapter = new AnimalAdapter(this, animalList);
        recyclerView.setAdapter(animalAdapter);
    }
}
