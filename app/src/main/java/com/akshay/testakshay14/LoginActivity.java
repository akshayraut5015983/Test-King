package com.akshay.testakshay14;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPass;
    String strEmail, strPass;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = new DatabaseHandler(this);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = edEmail.getText().toString();
                strPass = edPass.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (strEmail.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!strEmail.matches(emailPattern)) {
                    Toast.makeText(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    validateData(strEmail, strPass);
                }
            }
        });
        findViewById(R.id.btnReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }

    private void validateData(String strEmail, String strPass) {
        Cursor ssss = db.getContact(strEmail, strPass);
        if (ssss.getCount() >= 1) {
            if (strPass.equals(ssss.getString(ssss.getColumnIndex("pass")))) {

                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashbordActivity.class);
                intent.putExtra("key1", ssss.getString(ssss.getColumnIndex("fName")));
                intent.putExtra("key2", ssss.getString(ssss.getColumnIndex("lName")));
                intent.putExtra("key3", ssss.getString(ssss.getColumnIndex("email")));
                intent.putExtra("key4", ssss.getString(ssss.getColumnIndex("gender")));
                intent.putExtra("key5", ssss.getString(ssss.getColumnIndex("dateofb")));
                intent.putExtra("key6", ssss.getString(ssss.getColumnIndex("locCity")));
                intent.putExtra("key7", ssss.getString(ssss.getColumnIndex("image")));
                intent.putExtra("key8", ssss.getString(ssss.getColumnIndex("pass")));
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Password Not Match", Toast.LENGTH_SHORT).show();
            }
            Log.e("TAG", "validateDataif: " + ssss.getString(ssss.getColumnIndex("email")));

        } else {
            Toast.makeText(this, "Data Not Available", Toast.LENGTH_SHORT).show();

        }
        // Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));

        Log.e("TAG", "validateData: " + ssss.getCount());

    }
}