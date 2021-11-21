package com.example.input_show;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapplication.Database;
import com.example.noteapplication.ModelClass;
import com.example.noteapplication.R;

import org.json.JSONObject;

import java.io.Serializable;

public class ShowActivity extends AppCompatActivity implements Serializable {
    ImageView imageView;
    TextView title,description;
    EditText titleEdit,descriptionEdit;
    Database database;
    ImageButton backButton,editButton,saveEditedText,cancelButton;
    ModelClass modelClass;
    CardView cardView;
    ImageButton boldText;
    Spannable spannable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        findAllItem();
        database=new Database(this);
        findAndHideEditText();

        modelClass = (ModelClass) getIntent().getSerializableExtra("data");
        int position = getIntent().getIntExtra("position",0);


        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeByteArray(database.getData().get(position).getImage(),0,
                    database.getData().get(position).getImage().length);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        imageView.setImageBitmap(bm);
        title.setText(modelClass.getTitle());
        description.setText(Html.fromHtml(modelClass.getDescription()));
        //description.setText(modelClass.getDescription());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonClick();
                editTextSetData();
                cardView.setVisibility(View.VISIBLE);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whenCancelButtonClick();
                cardView.setVisibility(View.GONE);
            }
        });
        saveEditedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleInput = titleEdit.getText().toString();
                String descriptionInput = spannable.toString();
                int id = modelClass.getId();

                Boolean updateStatus = database.updateData(new ModelClass(id,titleInput,descriptionInput));
                if (updateStatus)
                {
                    Toast.makeText(getApplicationContext(), "update success", Toast.LENGTH_SHORT).show();
                    whenSaveButtonClick(titleInput,descriptionInput);
                    saveEditedTextData(titleInput,descriptionInput);
                }else {
                    Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        boldText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                boldText(v);
            }
        });

        cardView.setVisibility(View.GONE);
    }

    private void saveEditedTextData(String titleInput, String descriptionInput) {
        String titleData = titleInput;
        String descriptionData = descriptionInput;

    }

    private void findAllItem() {
        imageView = findViewById(R.id.imageShowID);
        backButton =findViewById(R.id.backButtonID);
        editButton=findViewById(R.id.editButtonID);
        title=findViewById(R.id.titleShowID);
        description=findViewById(R.id.descriptionShowID);
        cardView = findViewById(R.id.card2);
        boldText = findViewById(R.id.boldID);
    }

    private void whenCancelButtonClick() {
        titleEdit.setVisibility(View.GONE);
        descriptionEdit.setVisibility(View.GONE);
        saveEditedText.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        titleEdit.clearComposingText();
    }

    private void whenSaveButtonClick(String titleInput, String descriptionInput) {
        String titleT = titleInput;
        String des = descriptionInput;
        saveEditedText.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        titleEdit.setVisibility(View.GONE);
        descriptionEdit.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        title.setText(titleT);
        description.setText(des);
    }

    private void editTextSetData() {
        titleEdit.setText(modelClass.getTitle());
        descriptionEdit.setText(modelClass.getDescription());
    }

    private void findAndHideEditText() {
        titleEdit = findViewById(R.id.titleEditID);
        descriptionEdit=findViewById(R.id.descriptionEditID);
        saveEditedText=findViewById(R.id.saveEditedTextID);
        cancelButton=findViewById(R.id.cancelButtonID);

        titleEdit.setVisibility(View.GONE);
        descriptionEdit.setVisibility(View.GONE);
        saveEditedText.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

    private void editButtonClick() {
        titleEdit.setVisibility(View.VISIBLE);
        descriptionEdit.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        saveEditedText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return true;
    }

    public void boldText(View view)
    {
        spannable = new SpannableStringBuilder(descriptionEdit.getText());
        spannable.setSpan(new StyleSpan(Typeface.BOLD),descriptionEdit.getSelectionStart(),
                descriptionEdit.getSelectionEnd(),0);
        descriptionEdit.setText(spannable);

    }


}