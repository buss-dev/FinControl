package br.buss.fincontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.buss.fincontrolapp.R;
import br.buss.fincontrolapp.models.Profile;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private List<Profile> profiles;

    public ProfileAdapter(List<Profile> profileList) {
        profiles = profileList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView balanceView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.profileNameView);
            balanceView = itemView.findViewById(R.id.profileBalanceView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View transactionItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_cell, parent, false);
        return new ProfileAdapter.MyViewHolder(transactionItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.nameView.setText(profile.getName());
        holder.balanceView.setText(NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(profile.getBalance()));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }
}
