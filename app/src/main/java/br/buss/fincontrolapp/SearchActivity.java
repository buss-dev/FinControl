package br.buss.fincontrolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.Calendar;
import java.util.List;

import br.buss.fincontrolapp.adapter.TransactionAdapter;
import br.buss.fincontrolapp.helpers.DBUtils;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;
import br.buss.fincontrolapp.models.Transaction;

public class SearchActivity extends AppCompatActivity {

    private DatePickerDialog dateStartPickerDialog;
    private DatePickerDialog dateEndPickerDialog;
    private Button dateStartButton;
    private Button dateEndButton;
    private String startFilterDate;
    private String endFilterDate;
    private RecyclerView recyclerView;
    private List<Transaction> transactionList;
    private FinControlDBOperations database;
    private TransactionAdapter transactionAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RadioButton isDebit;
    private RadioButton isCredit;
    private RadioButton isAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Pesquisar operações");

        dateStartButton = findViewById(R.id.datePickerStart);
        dateEndButton = findViewById(R.id.datePickerEnd);

        isDebit = findViewById(R.id.radioDebit);
        isCredit = findViewById(R.id.radioCredit);
        isAll = findViewById(R.id.radioAll);

        initStartDatePicker();
        initEndDatePicker();
        dateStartButton.setText(getTodaysDate());
        dateEndButton.setText(getTodaysDate());

        startFilterDate = DBUtils.getFormattedDate();
        endFilterDate = DBUtils.getFormattedDate();

        recyclerView = findViewById(R.id.recyclerSearchActivity);
        database = new FinControlDBOperations(this);
        transactionList = database.getTransactionsFilteredByTime(startFilterDate, endFilterDate, "all");

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                startFilterDate = DBUtils.getFormattedDate(year, month, dayOfMonth);
                dateStartButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        dateStartPickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = DBUtils.makeDateString(dayOfMonth, month, year);
                endFilterDate = DBUtils.getFormattedDate(year, month, dayOfMonth);
                dateEndButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        dateEndPickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1) {
            return "JAN";
        }
        if(month == 2) {
            return "FEV";
        }
        if(month == 3) {
            return "MAR";
        }
        if(month == 4) {
            return "ABR";
        }
        if(month == 5) {
            return "MAI";
        }
        if(month == 6) {
            return "JUN";
        }
        if(month == 7) {
            return "JUL";
        }
        if(month == 8) {
            return "AGO";
        }
        if(month == 9) {
            return "SET";
        }
        if(month == 10) {
            return "OUT";
        }
        if(month == 11) {
            return "NOV";
        }
        if(month == 12) {
            return "DEZ";
        }
        return "JAN";
    }

    public void openStartDatePicker(View view) {
        dateStartPickerDialog.show();
    }

    public void openEndDatePicker(View view) {
        dateEndPickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecyclerTransactions();
    }

    public void updateRecyclerTransactions() {
        transactionAdapter = new TransactionAdapter(transactionList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(transactionAdapter);
    }

    public void searchTransactions(View view) {
        String filter;

        if (isCredit.isChecked()) {
            filter = "credit";
        } else if (isDebit.isChecked()) {
            filter = "debit";
        } else {
            filter = "all";
        }

        transactionList = database.getTransactionsFilteredByTime(startFilterDate, endFilterDate, filter);
        updateRecyclerTransactions();
    }

    public void searchTransactions() {
        transactionList.clear();
        String filter;

        if (isCredit.isChecked()) {
            filter = "credit";
        } else if (isDebit.isChecked()) {
            filter = "debit";
        } else {
            filter = "all";
        }

        transactionList = database.getTransactionsFilteredByTime(startFilterDate, endFilterDate, filter);
        updateRecyclerTransactions();
    }
}