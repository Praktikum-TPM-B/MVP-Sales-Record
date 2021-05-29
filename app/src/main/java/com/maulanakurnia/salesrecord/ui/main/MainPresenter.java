package com.maulanakurnia.salesrecord.ui.main;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.maulanakurnia.salesrecord.data.AppDatabase;
import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
@SuppressLint("StaticFieldLeak")
public class MainPresenter implements MainImpl.presenter {

    private MainImpl.view view;

    public MainPresenter(MainImpl.view view) {
        this.view = view;
    }

    @Override
    public void read(AppDatabase appDatabase) {
        List<SalesRecord> list;
        list = appDatabase.salesRecordDao().getAll();
        view.get(list);
    }


    public class Delete extends AsyncTask<Void, Void, Long> {
        private final AppDatabase appDatabase;
        private final Integer id;

        public Delete(AppDatabase appDatabase, int id) {
            this.appDatabase = appDatabase;
            this.id = id;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            appDatabase.salesRecordDao().deleteById(id);
            return null;
        }

        protected void onPostExecute(Long along){
            super.onPostExecute(along);
        }
    }

    @Override
    public void delete(int id, AppDatabase appDatabase) {
        new Delete(appDatabase, id).execute();
    }
}
