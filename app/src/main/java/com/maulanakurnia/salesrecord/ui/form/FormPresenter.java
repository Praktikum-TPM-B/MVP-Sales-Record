package com.maulanakurnia.salesrecord.ui.form;

import android.os.AsyncTask;
import android.util.Log;

import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;
import com.maulanakurnia.salesrecord.ui.main.MainImpl;

import java.util.Date;
import java.util.List;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
public class FormPresenter implements FormImpl.presenter {
    private final FormImpl.view view;

    public FormPresenter(FormImpl.view view) {
        this.view = view;
    }

    public class Insert extends AsyncTask<Void, Void, Long> {
        private final AppDatabase appDatabase;
        private final SalesRecord salesRecord;

        public Insert(AppDatabase appDatabase, SalesRecord salesRecord) {
            this.appDatabase = appDatabase;
            this.salesRecord = salesRecord;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return appDatabase.salesRecordDao().insert(salesRecord);
        }

        protected void onPostExecute(Long along){
            super.onPostExecute(along);
        }
    }

    @Override
    public void get(int id, AppDatabase appDatabase) {
        SalesRecord item;
        item = appDatabase.salesRecordDao().get(id);
        view.get(item);
    }

    @Override
    public void insert(Date date, Double grossProfit, Double expenditure, Double netGross, AppDatabase appDatabase) {
        final SalesRecord salesRecord = new SalesRecord();
        salesRecord.setDate(date);
        salesRecord.setGross_profit(grossProfit);
        salesRecord.setExpenditure(expenditure);
        salesRecord.setNet_gross(netGross);
        new Insert(appDatabase, salesRecord).execute();
    }

    public class Update extends AsyncTask<Void, Void, Integer> {
        private final AppDatabase appDatabase;
        private final SalesRecord salesRecord;

        public Update(AppDatabase appDatabase, SalesRecord salesRecord) {
            this.appDatabase = appDatabase;
            this.salesRecord = salesRecord;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return appDatabase.salesRecordDao().update(salesRecord);
        }

        protected void onPostExecute(Integer integer){
            super.onPostExecute(integer);
        }
    }

    @Override
    public void update(Date date, Double grossProfit, Double expenditure, Double netGross, int id, AppDatabase appDatabase) {
        final SalesRecord salesRecord = new SalesRecord();
        salesRecord.setDate(date);
        salesRecord.setGross_profit(grossProfit);
        salesRecord.setExpenditure(expenditure);
        salesRecord.setNet_gross(netGross);
        salesRecord.setId(id);
        new Update(appDatabase, salesRecord).execute();
    }
}
