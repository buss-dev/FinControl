package br.buss.fincontrolapp.helpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.buss.fincontrolapp.SearchActivity;
import br.buss.fincontrolapp.database.FinControlDB;
import br.buss.fincontrolapp.models.Transaction;
import br.buss.fincontrolapp.models.TransactionType;

public class FinControlDBOperations {
    private SQLiteDatabase db;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<TransactionType> transactionTypes = new ArrayList<>();

    private static final String TRANSACTION_QUERY = "SELECT t." + FinControlDB.COL_ID_TRANSACTIONS
            + ", t." + FinControlDB.COL_DESC_TRANSACTIONS
            + ", t." + FinControlDB.COL_TIME_TRANSACTIONS
            + ", t." + FinControlDB.COL_VALUE_TRANSACTIONS
            + ",type." + FinControlDB.COL_ID_TRANSACTION_TYPE
            + ",type." + FinControlDB.COL_DESC_TRANSACTION_DEBIT
            + ",type." + FinControlDB.COL_DESC_TRANSACTION_TYPE
            + ",type." + FinControlDB.COL_DESC_TRANSACTION_CREDIT
            + " FROM " + FinControlDB.TABLE_NAME_3
            + " t INNER JOIN " + FinControlDB.TABLE_NAME_2
            + " type ON t." + FinControlDB.COL_ID_TRANSACTION_TYPE + " = type." + FinControlDB.COL_ID_TRANSACTION_TYPE;

    private static final String[] COLUMNS_TRANSACTION_TYPE = {FinControlDB.COL_ID_TRANSACTION_TYPE, FinControlDB.COL_DESC_TRANSACTION_TYPE,
            FinControlDB.COL_DESC_TRANSACTION_DEBIT, FinControlDB.COL_DESC_TRANSACTION_CREDIT};

    private static final String[] COLUMNS_TRANSACTION_OPERATIONS = {FinControlDB.COL_VALUE_TRANSACTIONS, FinControlDB.COL_TIME_TRANSACTIONS};

    public FinControlDBOperations (Context context) {
        FinControlDB finControlDB = new FinControlDB(context);
        db = finControlDB.getWritableDatabase();
    }

    @SuppressLint("Range")
    private Transaction parseTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_ID_TRANSACTIONS)));
        transaction.setDesc(cursor.getString(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTIONS)));
        transaction.setTypeDesc(cursor.getString(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_TYPE)));
        transaction.setTime(cursor.getString(cursor.getColumnIndex(FinControlDB.COL_TIME_TRANSACTIONS)));
        transaction.setCredit(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_CREDIT)));
        transaction.setDebit(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_DEBIT)));
        transaction.setValue(cursor.getDouble(cursor.getColumnIndex(FinControlDB.COL_VALUE_TRANSACTIONS)));
        return transaction;
    }

    @SuppressLint("Range")
    private TransactionType parseTransactionType(Cursor cursor) {
        TransactionType transactionType = new TransactionType();
        transactionType.setId(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_ID_TRANSACTION_TYPE)));
        transactionType.setDescription(cursor.getString(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_TYPE)));
        transactionType.setCredit(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_CREDIT)));
        transactionType.setDebit(cursor.getInt(cursor.getColumnIndex(FinControlDB.COL_DESC_TRANSACTION_DEBIT)));
        return transactionType;
    }

    public ArrayList<Transaction> getAllTransactions() {
        String joinSelect = "SELECT t." + FinControlDB.COL_ID_TRANSACTIONS
                + ", t." + FinControlDB.COL_DESC_TRANSACTIONS
                + ", t." + FinControlDB.COL_TIME_TRANSACTIONS
                + ", t." + FinControlDB.COL_VALUE_TRANSACTIONS
                + ",type." + FinControlDB.COL_ID_TRANSACTION_TYPE
                + ",type." + FinControlDB.COL_DESC_TRANSACTION_DEBIT
                + ",type." + FinControlDB.COL_DESC_TRANSACTION_TYPE
                + ",type." + FinControlDB.COL_DESC_TRANSACTION_CREDIT
                + " FROM " + FinControlDB.TABLE_NAME_3
                + " t INNER JOIN " + FinControlDB.TABLE_NAME_2
                + " type ON t." + FinControlDB.COL_ID_TRANSACTION_TYPE + " = type." + FinControlDB.COL_ID_TRANSACTION_TYPE
                + " ORDER BY t." + FinControlDB.COL_TIME_TRANSACTIONS + " DESC"
                + " LIMIT 15";
        Cursor cursor = db.rawQuery(joinSelect, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactions.add(parseTransaction(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return transactions;
    }

    public ArrayList<TransactionType> getAllTransactionTypes(String filter) {
        Cursor cursor = db.query(FinControlDB.TABLE_NAME_2, COLUMNS_TRANSACTION_TYPE,
                 filter.equals("credit") ? FinControlDB.COL_DESC_TRANSACTION_CREDIT + " = 1" :
                         FinControlDB.COL_DESC_TRANSACTION_DEBIT + " = 1"
                ,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactionTypes.add(parseTransactionType(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return transactionTypes;
    }

    public void insertNewTransaction(Integer transactionTypeId, String transactionDescription, String currentDate, Double value) {
        ContentValues cv = new ContentValues();
        cv.put(FinControlDB.COL_ID_TRANSACTION_TYPE, transactionTypeId);
        cv.put(FinControlDB.COL_DESC_TRANSACTIONS, transactionDescription);
        cv.put(FinControlDB.COL_TIME_TRANSACTIONS, currentDate);
        cv.put(FinControlDB.COL_VALUE_TRANSACTIONS, value);
        db.insert(FinControlDB.TABLE_NAME_3, null, cv);
    }

    public ArrayList<Transaction> getTransactionsFilteredByTime(String startDate, String endDate, String isCreditDebitAll) {
        ArrayList<Transaction> transactionsFiltered = new ArrayList<>();

        String joinSelect = TRANSACTION_QUERY
                + " WHERE t." + FinControlDB.COL_TIME_TRANSACTIONS + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                + " AND type." + (
                        isCreditDebitAll.equals("credit") ? FinControlDB.COL_DESC_TRANSACTION_CREDIT + " = " + 1 :
                                isCreditDebitAll.equals("debit") ? FinControlDB.COL_DESC_TRANSACTION_DEBIT + " = " + 1 :
                                        FinControlDB.COL_ID_TRANSACTION_TYPE + " > " + 0
                );
        Cursor cursor = db.rawQuery(joinSelect, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactionsFiltered.add(parseTransaction(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return transactionsFiltered;
    }

    public ArrayList<Transaction> getTransactionsFiltered(String isCreditDebit) {

        ArrayList<Transaction> transactionsFiltered = new ArrayList<>();

        String joinSelect = TRANSACTION_QUERY
                + " WHERE type." + (
                isCreditDebit.equals("credit") ? FinControlDB.COL_DESC_TRANSACTION_CREDIT + " = " + 1 :
                        FinControlDB.COL_DESC_TRANSACTION_DEBIT + " = " + 1
        );
        Cursor cursor = db.rawQuery(joinSelect, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactionsFiltered.add(parseTransaction(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return transactionsFiltered;

    }

    @SuppressLint("Range")
    public Double getValueSpent() {
        Double valueSpent = null;
        Cursor cursor = db.rawQuery("SELECT SUM(t." + FinControlDB.COL_VALUE_TRANSACTIONS
                + ") as SOMA FROM " + FinControlDB.TABLE_NAME_3
                + " t INNER JOIN " + FinControlDB.TABLE_NAME_2
                + " type ON t." + FinControlDB.COL_ID_TRANSACTION_TYPE + " = type." + FinControlDB.COL_ID_TRANSACTION_TYPE
                + " WHERE type." + FinControlDB.COL_DESC_TRANSACTION_DEBIT + " = 1"
                , null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            valueSpent = cursor.getDouble(cursor.getColumnIndex("SOMA"));
            cursor.moveToNext();
        }
        cursor.close();

        return valueSpent;
    }

    @SuppressLint("Range")
    public Double getValueEarned() {
        Double valueEarned = null;
        Cursor cursor = db.rawQuery("SELECT SUM(t." + FinControlDB.COL_VALUE_TRANSACTIONS
                        + ") as SOMA FROM " + FinControlDB.TABLE_NAME_3
                        + " t INNER JOIN " + FinControlDB.TABLE_NAME_2
                        + " type ON t." + FinControlDB.COL_ID_TRANSACTION_TYPE + " = type." + FinControlDB.COL_ID_TRANSACTION_TYPE
                        + " WHERE type." + FinControlDB.COL_DESC_TRANSACTION_CREDIT + " = 1"
                , null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            valueEarned = cursor.getDouble(cursor.getColumnIndex("SOMA"));
            cursor.moveToNext();
        }
        cursor.close();

        return valueEarned;
    }

    public void deleteTransaction(Integer id) {
        db.delete(FinControlDB.TABLE_NAME_3, FinControlDB.COL_ID_TRANSACTIONS + " = " + id, null);
    }

    public void updateTransaction(Integer transactionTypeId, String transactionDescription, String currentDate, Double value, Integer transactionId) {
        ContentValues cv = new ContentValues();
        cv.put(FinControlDB.COL_ID_TRANSACTION_TYPE, transactionTypeId);
        cv.put(FinControlDB.COL_DESC_TRANSACTIONS, transactionDescription);
        cv.put(FinControlDB.COL_TIME_TRANSACTIONS, currentDate);
        cv.put(FinControlDB.COL_VALUE_TRANSACTIONS, value);
        db.update(FinControlDB.TABLE_NAME_3, cv, FinControlDB.COL_ID_TRANSACTIONS + " = " + transactionId, null);
    }
}
