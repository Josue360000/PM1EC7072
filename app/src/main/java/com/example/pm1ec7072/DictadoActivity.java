package com.example.pm1ec7072;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

public class DictadoActivity extends AppCompatActivity {

    private Button btn_dictar;
    private Button btn_guardar_dictado;
    private TextView tv_resultado_dictado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictado);

        btn_dictar = findViewById(R.id.btn_dictar);
        btn_guardar_dictado = findViewById(R.id.btn_guardar_dictado);
        tv_resultado_dictado = findViewById(R.id.tv_resultado_dictado);

        btn_dictar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
            } else {
                iniciar_dictado();
            }
        });


        btn_guardar_dictado.setOnClickListener(v -> {
            String texto = tv_resultado_dictado.getText().toString();
            if (!texto.isEmpty() && !texto.equals("El texto aparecera aqui...")) {
                Intent intent = new Intent();
                intent.putExtra("descripcion_dictada", texto);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "No hay texto para guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciar_dictado() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...");
        try {
            startActivityForResult(intent, 300);
        } catch (Exception e) {
            Toast.makeText(this, "Dictado no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> resultados = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (resultados != null && !resultados.isEmpty()) {
                tv_resultado_dictado.setText(resultados.get(0));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciar_dictado();
            } else {
                AlertDialog.Builder constructor = new AlertDialog.Builder(this);
                constructor.setTitle("Permiso requerido");
                constructor.setMessage("Se necesita acceso al microfono para dictar el texto.");
                constructor.setPositiveButton("Entendido", null);
                constructor.show();
            }
        }
    }
}