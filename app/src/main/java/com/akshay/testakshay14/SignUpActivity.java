package com.akshay.testakshay14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] country = {"India", "USA", "China", "Japan", "Other"};

    EditText edFname, edLname, edEmail, edPass, edConPass;
    Spinner spinner;
    TextView tvDate;
    String strFname, strLname, strEmail, strPass, strConPass, strDate, strImge = "image", strItem, strGender;
    ImageView imageView;
    RadioGroup radioGroup;
    final Calendar myCalendar = Calendar.getInstance();
    private Bitmap bitmap;
    private Uri filePath;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        radioGroup = findViewById(R.id.rGrup);
        edFname = findViewById(R.id.edFname);
        edLname = findViewById(R.id.edLname);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        edConPass = findViewById(R.id.edConPass);
        tvDate = findViewById(R.id.tvDate);
        spinner = findViewById(R.id.sppiner);
        imageView = findViewById(R.id.image);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        updateLabel();
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(SignUpActivity.this);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String date = String.valueOf(sdf.format(myCalendar.getTime()));
        strDate = String.valueOf(date);
        Log.e("TAG", "updateLabel: " + date);
        tvDate.setText(date);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        try {
                            bitmap = (Bitmap) data.getExtras().get("data");

                            imageView.setImageBitmap(bitmap);
                            Log.d("TAG", "onActivityResult: " + bitmap);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            strImge = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            Log.d("TAG", ": " + strItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        filePath = data.getData();

                        Log.d("TAG", "filePath:a " + filePath);
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            imageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                            byte[] imageBytes = baos.toByteArray();

                            Log.d("TAG", "imageBytes: " + baos.toByteArray().toString());
                            strImge = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            Log.d("TAG", "encodedImage: a" + strImge);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void submitData() {
        strFname = edFname.getText().toString();
        strLname = edLname.getText().toString();
        strEmail = edEmail.getText().toString();
        strPass = edPass.getText().toString();
        strConPass = edConPass.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        strGender = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

        if (strFname.equals("")) {
            Toast.makeText(this, "Enter first Name", Toast.LENGTH_SHORT).show();
        } else if (strLname.equals("")) {
            Toast.makeText(this, "Enter last Name", Toast.LENGTH_SHORT).show();
        } else if (strEmail.equals("")) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (!strEmail.matches(emailPattern)) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
        } else if (strPass.equals("")) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else if (strConPass.equals("")) {
            Toast.makeText(this, "Re-enter password", Toast.LENGTH_SHORT).show();
        } else if (!strConPass.equals(strPass)) {
            Toast.makeText(this, "Password Not match", Toast.LENGTH_SHORT).show();
        } else if (strImge.equals("image")) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else {
            if (db.addContact(new Contact(strFname, strLname, strEmail, strGender, strDate, strItem, strImge, strPass))) {
                Toast.makeText(this, "Insert Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Not Inserted", Toast.LENGTH_SHORT).show();
            }
            Log.e("TAG", "submitData: " + strFname + strLname + strEmail + strGender + strDate + strItem + strImge + strPass);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Toast.makeText(getApplicationContext(),country[i] , Toast.LENGTH_LONG).show();
        strItem = country[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}