package com.example.multiplo_usuario_autenticacao_google_maps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class user_profile extends AppCompatActivity {

    Button btn1;
    TextView welcome;
    ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btn1 = findViewById(R.id.btn1);
        welcome =  findViewById(R.id.welcome);
        img1 = findViewById(R.id.img1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(user_profile.this);
                View layoutView =  getLayoutInflater().inflate(R.layout.my_layout, null);
                final EditText et = layoutView.findViewById(R.id.edt_layout);

                ad.setTitle("Adicione um valor");
                ad.setView(layoutView);
                ad.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String newInfo = et.getText().toString();

                    }
                });
                ad.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(user_profile.this, login.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}