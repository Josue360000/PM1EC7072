package com.example.pm1ec7072.Vista;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pm1ec7072.R;
import com.example.pm1ec7072.BaseDatos.apunte_dao;
import com.example.pm1ec7072.Modelo.apunte_modelo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private EditText et_asignatura;
    private EditText et_tema;
    private EditText et_fecha;
    private EditText et_descripcion;
    private Button btn_grabar;
    private Button btn_guardar;
    private Button btn_ir_dictado_desde_registro;
    private Button btn_ver_lista;
    private MediaRecorder grabadora;
    private String ruta_audio_grabado = "";
    private boolean esta_grabando = false;
    private apunte_dao apunte_dao_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        et_asignatura = findViewById(R.id.et_asignatura);
        et_tema = findViewById(R.id.et_tema);
        et_fecha = findViewById(R.id.et_fecha);
        et_descripcion = findViewById(R.id.et_descripcion);
        btn_grabar = findViewById(R.id.btn_grabar);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_ir_dictado_desde_registro = findViewById(R.id.btn_ir_dictado_desde_registro);
        btn_ver_lista = findViewById(R.id.btn_ver_lista);
        apunte_dao_obj = new apunte_dao(this);

        btn_grabar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            } else {
                controlar_grabacion();
            }
        });

        btn_guardar.setOnClickListener(v -> guardar_apunte());

        btn_ir_dictado_desde_registro.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, DictadoActivity.class);
            startActivityForResult(intent, 400);
        });

        btn_ver_lista.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, ListaActivity.class);
            startActivity(intent);
        });
    }

    private void controlar_grabacion() {
        if (!esta_grabando) {
            File carpeta = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            File archivo_audio = new File(carpeta, "audio_" + System.currentTimeMillis() + ".3gp");
            ruta_audio_grabado = archivo_audio.getAbsolutePath();

            grabadora = new MediaRecorder();
            grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabadora.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            grabadora.setOutputFile(ruta_audio_grabado);

            try {
                grabadora.prepare();
                grabadora.start();
                esta_grabando = true;
                btn_grabar.setText("Detener Grabacion");
                Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error al grabar", Toast.LENGTH_SHORT).show();
            }
        } else {
            grabadora.stop();
            grabadora.release();
            grabadora = null;
            esta_grabando = false;
            btn_grabar.setText("Grabar Audio");
            Toast.makeText(this, "Audio guardado", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardar_apunte() {
        String asignatura = et_asignatura.getText().toString().trim();
        String tema = et_tema.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String descripcion = et_descripcion.getText().toString().trim();

        if (asignatura.isEmpty() || tema.isEmpty() || fecha.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", fecha)) {
            Toast.makeText(this, "Formato de fecha incorrecto use AAAA-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }

        apunte_modelo nuevo_apunte = new apunte_modelo(0, asignatura, tema, fecha, descripcion, ruta_audio_grabado);
        long id = apunte_dao_obj.insertar_apunte(nuevo_apunte);

        if (id > 0) {
            Toast.makeText(this, "Apunte guardado correctamente", Toast.LENGTH_SHORT).show();
            et_asignatura.setText("");
            et_tema.setText("");
            et_fecha.setText("");
            et_descripcion.setText("");
            ruta_audio_grabado = "";
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null) {
            String texto_dictado = data.getStringExtra("descripcion_dictada");
            if (texto_dictado != null) {
                et_descripcion.setText(texto_dictado);
            }
        }
    }
}