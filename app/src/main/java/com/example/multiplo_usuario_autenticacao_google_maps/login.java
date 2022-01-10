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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    TextView signup;
    EditText email, password;
    FirebaseAuth auth;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // carregando entradas
        signup = findViewById(R.id.signup);
        btn1 = findViewById(R.id.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // busca a autenticação no banco
        auth = FirebaseAuth.getInstance();

        // botão logar
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emails = email.getText().toString();
                String senha = password.getText().toString();

                if (TextUtils.isEmpty(emails)) {
                    email.requestFocus();

                    return;
                }
                auth.signInWithEmailAndPassword(emails, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(login.this, user_profile.class));

                            Toast.makeText(login.this, "Conta de usuário criada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (TextUtils.isEmpty(senha)) {
                    password.setError("Requirido");
                    password.requestFocus();

                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
                    email.setError("Email não válido");
                    email.requestFocus();

                    return;
                }
                if (senha.length() < 6) {
                    password.setError("Pelo menos seis caracteres");
                    password.requestFocus();

                    return;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, registration.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                    Toast.makeText(login.this, "Usuário não é vazio", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this, user_profile.class));
                }
            }
        });
    }
}