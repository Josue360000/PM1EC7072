package com.example.pm1ec7072;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView lv_apuntes;
    private Spinner spinner_asignaturas;
    private Button btn_filtrar;
    private Button btn_ir_dictado;
    private apunte_dao apunte_dao_obj;
    private List<apunte_modelo> lista_actual;
    private MediaPlayer reproductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lv_apuntes = findViewById(R.id.lv_apuntes);
        spinner_asignaturas = findViewById(R.id.spinner_asignaturas);
        btn_filtrar = findViewById(R.id.btn_filtrar);
        btn_ir_dictado = findViewById(R.id.btn_ir_dictado);
        apunte_dao_obj = new apunte_dao(this);

        cargar_lista_completa();

        btn_filtrar.setOnClickListener(v -> {
            String filtro = spinner_asignaturas.getSelectedItem() != null ? spinner_asignaturas.getSelectedItem().toString() : "";
            lista_actual = apunte_dao_obj.filtrar_por_asignatura(filtro);
            actualizar_lista(lista_actual);
        });

        btn_ir_dictado.setOnClickListener(v -> {
            Intent intent = new Intent(ListaActivity.this, DictadoActivity.class);
            startActivity(intent);
        });

        lv_apuntes.setOnItemClickListener((parent, view, position, id) -> mostrar_opciones(lista_actual.get(position)));
    }

    private void cargar_lista_completa() {
        lista_actual = apunte_dao_obj.obtener_todos_apuntes();
        actualizar_lista(lista_actual);
        actualizar_spinner();
    }

    private void actualizar_lista(List<apunte_modelo> lista) {
        List<String> textos = new ArrayList<>();
        for (apunte_modelo a : lista) {
            textos.add(a.get_asignatura() + " - " + a.get_tema() + "\n" + a.get_fecha());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos);
        lv_apuntes.setAdapter(adaptador);
    }

    private void actualizar_spinner() {
        List<String> asignaturas = new ArrayList<>();
        for (apunte_modelo a : lista_actual) {
            if (!asignaturas.contains(a.get_asignatura())) {
                asignaturas.add(a.get_asignatura());
            }
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, asignaturas);
        spinner_asignaturas.setAdapter(adaptador);
    }

    private void mostrar_opciones(apunte_modelo apunte) {
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Opciones");
        constructor.setMessage("Que desea hacer con el apunte?");
        constructor.setPositiveButton("Eliminar", (dialog, which) -> {
            apunte_dao_obj.eliminar_apunte(apunte.get_id_apunte());
            cargar_lista_completa();
            Toast.makeText(this, "Apunte eliminado", Toast.LENGTH_SHORT).show();
        });
        constructor.setNeutralButton("Reproducir", (dialog, which) -> {
            if (apunte.get_ruta_audio() != null && !apunte.get_ruta_audio().isEmpty()) {
                if (reproductor != null) {
                    reproductor.release();
                }
                reproductor = new MediaPlayer();
                try {
                    reproductor.setDataSource(apunte.get_ruta_audio());
                    reproductor.prepare();
                    reproductor.start();
                    Toast.makeText(this, "Reproduciendo audio", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Error al reproducir", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No hay audio en este apunte", Toast.LENGTH_SHORT).show();
            }
        });
        constructor.setNegativeButton("Cancelar", null);
        constructor.show();
    }
}