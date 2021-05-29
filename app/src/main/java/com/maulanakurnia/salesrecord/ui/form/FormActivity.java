package com.maulanakurnia.salesrecord.ui.form;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.ui.main.MainActivity;
import com.maulanakurnia.salesrecord.utils.Currency;
import com.maulanakurnia.salesrecord.utils.DateTypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
@SuppressLint({"SetTextI18n","NonConstantResourceId"})
public class FormActivity extends AppCompatActivity implements FormImpl.view {
    private int salesRecordID    = 0;
    private boolean isEdit       = false;

    private EditText dateInput, grossprovitInput, expenditureInput;
    private Button submit;
    protected TextView title;
    private LinearLayout back;

    private SimpleDateFormat dateFormat;
    private MaterialDatePicker<Long> datePicker;
    private FormPresenter formPresenter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        dateInput               = findViewById(R.id.et_date);
        grossprovitInput        = findViewById(R.id.et_gross_provit);
        expenditureInput        = findViewById(R.id.et_expenditure);
        submit                  = findViewById(R.id.btn_submit);
        title                   = findViewById(R.id.form_title);
        back                    = findViewById(R.id.back);
        appDatabase             = AppDatabase.getDatabase(getApplicationContext());
        formPresenter           = new FormPresenter(this);
        dateFormat              = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
        datePicker              = MaterialDatePicker.Builder.datePicker()
                                  .setTitleText("Pilih Tanggal")
                                  .setCalendarConstraints(new CalendarConstraints.Builder().setOpenAt(Calendar.getInstance().getTime().getTime()).build())
                                  .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                  .build();

        if(!isEdit) {
            datePicker.show(getSupportFragmentManager(), datePicker.getTag());
            datePicker.dismiss();
            dateInput.setText(dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));
        }

        dateInput.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), datePicker.getTag());
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            dateInput.setText(dateFormat.format(datePicker.getSelection()));
        });

        grossprovitInput.addTextChangedListener(new Currency(grossprovitInput));
        expenditureInput.addTextChangedListener(new Currency(expenditureInput));

        if(getIntent().hasExtra(MainActivity.SALES_RECORD_ID)) {
            salesRecordID = getIntent().getIntExtra(MainActivity.SALES_RECORD_ID,0);
            formPresenter.get(salesRecordID, appDatabase);
            isEdit = true;
            submit.setText("UBAH");
            title.setText("Ubah Catatan");
        }
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void get(SalesRecord item) {
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setCalendarConstraints(new CalendarConstraints.Builder().setOpenAt(item.getDate().getTime()).build())
                .setSelection(DateTypeConverter.fromDate(item.getDate())).build();
        datePicker.show(getSupportFragmentManager(), datePicker.getTag());
        datePicker.dismiss();
        dateInput.setText(dateFormat.format(item.getDate()));
        grossprovitInput.setText(item.getGross_profit().toString());
        expenditureInput.setText(item.getExpenditure().toString());
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                int id                  = salesRecordID;
                String grossProfit      = grossprovitInput.getText().toString();
                String expenditure      = expenditureInput.getText().toString();
                String netGross         = String.valueOf(Double.parseDouble(Currency.trimComma(grossProfit)) - Double.parseDouble(Currency.trimComma(expenditure)));

                if(!isEdit) {
                    try {
                        formPresenter.insert(
                                DateTypeConverter.toDate(datePicker.getSelection()),
                                Double.parseDouble(Currency.trimComma(expenditure)),
                                Double.parseDouble(Currency.trimComma(grossProfit)),
                                Double.parseDouble(Currency.trimComma(netGross)),
                                appDatabase
                        );

                        finish();
                    }catch (Exception err) {
                        Log.i("EXCEPTION", "ERROR: "+ err);
                    }
                }
                else {
                    try{
                        formPresenter.update(
                                DateTypeConverter.toDate((Long) datePicker.getSelection()),
                                Double.parseDouble(Currency.trimComma(grossProfit)),
                                Double.parseDouble(Currency.trimComma(expenditure)),
                                Double.parseDouble(netGross),
                                id,
                                appDatabase
                        );

                        isEdit = false;
                        finish();
                    }catch (NullPointerException err) {
                        Log.i("EXCETION", "ERRR: "+err);
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
