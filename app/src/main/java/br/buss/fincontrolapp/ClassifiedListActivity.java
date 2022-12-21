package br.buss.fincontrolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.buss.fincontrolapp.adapter.TransactionAdapter;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;
import br.buss.fincontrolapp.models.Transaction;

public class ClassifiedListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCredit;
    private RecyclerView recyclerViewDebit;
    private List<Transaction> debitTransactions;
    private List<Transaction> creditTransactions;
    private FinControlDBOperations database;
    private TextView debitValue;
    private TextView creditValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified_list);
        setTitle("Lista Classificada");

        recyclerViewCredit = findViewById(R.id.recyclerCredit);
        recyclerViewDebit = findViewById(R.id.recyclerDebit);

        debitValue = findViewById(R.id.debitValueClassified);
        creditValue = findViewById(R.id.creditValueClassified);

        database = new FinControlDBOperations(this);

        creditValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(database.getValueEarned()));
        debitValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(database.getValueSpent()));

        debitTransactions = database.getTransactionsFiltered("debit");
        creditTransactions = database.getTransactionsFiltered("credit");

        recyclerViewCredit.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                creditValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(database.getValueEarned()));
            }
        });

        recyclerViewDebit.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                debitValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(database.getValueSpent()));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecyclerDebitTransactions();
        updateRecyclerCreditTransactions();
    }

    public void updateRecyclerDebitTransactions() {
        TransactionAdapter transactionAdapter = new TransactionAdapter(debitTransactions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDebit.setLayoutManager(layoutManager);
        recyclerViewDebit.setHasFixedSize(true);
        recyclerViewDebit.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerViewDebit.setAdapter(transactionAdapter);
    }

    public void updateRecyclerCreditTransactions() {
        TransactionAdapter transactionAdapter = new TransactionAdapter(creditTransactions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCredit.setLayoutManager(layoutManager);
        recyclerViewCredit.setHasFixedSize(true);
        recyclerViewCredit.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerViewCredit.setAdapter(transactionAdapter);
    }
}