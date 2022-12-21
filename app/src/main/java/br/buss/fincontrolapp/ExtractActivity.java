package br.buss.fincontrolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.buss.fincontrolapp.adapter.TransactionAdapter;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;
import br.buss.fincontrolapp.models.Transaction;

public class ExtractActivity extends AppCompatActivity {

    RecyclerView recyclerViewTransactions;
    List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);
        setTitle("Extrato");

        FinControlDBOperations database = new FinControlDBOperations(this);

        recyclerViewTransactions = findViewById(R.id.recyclerViewExtract);
        TextView totalMoney = findViewById(R.id.totalMoneyView);

        totalMoney.setText(String.format("R$ %.2f", database.getValueEarned() - database.getValueSpent()));

        transactionList = database.getAllTransactions();

        recyclerViewTransactions.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                totalMoney.setText(String.format("R$ %.2f", database.getValueEarned() - database.getValueSpent()));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecyclerTransactions();
    }

    public void updateRecyclerTransactions() {
        TransactionAdapter transactionAdapter = new TransactionAdapter(transactionList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTransactions.setLayoutManager(layoutManager);
        recyclerViewTransactions.setHasFixedSize(true);
        recyclerViewTransactions.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerViewTransactions.setAdapter(transactionAdapter);
    }
}