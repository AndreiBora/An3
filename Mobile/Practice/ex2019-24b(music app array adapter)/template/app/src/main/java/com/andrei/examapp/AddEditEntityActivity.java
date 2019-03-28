package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andrei.examapp.model.GenericEntity;

public class AddEditEntityActivity extends AppCompatActivity {
    public static final String EXTRA_ADD_EDIT_ENTITY =
            "com.andrei.examapp.EXTRA_ADD_EDIT_ENTITY";

    private EditText etxtTitle;
    private EditText etxtDescription;
    private EditText etxtAlbum;
    private EditText etxtGenre;
    private EditText etxtYear;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);


        Intent intent = getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_ADD_EDIT_ENTITY);
        etxtTitle = findViewById(R.id.etxt_title);
        etxtDescription = findViewById(R.id.etxt_description);
        etxtAlbum = findViewById(R.id.etxt_album);
        etxtGenre = findViewById(R.id.etxt_genre);
        etxtYear = findViewById(R.id.etxt_yaer);
        btnAdd = findViewById(R.id.btn_add_song);

        if (entity.getId() != null) {
            setTitle("Details");
            etxtTitle.setText(entity.getTitle());
            etxtDescription.setText(entity.getDescription());
            etxtAlbum.setText(entity.getAlbum());
            etxtGenre.setText(entity.getGenre());
            etxtYear.setText(entity.getYear().toString());
        } else {
            setTitle("Add song");
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setOnClickListener(v->{
                String title = etxtTitle.getText().toString();
                String description = etxtDescription.getText().toString();
                String album = etxtAlbum.getText().toString();
                String genre = etxtAlbum.getText().toString();
                Integer year = Integer.parseInt(etxtYear.getText().toString());
                GenericEntity entity1 = new GenericEntity(title,description,album,genre,year);
                Intent data = new Intent();
                data.putExtra(EXTRA_ADD_EDIT_ENTITY,entity1);
                setResult(RESULT_OK,data);
                finish();
            });


        }
    }
}
