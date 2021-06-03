package com.example.activity5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.activity5.Retrofit.RetrofitBuilder;
import com.example.activity5.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button button_convert;
    EditText text_BC, text_converted;
    Spinner s_from, s_to;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_convert = (Button)findViewById(R.id.btn_convert);
        text_BC = (EditText)findViewById(R.id.txt_BC);
        text_converted = (EditText)findViewById(R.id.txt_converted);
        s_from = (Spinner)findViewById(R.id.spin_from);
        s_to = (Spinner)findViewById(R.id.spin_to);

        String [] dropdownlist = {"USD","EUR","KRW","JPY","PHP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,dropdownlist);
        s_from.setAdapter(adapter);
        s_to.setAdapter(adapter);

        button_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInterface retrofitinterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);

                Call<JsonObject> call = retrofitinterface.getExchangeCurrency(s_from.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        Toast.makeText(MainActivity.this, "COMPUTED", Toast.LENGTH_SHORT).show();
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");
                        Double currency = Double.valueOf(text_BC.getText().toString());
                        Double multiplier = Double.valueOf(rates.get(s_to.getSelectedItem().toString()).toString());
                        Double result = currency * multiplier;
                        text_converted.setText(String.valueOf(result));

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });



    }
}