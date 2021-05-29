package com.maulanakurnia.salesrecord.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maulanakurnia.salesrecord.data.model.SalesRecord;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/29/2021
 * Keep Coding & Stay Awesome!
 **/
@Dao
public interface SalesRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(SalesRecord salesRecord);

    @Query("SELECT * FROM sales_record ORDER BY date ASC")
    List<SalesRecord> getAll();

    @Query("SELECT * FROM sales_record WHERE id = :id")
    SalesRecord get(int id);

    @Update
    Integer update(SalesRecord salesRecord);

    @Query("DELETE FROM sales_record WHERE id = :id")
    void deleteById(long id);

    @Query("DELETE FROM sales_record")
    void deleteAll();
}
