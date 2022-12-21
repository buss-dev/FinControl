package br.buss.fincontrolapp.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.buss.fincontrolapp.R;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;
import br.buss.fincontrolapp.models.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactions;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView transactionDesc;
        TextView transactionTypeDesc;
        TextView transactionValue;
        TextView transactionDate;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionDesc = itemView.findViewById(R.id.transaction_type_desc);
            transactionTypeDesc = itemView.findViewById(R.id.transaction_desc);
            transactionValue = itemView.findViewById(R.id.transaction_value);
            transactionDate = itemView.findViewById(R.id.transaction_date);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            position = getLayoutPosition();
            menu.add("Remover").setOnMenuItemClickListener(onClickMenu);
        }

        private final MenuItem.OnMenuItemClickListener onClickMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Remover")) {
                    FinControlDBOperations database = new FinControlDBOperations(itemView.getContext());
                    database.deleteTransaction(transactions.get(position).getIdTransaction());
                    transactions.remove(position);
                    Toast.makeText(itemView.getContext(), "Transação excluída", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                return false;
            }
        };


    }

    public TransactionAdapter(List<Transaction> list) {
        transactions = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View transactionItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_cell, parent, false);
        return new MyViewHolder(transactionItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.transactionDesc.setText(transaction.getDesc());
        holder.transactionTypeDesc.setText(transaction.getTypeDesc());
        holder.transactionDate.setText(transaction.getTime());

        if (transaction.getCredit()) {
            holder.transactionValue.setTextColor(holder.transactionValue.getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.transactionValue.setTextColor(holder.transactionValue.getResources().getColor(android.R.color.holo_red_light));
        }

        holder.transactionValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(transaction.getValue()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
