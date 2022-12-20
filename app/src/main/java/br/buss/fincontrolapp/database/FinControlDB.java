package br.buss.fincontrolapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FinControlDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "FinControl.db";
    private static final int DB_VERSION = 5;

    // Table names
    public static final String TABLE_NAME_1 = "user_fincontrol";
    public static final String TABLE_NAME_2 = "transaction_type";
    public static final String TABLE_NAME_3 = "transaction_fincontrol";

    // Column names for each table
    public static final String COL_ID_USER = "user_id";
    public static final String COL_NAME_USER = "user_fullname";

    public static final String COL_ID_TRANSACTION_TYPE = "transaction_type_id";
    public static final String COL_DESC_TRANSACTION_TYPE = "transaction_type_description";
    public static final String COL_DESC_TRANSACTION_DEBIT = "transaction_type_is_credit";
    public static final String COL_DESC_TRANSACTION_CREDIT = "transaction_type_is_debit";

    public static final String COL_ID_TRANSACTIONS = "transaction_id";
    public static final String COL_DESC_TRANSACTIONS = "transaction_description";
    public static final String COL_TIME_TRANSACTIONS = "transaction_time";
    public static final String COL_VALUE_TRANSACTIONS = "transaction_value";

    // SQL statement to create table 1
    private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME_1 + "("
            + COL_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME_USER + " TEXT "
            + ")";

    // SQL statement to create table 2
    private static final String SQL_CREATE_TABLE_TRANSACTION_TYPE = "CREATE TABLE " + TABLE_NAME_2 + "("
            + COL_ID_TRANSACTION_TYPE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DESC_TRANSACTION_TYPE + " TEXT, "
            + COL_DESC_TRANSACTION_DEBIT + " BOOLEAN, "
            + COL_DESC_TRANSACTION_CREDIT + " BOOLEAN "
            + ")";

    // SQL statement to create table 3
    private static final String SQL_CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_NAME_3 + "("
            + COL_ID_TRANSACTIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DESC_TRANSACTIONS + " TEXT, "
            + COL_ID_TRANSACTION_TYPE + " INTEGER, "
            + COL_VALUE_TRANSACTIONS + " REAL, "
            + COL_TIME_TRANSACTIONS + " TEXT, " //YYYY-MM-DD
            + COL_ID_USER + " INTEGER, "
            + "CONSTRAINT user_transaction_fk FOREIGN KEY (" + COL_ID_USER + ") references " + TABLE_NAME_1 + "(" + COL_ID_USER + "), "
            + "CONSTRAINT transaction_type_fk FOREIGN KEY (" + COL_ID_TRANSACTION_TYPE + ") references " + TABLE_NAME_2 + "(" + COL_ID_TRANSACTION_TYPE + ")"
            + ")";

    public FinControlDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_TRANSACTION_TYPE);
        db.execSQL(SQL_CREATE_TABLE_TRANSACTIONS);
        defaultDebitInserts(db);
        defaultCreditInserts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    private void defaultDebitInserts(SQLiteDatabase db) {
        ArrayList<String> debits = new ArrayList<String>();
        debits.add("Moradia");
        debits.add("Saúde");
        debits.add("Outros");

        ContentValues cvDebits = new ContentValues();
        cvDebits.put(COL_DESC_TRANSACTION_DEBIT, Boolean.TRUE);
        cvDebits.put(COL_DESC_TRANSACTION_CREDIT, Boolean.FALSE);
        for (String debit:
                debits) {
            cvDebits.put(COL_DESC_TRANSACTION_TYPE, debit);
            db.insert(TABLE_NAME_2, null, cvDebits);
        }
    }

    private void defaultCreditInserts(SQLiteDatabase db) {
        ArrayList<String> credits = new ArrayList<String>();
        credits.add("Salário");
        credits.add("Outros");

        ContentValues cvCredits = new ContentValues();
        cvCredits.put(COL_DESC_TRANSACTION_DEBIT, Boolean.FALSE);
        cvCredits.put(COL_DESC_TRANSACTION_CREDIT, Boolean.TRUE);
        for (String credit:
                credits) {
            cvCredits.put(COL_DESC_TRANSACTION_TYPE, credit);
            db.insert(TABLE_NAME_2, null, cvCredits);
        }
    }

}
