package com.example.pemilubersama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Presiden1Activity extends AppCompatActivity {
    private TextView textViewNama, textViewAlamat, textViewTanggalLahir, textViewPilihanSaya;
    private DatabaseAcc dbHelper;

    public Button buttonkeluar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presiden1);
        dbHelper = new DatabaseAcc(this);

        // Inisialisasi elemen UI
        textViewNama = findViewById(R.id.textViewNama);
        textViewAlamat = findViewById(R.id.textViewAlamat);
        textViewTanggalLahir = findViewById(R.id.textViewTanggalLahir);
        textViewPilihanSaya = findViewById(R.id.textViewPilihanSaya);
        buttonkeluar = findViewById(R.id.buttonexit);
        // Ambil NIK dari SharedPreferences (simpan NIK saat login)
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String nikAkunLogin = sharedPreferences.getString("NIK_LOGIN", "");

        // Ambil data dari DatabaseAcc berdasarkan NIK akun yang login
        fetchDataFromDatabase(nikAkunLogin);

        // Set teks untuk pilihan Anda
        textViewPilihanSaya.setText("Pilihan Saya: Anies Baswedan dan Muhaimin Iskandar");
    }

    private void fetchDataFromDatabase(String nik) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseAcc.COLUMN_NIK,
                DatabaseAcc.COLUMN_FULL_NAME,
                DatabaseAcc.COLUMN_ADDRESS,
                DatabaseAcc.COLUMN_BIRTH_DATE
        };

        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {nik};

        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        buttonkeluar.setOnClickListener(view -> {
            finishAffinity(); // close the current activity
            System.exit(0); // exit the application
        });

        if (cursor.moveToNext()) {
            // Ambil data dari cursor
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_FULL_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ADDRESS));
            String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_BIRTH_DATE));

            // Tampilkan data ke dalam elemen UI
            textViewNama.setText("Nama : " + fullName);
            textViewAlamat.setText("Alamat : " + address);
            textViewTanggalLahir.setText("Tanggal Lahir : " + birthDate);
        }

        cursor.close();
        db.close();
    }

//public void keluar(){
//    finish();
//     System.exit(0);
}
