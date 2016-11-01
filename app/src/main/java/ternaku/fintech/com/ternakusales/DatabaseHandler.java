package ternaku.fintech.com.ternakusales;

/**
 * Created by YORIS on 10/4/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by YORIS on 9/29/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_ternaku_sales";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TBL_REMINDER = "CREATE TABLE HISTORY_PENJUALAN (ID_PENJUALAN INTEGER PRIMARY KEY , ID_PETERNAKAN TEXT, NAMA_PETERNAKAN TEXT, ALAMAT TEXT, LATITUDE TEXT, LONGITUDE TEXT, TELEPON TEXT, TANGGAL_TRANSAKSI TEXT, USERNAME TEXT, PASSWORD TEXT)";
        db.execSQL(CREATE_TBL_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "HISTORY_PENJUALAN");
        onCreate(db);
    }

    public void AddHistory(HistoryPenjualan penjualan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(false);
        ContentValues values = new ContentValues();
        values.put("ID_PETERNAKAN", penjualan.getId_peternakan()); // Contact Phone Number
        values.put("NAMA_PETERNAKAN", penjualan.getNama_peternakan());
        values.put("ALAMAT", penjualan.getAlamat());
        values.put("LATITUDE", penjualan.getLat());
        values.put("LONGITUDE", penjualan.getLng());
        values.put("TELEPON", penjualan.getTelpon());
        values.put("USERNAME", penjualan.getUsername());
        values.put("PASSWORD", penjualan.getPassword());

        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String CurrentDate = df1.format(new Date());
        values.put("TANGGAL_TRANSAKSI", CurrentDate);

        // Inserting Row
        db.insert("HISTORY_PENJUALAN", null, values);

        db.close(); // Closing database connection
    }

    public ArrayList<HistoryPenjualan> GetHistory() {
        ArrayList<HistoryPenjualan> reminderList = new ArrayList<HistoryPenjualan>();
        // Select All Query
        String selectQuery = "SELECT * FROM HISTORY_PENJUALAN ORDER BY TANGGAL_TRANSAKSI DESC";
        Log.d("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    HistoryPenjualan r = new HistoryPenjualan();
                    r.setId_peternakan(cursor.getString(cursor.getColumnIndex("ID_PETERNAKAN")));
                    r.setNama_peternakan(cursor.getString(cursor.getColumnIndex("NAMA_PETERNAKAN")));
                    r.setAlamat(cursor.getString(cursor.getColumnIndex("ALAMAT")));
                    r.setLat(cursor.getString(cursor.getColumnIndex("LATITUDE")));
                    r.setLng(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
                    r.setTelpon(cursor.getString(cursor.getColumnIndex("TELEPON")));
                    r.setTgl_transaksi(cursor.getString(cursor.getColumnIndex("TANGGAL_TRANSAKSI")));
                    r.setUsername(cursor.getString(cursor.getColumnIndex("USERNAME")));
                    r.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));


                    // Adding contact to list
                    reminderList.add(r);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return reminderList;
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE TBL_REMINDER");
    }

    public void DeleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TBL_REMINDER");
    }

    public void updateRead(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE TBL_REMINDER SET ISREAD=1 WHERE  ID_REMINDER='" + id +"'");
        db.close();
    }

}
