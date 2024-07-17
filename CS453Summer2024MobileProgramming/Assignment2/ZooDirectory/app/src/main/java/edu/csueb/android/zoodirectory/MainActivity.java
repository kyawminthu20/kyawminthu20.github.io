package edu.csueb.android.zoodirectory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;

import edu.csueb.android.zoodirectory.R; // This is the correct R class




public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        animalList = new ArrayList<>();
        animalList.add(new Animal("Lion", R.drawable.lion, "The lion is a species in the family Felidae."));
        animalList.add(new Animal("Tiger", R.drawable.tiger, "The tiger is the largest living cat species."));
        animalList.add(new Animal("Elephant", R.drawable.elephant, "Elephants are the largest existing land animals."));
        animalList.add(new Animal("Giraffe", R.drawable.giraffe, "The giraffe is the tallest living terrestrial animal."));
        animalList.add(new Animal("Zebra", R.drawable.zebra, "Zebras are African equids with distinctive black-and-white striped coats."));

        animalAdapter = new AnimalAdapter(this, animalList);
        Log.d("MainActivity", "Item count: " + animalList.size());

        recyclerView.setAdapter(animalAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_information){

            // Handle Information menu item click
            Intent infoIntent = new Intent(this, ZooInfoActivity.class);
            startActivity(infoIntent);
            return true;

        }else if(item.getItemId() == R.id.action_uninstall){

            // Handle Uninstall menu item click
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(uninstallIntent);
            return true;

        }

        return super.onOptionsItemSelected(item);




    }

}
