package com.example.multiplo_usuario_autenticacao_google_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplo_usuario_autenticacao_google_maps.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference minhaRef;
    EditText txt1, txt2;
    TextView txt3;
    Button btn1, btn2;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        minhaRef = FirebaseDatabase.getInstance().getReference();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User usuarioAtual = new User(txt1.getText().toString(), Integer.parseInt(txt2.getText().toString()));

                minhaRef.child("Usuário" + count).setValue(usuarioAtual);
                count++;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minhaRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> iterable = snapshot.getChildren();
                        String s = "";
                        for(DataSnapshot ds: iterable) {
                            User fetchUser = ds.getValue(User.class);
                            String nome = fetchUser.getName();
                            int mob = fetchUser.getMob();
                            
                            s = s + nome + " "+mob+"\n";

                            Toast.makeText(MainActivity.this, "kimn" + nome + mob, Toast.LENGTH_SHORT).show();
                        }
                        txt3.setText(s);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}