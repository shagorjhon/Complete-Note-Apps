package com.example.input_show;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteapplication.Database;
import com.example.noteapplication.MainActivity;
import com.example.noteapplication.ModelClass;
import com.example.noteapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class InputActivity extends AppCompatActivity implements Serializable {
    ImageView imageView;
    EditText title,description;
    Button save;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    Database database;
    byte[] imageArray =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        imageView  =findViewById(R.id.imageViewID);
        title = findViewById(R.id.titleInputID);
        description=findViewById(R.id.descriptionInputID);
        save  =findViewById(R.id.saveBtnID);
        database = new Database(this);


        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getEditText(title,description);
                intentSend();
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
    }

    private void intentSend() {
        Intent intent = new Intent(InputActivity.this, MainActivity.class);
        intent.putExtra("key","data");
        startActivity(intent);
    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&& resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                imageArray = stream.toByteArray();

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getEditText(EditText title, EditText description) {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        String date = simpleDateFormat.format(calendar.getTime());
        String inputTitle = title.getText().toString();
        String inputDescription = description.getText().toString();

        ModelClass modelClass = new ModelClass(inputTitle,inputDescription,date,imageArray);
        Boolean insertData=database.insertData(modelClass);
        if (insertData)
        {
            Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show();
            this.finish();
        }else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }


        private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        // TODO Auto-generated method stub
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;

    }
}