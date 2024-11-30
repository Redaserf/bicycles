    package com.example.bicycles.Views;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.bicycles.R;

    public class EditProfileActivity extends AppCompatActivity {

        private EditText etUsername, etAboutMe;
        private Button btnSave;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile);

            etUsername = findViewById(R.id.et_username);
            etAboutMe = findViewById(R.id.et_about_me);
            btnSave = findViewById(R.id.btn_save);

            // Recuperar datos actuales del perfil
            Intent intent = getIntent();
            String currentUsername = intent.getStringExtra("username");
            String currentAboutMe = intent.getStringExtra("about_me");

            etUsername.setText(currentUsername);
            etAboutMe.setText(currentAboutMe);

            btnSave.setOnClickListener(view -> {
                String updatedUsername = etUsername.getText().toString().trim();
                String updatedAboutMe = etAboutMe.getText().toString().trim();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated_username", updatedUsername);
                resultIntent.putExtra("updated_about_me", updatedAboutMe);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
    }
