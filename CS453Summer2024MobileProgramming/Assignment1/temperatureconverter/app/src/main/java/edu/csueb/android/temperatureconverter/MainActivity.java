package edu.csueb.android.temperatureconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.editText);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            RadioButton celsiusButton = findViewById(R.id.celsiusRadioButton);
            RadioButton fahrenheitButton = findViewById(R.id.fahrenheitRadioButton);

            if (text.getText().length() == 0) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_LONG).show();
                return;
            }

            float inputValue = Float.parseFloat(text.getText().toString());
            if (celsiusButton.isChecked()) {
                text.setText(String.valueOf(ConverterUtil.convertFahrenheitToCelsius(inputValue)));
                celsiusButton.setChecked(false);
                fahrenheitButton.setChecked(true);
            } else {
                text.setText(String.valueOf(ConverterUtil.convertCelsiusToFahrenheit(inputValue)));
                fahrenheitButton.setChecked(false);
                celsiusButton.setChecked(true);
            }
        }
    }
}