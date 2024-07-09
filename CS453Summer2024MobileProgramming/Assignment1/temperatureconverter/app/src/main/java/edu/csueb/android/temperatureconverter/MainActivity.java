package edu.csueb.android.temperatureconverter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText celsiusText;
    private EditText fahrenheitText;

    private boolean ifCelsiusChanged;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celsiusText = findViewById(R.id.celsiusText);

        fahrenheitText = findViewById(R.id.fahrenheitText);


        celsiusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ifCelsiusChanged = true;



            }

        });
        fahrenheitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ifCelsiusChanged = false;

            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            if(!ifCelsiusChanged)
            {
                //Float.parseFloat(fahrenheitText.getText().toString())))
                celsiusText.setText(String.valueOf(ConverterUtil.convertFahrenheitToCelsius(Float.parseFloat(fahrenheitText.getText().toString()))));

            }
            else{
                //Float.parseFloat(celsiusText.getText().toString())))
                fahrenheitText.setText(String.valueOf(ConverterUtil.convertCelsiusToFahrenheit(Float.parseFloat(celsiusText.getText().toString()))));

            }



        }
    }
}
