package com.example.multiplo_usuario_autenticacao_google_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {

    EditText name,reg_email,reg_password,reg_re_password;
    FirebaseAuth auth;
    Button btn1;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Chamada dos Componentes
        name = findViewById(R.id.name);
        reg_email = findViewById(R.id.email_reg);
        reg_password = findViewById(R.id.password_reg);
        reg_re_password = findViewById(R.id.rePassword);
        btn1 = findViewById(R.id.login_reg);

        // Chamada no firbase

        auth =  FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference();

        // Botão de cadastro
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // busca campo tempo
                String email = reg_email.getText().toString();
                final String nam = name.getText().toString();
                String password = reg_password.getText().toString();

                // Teste se o campo é vazio
                if (TextUtils.isEmpty(email)) {
                    reg_email.setError("Requirido");
                    reg_email.requestFocus();

                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            dataRef.child(uid).setValue(nam);

                            Toast.makeText(registration.this, "Conta de Usuário criada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(registration.this, user_profile.class));

                        } else {
                            Toast.makeText(registration.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (TextUtils.isEmpty(password)) {
                    reg_password.setError("Requirido");
                    reg_password.requestFocus();

                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    reg_email.setError("E-mail não válido");
                    reg_email.requestFocus();

                    return;
                }
                if (password.length() < 6) {
                    reg_password.setError("Pelo menos seis caracteres");
                    reg_password.requestFocus();
                    return;
                }
            }
        });
    }
}