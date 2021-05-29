package com.maulanakurnia.salesrecord.ui.main;

import android.view.View;

import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
public interface MainImpl {
    interface view  {
        void get(List<SalesRecord> list);
        void delete(SalesRecord item);
    }

    interface presenter {
        void read(AppDatabase appDatabase);
        void delete(int id, AppDatabase appDatabase);
    }
}
