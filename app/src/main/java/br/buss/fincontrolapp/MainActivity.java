package br.buss.fincontrolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Locale;

import br.buss.fincontrolapp.database.FinControlDB;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;
    FinControlDBOperations database;
    Double spentValue;
    Double earnedValue;
    TextView totalMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTitle("FinControl");

        database = new FinControlDBOperations(this);
        spentValue = database.getValueSpent();
        earnedValue = database.getValueEarned();

        pieChart = findViewById(R.id.piechart);
        totalMoney = findViewById(R.id.mainActivityTotalMoney);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pieChart.clearChart();
        spentValue = database.getValueSpent();
        earnedValue = database.getValueEarned();
        totalMoney.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(earnedValue - spentValue));

        if (spentValue == 0 && earnedValue == 0) {
            pieChart.addPieSlice(new PieModel(
                    "Sem transações", 100, getResources().getColor(android.R.color.darker_gray)
            ));
        } else {
            pieChart.addPieSlice(new PieModel(
                    "Débitos", spentValue.floatValue(), getResources().getColor(android.R.color.holo_red_light)
            ));
            pieChart.addPieSlice(new PieModel(
                    "Créditos", earnedValue.floatValue(), getResources().getColor(android.R.color.holo_green_light)
            ));
        }

        pieChart.startAnimation();
    }

    public void goToExtractActivity(View view) {
        Intent it = new Intent( this, ExtractActivity.class);
        startActivity(it);
    }

    public void goToSearchActivity(View view) {
        Intent it = new Intent( this, SearchActivity.class);
        startActivity(it);
    }

    public void goToNewOperationActivity(View view) {
        Intent it = new Intent(this, RegisterOperationsActivity.class);
        startActivity(it);
    }

    public void goToClassifiedListActivity(View view) {
        Intent it = new Intent(this, ClassifiedListActivity.class);
        startActivity(it);
    }

    public void exitAplication(View view) {
        finish();
    }

}