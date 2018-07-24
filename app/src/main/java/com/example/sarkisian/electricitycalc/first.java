package com.example.sarkisian.electricitycalc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;



public class first extends Activity {

    public final static String FILENAME = "sample.txt"; // имя файла
    private EditText mEditText;
    private EditText ShowHistory;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    ContentValues cv;
    final String LOG_TAG = "myLogs";
    long rowID;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        nameField = (EditText) findViewById(R.id.current);
        nameField2 = (EditText) findViewById(R.id.last);
        mEditText = (EditText) findViewById(R.id.editTextFirst);
        ShowHistory = (EditText) findViewById(R.id.editTextFirst);
        dbHelper = new DBHelper(this);

        cv = new ContentValues();
        db = dbHelper.getWritableDatabase();

    }


    public static EditText nameField;
    public static EditText nameField2;
    public static int difference = 0;
    public static double result2 = 0;
    public static double result3 = 0;
    public static double price = 0;
    public static String total;
    static int current;
    static int last;



    public void Calculate(View view) {
        String NF = nameField.getText().toString().trim();
        String NF2 = nameField2.getText().toString().trim();
        Dialog dialog = new Dialog(first.this);
        dialog.setTitle("Ошибка");
        dialog.setContentView(R.layout.dialog);

        TextView dialogText = (TextView) dialog.findViewById(R.id.dialogTextView);
        dialogText.setTextSize(30);

        TextView oplata = (TextView) findViewById(R.id.result);
        oplata.setTextSize(20);


        if (NF.isEmpty() || NF2.isEmpty()) {
            dialogText.setText("Введите показания!");
            dialog.show();
        }
        else if (NF.matches("^[A-Za-z]$") || NF2.matches("^[A-Za-z]$")) {
            dialogText.setText("Введите только цифры!");
            dialog.show();
        }
        else {
            int current = Integer.parseInt(NF);
            int last = Integer.parseInt(NF2);
            if (current < last) {
                dialogText.setText("Введите показания счетчика больше, чем предыдущие!");
                dialog.show();
            } else {
                difference = current - last;
                price = difference * (0.90);
                price = new BigDecimal(price).setScale(1, RoundingMode.UP).doubleValue();
                if (difference > 100) {
                    result2 = difference - 100;
                    result3 = new BigDecimal(result2 * 1.68).setScale(1, RoundingMode.UP).doubleValue() + 100 * 0.90;
                    oplata.setText(String.valueOf("Детальный рассчет: " + "\n" + "Разница: " + difference + "кВт⋅час" + "\n" + "100 кВт⋅час * 90 коп."
                            + "\n" + "+ " + result2 + " кВт⋅час * 168 коп." + "\n" + "\n" + "Расход электричества: " + "\n" + difference + " кВт*час" + "\n"
                            + "\n" + "Сумма к оплате: " + "\n" + (result3) + " грн"));
                    price = result3;
                } else {
                    oplata.setText(String.valueOf("Детальный рассчет: " + "\n" + "Разница: " + difference + " кВт⋅час" +
                            "\n" + difference + " кВт⋅час * 90 коп." + "\n" + "\n" + "Расход электричества: " + "\n" + difference + " кВт⋅час" +
                            "\n" + "\n" + "Сумма к оплате: " + "\n" + price + " грн"));

                }
            }
        }
    }

    public void Save(View view) {

        Dialog saveError = new Dialog(first.this);
        saveError.setTitle("Ошибка при сохранении");
        saveError.setContentView(R.layout.dialog);
        TextView saveErrorText = (TextView) saveError.findViewById(R.id.dialogTextView);
        saveErrorText.setTextSize(30);
        TextView two = (TextView) findViewById(R.id.textView2);
        if (!(nameField.length()==0 || nameField2.length()==0)) {
            current = Integer.parseInt(nameField.getText().toString());
            last = Integer.parseInt(nameField2.getText().toString());
            total = current + " " + last + " " + String.valueOf(difference) + " кВт*час " + price + " грн";
            two.setText(total);
        }
        else {
            saveErrorText.setText("Вы не ввели показания для сохранения!");
            saveError.show();
        }
        current = 0;
        last = 0;
    }


    public void savefile(View v) {

        Log.d(LOG_TAG, "--- Insert in mytable: ---");
        String current = nameField.getText().toString();
        String past = nameField2.getText().toString();
        cv.put("current", current);
        cv.put("past", past);
        cv.put("difference", difference);
        cv.put("price", price);
        rowID = db.insert("mytable1", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        dbHelper.close();
    }




    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB1", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable1 ("
                    + "id integer primary key autoincrement,"
                    + "current text,"
                    + "past text,"
                    + "difference text,"
                    + "price text" +");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }



}
