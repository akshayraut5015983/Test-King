package com.akshay.testakshay14;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashbordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] country = {"India", "USA", "China", "Japan", "Other"};

    String strFname, strLname, strEmail, strPass, strConPass, strDate, strImge = "image", strItem, strGender;
    EditText edFname, edLname, edEmail, edPass, edConPass;
    Spinner spinner;
    TextView tvDate;
    ImageView imageView;
    RadioGroup radioGroup;
    final Calendar myCalendar = Calendar.getInstance();
    private Bitmap bitmap;
    private Uri filePath;
    DatabaseHandler db;
    RadioButton rbMale, rbFemaile;
    Button btnSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);
        db = new DatabaseHandler(this);
        radioGroup = findViewById(R.id.rGrup);
        edFname = findViewById(R.id.edFname);
        edLname = findViewById(R.id.edLname);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        edConPass = findViewById(R.id.edConPass);
        tvDate = findViewById(R.id.tvDate);
        btnSub = findViewById(R.id.btnSubmit);
        spinner = findViewById(R.id.sppiner);
        imageView = findViewById(R.id.image);
        rbFemaile = findViewById(R.id.rdFemale);
        rbMale = findViewById(R.id.rdMale);

        spinner.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            strFname = intent.getExtras().getString("key1");
            strLname = intent.getExtras().getString("key2");
            strEmail = intent.getExtras().getString("key3");
            strGender = intent.getExtras().getString("key4");
            strDate = intent.getExtras().getString("key5");
            strItem = intent.getExtras().getString("key6");
            strImge = intent.getExtras().getString("key7");
            strPass = intent.getExtras().getString("key8");

            edFname.setText(strFname);
            edLname.setText(strLname);
            edEmail.setText(strEmail);
            edPass.setText(strPass);
            tvDate.setText(strDate);
            Log.e("TAG", "onCreate: " + strItem);
            byte[] decodedString = Base64.decode(strImge, Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
        edFname.setFocusableInTouchMode(false);
        edLname.setFocusableInTouchMode(false);
        edEmail.setFocusableInTouchMode(false);
        edPass.setFocusableInTouchMode(false);
        edConPass.setFocusableInTouchMode(false);
        tvDate.setClickable(false);

        if (strGender.equals("Male")) {
            ((RadioButton) radioGroup.findViewById(R.id.rdMale)).setChecked(true);

        } else {
            ((RadioButton) radioGroup.findViewById(R.id.rdFemale)).setChecked(true);

        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setSelection(aa.getPosition(String.valueOf(strItem)));

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
        // updateLabel();
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DashbordActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(DashbordActivity.this);
            }
        });
    }

    private void updateData() {
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
            int val = db.updateContact(new Contact(strFname, strLname, strEmail, strGender, strDate, strItem, strImge, strPass));
            if (val == 1) {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                btnSub.setVisibility(View.GONE);
                edPass.setVisibility(View.GONE);
                edConPass.setVisibility(View.GONE);
                edFname.setFocusableInTouchMode(false);
                edLname.setFocusableInTouchMode(false);
                edEmail.setFocusableInTouchMode(false);
                edPass.setFocusableInTouchMode(false);
                edConPass.setFocusableInTouchMode(false);
                tvDate.setClickable(false);
            }else {
                Toast.makeText(this, "Not updated", Toast.LENGTH_SHORT).show();
            }
            Log.e("TAG", "updateData: " + val);
            Log.e("TAG", "submitData: " + strFname + strLname + strEmail + strGender + strDate + strItem + strImge + strPass);
        }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Toast.makeText(getApplicationContext(),country[i] , Toast.LENGTH_LONG).show();
        strItem = country[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                editCalll();
                return true;
            case R.id.item2:
                startActivity(new Intent(DashbordActivity.this, LoginActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editCalll() {
        edFname.setFocusableInTouchMode(true);
        edLname.setFocusableInTouchMode(true);
        edEmail.setFocusableInTouchMode(false);
        edPass.setFocusableInTouchMode(true);
        edConPass.setFocusableInTouchMode(true);
        edPass.setVisibility(View.VISIBLE);
        edConPass.setVisibility(View.VISIBLE);
        tvDate.setClickable(true);
        btnSub.setVisibility(View.VISIBLE);
    }
}