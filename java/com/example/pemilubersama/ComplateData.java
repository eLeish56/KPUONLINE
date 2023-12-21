package com.example.pemilubersama;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

public class ComplateData extends AppCompatActivity {
    private EditText editTextNik, editTextFullName, editTextOrigin, editTextBirthDate,
            editTextAddress, editTextOccupation, editTextPhoneNumber;
    private Spinner spinnerGender;
    private Button buttonSaveData;

    private DatabaseAcc dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_data);


        dbHelper = new DatabaseAcc(this);

        // Inisialisasi elemen UI
        editTextNik = findViewById(R.id.editTextNik);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextOrigin = findViewById(R.id.editTextOrigin);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSaveData = findViewById(R.id.buttonSaveData);

        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDatabase();
            }
        });

        dbHelper = new DatabaseAcc(this);

        // Ambil NIK dari Intent
        Intent intent = getIntent();
        if (intent.hasExtra("NIK_TO_DISPLAY")) {
            String nikToDisplay = intent.getStringExtra("NIK_TO_DISPLAY");

            // Tampilkan data dari DatabaseAcc
            displayDataFromDatabase(nikToDisplay);
        }

    }

    private void displayDataFromDatabase(String nikToDisplay) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseAcc.COLUMN_NIK,
                DatabaseAcc.COLUMN_FULL_NAME,
                DatabaseAcc.COLUMN_ORIGIN,
                DatabaseAcc.COLUMN_BIRTH_DATE,
                DatabaseAcc.COLUMN_ADDRESS,
                DatabaseAcc.COLUMN_OCCUPATION,
                DatabaseAcc.COLUMN_GENDER,
                DatabaseAcc.COLUMN_PHONE_NUMBER
        };

        String selection = DatabaseAcc.COLUMN_NIK + " = ?";
        String[] selectionArgs = {nikToDisplay};

        Cursor cursor = db.query(
                DatabaseAcc.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            // Ambil data dari cursor
            int fullNameIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_FULL_NAME);
            String fullName = cursor.getString(fullNameIndex);

            int originIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ORIGIN);
            String origin = cursor.getString(originIndex);

            int birthDateIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_BIRTH_DATE);
            String birthDate = cursor.getString(birthDateIndex);

            int addressIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_ADDRESS);
            String address = cursor.getString(addressIndex);

            int occupationIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_OCCUPATION);
            String occupation = cursor.getString(occupationIndex);

            int genderIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_GENDER);
            String gender = cursor.getString(genderIndex);

            int phoneNumberIndex = cursor.getColumnIndexOrThrow(DatabaseAcc.COLUMN_PHONE_NUMBER);
            String phoneNumber = cursor.getString(phoneNumberIndex);
            // Tampilkan data ke dalam elemen UI
            editTextNik.setText(nikToDisplay);
            editTextFullName.setText(fullName);
            editTextOrigin.setText(origin);
            editTextBirthDate.setText(birthDate);
            editTextAddress.setText(address);
            editTextOccupation.setText(occupation);

            // Set selected item pada Spinner sesuai dengan data dari database
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.gender_array,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGender.setAdapter(adapter);
            int position = adapter.getPosition(gender);
            spinnerGender.setSelection(position);

            editTextPhoneNumber.setText(phoneNumber);
        }

        cursor.close();
        db.close();
    }

    private void saveDataToDatabase() {
        // Ambil nilai dari elemen UI
        String nik = editTextNik.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String origin = editTextOrigin.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();
        String address = editTextAddress.getText().toString();
        String occupation = editTextOccupation.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        // Buka koneksi ke database untuk menulis
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Cek apakah data dengan NIK tertentu sudah ada dalam database
        String[] projection = {DatabaseAcc.COLUMN_NIK};
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

        // Jika data dengan NIK tertentu sudah ada, perbarui data
        if (cursor.moveToFirst()) {
            ContentValues updateValues = new ContentValues();
            updateValues.put(DatabaseAcc.COLUMN_FULL_NAME, fullName);
            updateValues.put(DatabaseAcc.COLUMN_ORIGIN, origin);
            updateValues.put(DatabaseAcc.COLUMN_BIRTH_DATE, birthDate);
            updateValues.put(DatabaseAcc.COLUMN_ADDRESS, address);
            updateValues.put(DatabaseAcc.COLUMN_OCCUPATION, occupation);
            updateValues.put(DatabaseAcc.COLUMN_PHONE_NUMBER, phoneNumber);
            updateValues.put(DatabaseAcc.COLUMN_GENDER, gender);

            int rowsAffected = db.update(
                    DatabaseAcc.TABLE_NAME,
                    updateValues,
                    selection,
                    selectionArgs
            );

            if (rowsAffected > 0) {
                // Data berhasil diperbarui
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                // Terjadi kesalahan saat memperbarui data
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Jika data dengan NIK tertentu tidak ada, simpan data baru
            ContentValues insertValues = new ContentValues();
            insertValues.put(DatabaseAcc.COLUMN_NIK, nik);
            insertValues.put(DatabaseAcc.COLUMN_FULL_NAME, fullName);
            insertValues.put(DatabaseAcc.COLUMN_ORIGIN, origin);
            insertValues.put(DatabaseAcc.COLUMN_BIRTH_DATE, birthDate);
            insertValues.put(DatabaseAcc.COLUMN_ADDRESS, address);
            insertValues.put(DatabaseAcc.COLUMN_OCCUPATION, occupation);
            insertValues.put(DatabaseAcc.COLUMN_PHONE_NUMBER, phoneNumber);
            insertValues.put(DatabaseAcc.COLUMN_GENDER, gender);

            long newRowId = db.insert(DatabaseAcc.TABLE_NAME, null, insertValues);

            if (newRowId != -1) {
                // Data baru berhasil disimpan
                Toast.makeText(this, "Data baru berhasil disimpan", Toast.LENGTH_SHORT).show();
            } else {
                // Terjadi kesalahan saat menyimpan data baru
                Toast.makeText(this, "Gagal menyimpan data baru", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
        db.close();

        // Setelah data disimpan atau diperbarui, Anda dapat mengarahkan pengguna ke layar dashboard atau lainnya
        Intent intent = new Intent(ComplateData.this, DashBoard.class);
        startActivity(intent);

        // Finish activity agar tidak dapat kembali menggunakan tombol back
        finish();
    }
}