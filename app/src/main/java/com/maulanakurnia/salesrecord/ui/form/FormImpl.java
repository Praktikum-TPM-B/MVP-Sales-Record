package com.maulanakurnia.salesrecord.ui.form;

import android.view.View;

import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.Date;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
public interface FormImpl {
    interface view extends View.OnClickListener {
        void get(SalesRecord item);
    }
    interface presenter {
        void get(int id, AppDatabase appDatabase);
        void insert(Date date, Double grossProfit, Double expenditure, Double netGross, AppDatabase appDatabase);
        void update(Date date, Double grossProfit, Double expenditure, Double netGross, int id, AppDatabase appDatabase);
    }
}
