package br.buss.fincontrolapp.models;

import androidx.annotation.NonNull;

public class TransactionType {
    private Integer id;
    private Boolean isDebit;
    private Boolean isCredit;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDebit() {
        return isDebit;
    }

    public void setDebit(Integer debit) {
        isDebit = debit == 1;
    }

    public Boolean getCredit() {
        return isCredit;
    }

    public void setCredit(Integer credit) {
        isCredit = credit == 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return this.description;
    }
}
