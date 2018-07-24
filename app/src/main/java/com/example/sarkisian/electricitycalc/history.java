package com.example.sarkisian.electricitycalc;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.example.sarkisian.electricitycalc.first.dbHelper;


public class history extends first {

    public static TableLayout table;
    int clearCount = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);


        table = new TableLayout(this);
        table = (TableLayout) findViewById(R.id.tableTable);

        first.db = dbHelper.getWritableDatabase();
        first.db.execSQL("create table if not exists mytable1 ("
                + "id integer primary key autoincrement,"
                + "current text,"
                + "past text,"
                + "difference integer,"
                + "price integer" +");");
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        Cursor c = first.db.query("mytable1", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int currentIndex = c.getColumnIndex("current");
            int pastIndex = c.getColumnIndex("past");
            int differenceIndex = c.getColumnIndex("difference");
            int priceIndex = c.getColumnIndex("price");

            do {
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", current = " + c.getString(currentIndex) +
                                ", past = " + c.getString(pastIndex) +
                                ", difference = " + c.getString(differenceIndex) +
                                ", price = " + c.getString(priceIndex));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

                TableRow Row1 = new TableRow(this);
                TextView textViewID = new TextView(this);
                TextView textViewCurrent = new TextView(this);
                TextView textViewPast = new TextView(this);
                TextView textViewDifference = new TextView(this);
                TextView textViewPrice = new TextView(this);
                textViewID.setText(c.getString(idColIndex));
                textViewCurrent.setText(c.getString(currentIndex));
                textViewPast.setText(c.getString(pastIndex));
                textViewDifference.setText(c.getString(differenceIndex));
                textViewPrice.setText(c.getString(priceIndex));
                Row1.addView(textViewID);
                Row1.addView(textViewCurrent);
                Row1.addView(textViewPast);
                Row1.addView(textViewDifference);
                Row1.addView(textViewPrice);
                table.addView(Row1);
                //setContentView(table);

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        dbHelper.close();

    }

    private void openFile(String fileName) {

    }

    public void open(View v) {
        openFile(first.FILENAME);
    }

    public void clear_history(View v) {
        Log.d(LOG_TAG, "--- Clear mytable1: ---");
        // удаляем все записи
        first.db = dbHelper.getWritableDatabase();
        clearCount = first.db.delete("mytable1", null, null);
        first.db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = " + "'mytable1'");
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        dbHelper.close();

    }

}
