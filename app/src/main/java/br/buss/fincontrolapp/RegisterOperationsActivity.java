package br.buss.fincontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.buss.fincontrolapp.helpers.DBUtils;
import br.buss.fincontrolapp.helpers.FinControlDBOperations;
import br.buss.fincontrolapp.models.Transaction;
import br.buss.fincontrolapp.models.TransactionType;

public class RegisterOperationsActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private String formattedDBDate;
    private RadioButton isDebit;
    private RadioButton isCredit;
    private RadioGroup radioGroupRegister;
    private String isCreditDebit = "debit";
    private Spinner spinnerRegister;
    private FinControlDBOperations database;
    private EditText operationDescription;
    private EditText operationValue;
    private Integer transactionTypeId;
    List<TransactionType> transactionTypeList;
    Transaction transaction;
    Boolean existingTransaction = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_operations);
        if (existingTransaction) {
            setTitle("Editar operação");
        } else {
            setTitle("Nova operação");
        }
        Bundle bundle = getIntent().getExtras();

        spinnerRegister = findViewById(R.id.spinnerRegister);
        isDebit = findViewById(R.id.radioDebitRegister);
        isCredit = findViewById(R.id.radioCreditRegister);
        radioGroupRegister = findViewById(R.id.radioGroupRegister);

        operationDescription = findViewById(R.id.operationDesc);
        operationValue = findViewById(R.id.operationValue);

        dateButton = findViewById(R.id.datePickerRegister);
        dateButton.setText(getTodaysDate());
        formattedDBDate = DBUtils.getFormattedDate();

        radioGroupRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioDebitRegister) {
                    isCreditDebit = "debit";
                } else {
                    isCreditDebit = "credit";
                }
                transactionTypeList.clear();
                updateSpinner();
            }
        });

        database = new FinControlDBOperations(this);
        updateSpinner();

        initDatePicker();
        dateButton = findViewById(R.id.datePickerRegister);

        operationValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        if (bundle != null) {
            existingTransaction = true;
            transaction = (Transaction) bundle.getSerializable("TRANSACTION");
            dateButton.setText(transaction.getTime());
            operationDescription.setText(transaction.getDesc());
            operationValue.setText(String.valueOf(transaction.getValue()));
            if (transaction.getCredit()) {
                isDebit.setChecked(false);
                isCredit.setChecked(true);
            } else {
                isDebit.setChecked(true);
                isCredit.setChecked(false);
            }
        }

    }

    private void updateSpinner() {
        transactionTypeList = database.getAllTransactionTypes(isCreditDebit);

        ArrayAdapter<TransactionType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, transactionTypeList);
        spinnerRegister.setAdapter(adapter);
        spinnerRegister.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               transactionTypeId = transactionTypeList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                formattedDBDate = DBUtils.getFormattedDate(year, month, dayOfMonth);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void saveOperation(View view) {
        if (operationValue.length() == 0) {
            Toast.makeText(this, "Preencha o valor!", Toast.LENGTH_SHORT).show();
        } else {
            if (Double.parseDouble(operationValue.getText().toString()) == 0.0) {
                Toast.makeText(this, "Valor não pode ser igual a 0", Toast.LENGTH_SHORT).show();
            } else {
                if (!existingTransaction) {
                    database.insertNewTransaction(transactionTypeId, operationDescription.getText().toString(),
                            formattedDBDate, Double.parseDouble(operationValue.getText().toString()));
                } else {
                    database.updateTransaction(transactionTypeId, operationDescription.getText().toString(),
                            formattedDBDate, Double.parseDouble(operationValue.getText().toString()), transaction.getIdTransaction());
                }
                Toast.makeText(this, "Operação salva", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        /**
         * Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {

                // protects against many dots
                if (source.equals(".") || source.equals(","))
                {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }

    }


}

