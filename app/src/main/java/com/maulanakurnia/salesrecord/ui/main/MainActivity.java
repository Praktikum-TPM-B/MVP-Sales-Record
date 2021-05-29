package com.maulanakurnia.salesrecord.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maulanakurnia.salesrecord.R;
import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.ui.SalesRecordAdapter;
import com.maulanakurnia.salesrecord.ui.form.FormActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
@SuppressLint({"StaticFieldLeak","InflateParams"})
public class MainActivity extends AppCompatActivity implements MainImpl.view {
    public static final String SALES_RECORD_ID = "SALES_RECORD_ID";

    protected RecyclerView recyclerView;
    protected SalesRecordAdapter salesRecordAdapter;
    protected AppDatabase appDatabase;
    protected MainPresenter mainPresenter;
    protected MaterialAlertDialogBuilder builder;
    protected SimpleDateFormat dateFormat;
    public static BottomSheetDialog bottomSheet;
    protected View bsView;
    protected SalesRecord salesRecord = new SalesRecord();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase     = AppDatabase.getDatabase(getApplicationContext());
        recyclerView    = findViewById(R.id.rv_sales_record);
        builder         = new MaterialAlertDialogBuilder(this, R.layout.dialog_question);
        dateFormat      = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
        bottomSheet     = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bsView          = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,null);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainPresenter = new MainPresenter(this);
        mainPresenter.read(appDatabase);

        FloatingActionButton addRecord = findViewById(R.id.fb_add);
        addRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void get(List<SalesRecord> list) {
        salesRecordAdapter = new SalesRecordAdapter(this, list, this);
        recyclerView.setAdapter(salesRecordAdapter);
    }

    @Override
    public void delete(SalesRecord list) {
        View dialog             = LayoutInflater.from(this).inflate(R.layout.dialog_question, findViewById(R.id.layout_dialog));
        builder.setView(dialog);
        AlertDialog alertDialog = builder.create();

        dialog.findViewById(R.id.dialog_action_cancel).setOnClickListener(v3 -> {
            alertDialog.dismiss();
            bottomSheet.show();
        });

        dialog.findViewById(R.id.dialog_action_delete).setOnClickListener(v4 -> {
            mainPresenter.delete(list.getId(), appDatabase);
            alertDialog.dismiss();
            bottomSheet.dismiss();
            refresh();
            Toast.makeText(this, "Berhasil menghapus catatan "+dateFormat.format(list.getDate()), Toast.LENGTH_SHORT).show();
        });

        if(alertDialog.getWindow() != null) { alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }
        alertDialog.show();
    }


    private void refresh() {
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }
}
