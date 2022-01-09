package com.example.multiplo_usuario_autenticacao_google_maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registration extends AppCompatActivity {

    EditText name,reg_email,reg_password,reg_re_password;
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        reg_re_password = findViewById(R.id.rePassword);
        btn1 = findViewById(R.id.login_reg);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(registration.this, user_profile.class));
            }
        });
    }
}