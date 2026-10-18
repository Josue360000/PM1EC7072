package com.example.pm1ec7072;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class apunte_dao extends SQLiteOpenHelper {

    private static final String nombre_base_datos = "pm1ec7072.db";
    private static final int version_base_datos = 1;
    private static final String tabla_apuntes = "apuntes";

    public apunte_dao(Context context) {
        super(context, nombre_base_datos, null, version_base_datos);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crear_tabla = "CREATE TABLE " + tabla_apuntes + " ("
                + "id_apunte INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "asignatura TEXT, tema TEXT, fecha TEXT, descripcion TEXT, ruta_audio TEXT)";
        db.execSQL(crear_tabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tabla_apuntes);
        onCreate(db);
    }

    public long insertar_apunte(apunte_modelo apunte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("asignatura", apunte.get_asignatura());
        valores.put("tema", apunte.get_tema());
        valores.put("fecha", apunte.get_fecha());
        valores.put("descripcion", apunte.get_descripcion());
        valores.put("ruta_audio", apunte.get_ruta_audio());
        long resultado = db.insert(tabla_apuntes, null, valores);
        db.close();
        return resultado;
    }

    public List<apunte_modelo> obtener_todos_apuntes() {
        List<apunte_modelo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tabla_apuntes, null);
        if (cursor.moveToFirst()) {
            do {
                apunte_modelo apunte = new apunte_modelo();
                apunte.set_id_apunte(cursor.getInt(0));
                apunte.set_asignatura(cursor.getString(1));
                apunte.set_tema(cursor.getString(2));
                apunte.set_fecha(cursor.getString(3));
                apunte.set_descripcion(cursor.getString(4));
                apunte.set_ruta_audio(cursor.getString(5));
                lista.add(apunte);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public List<apunte_modelo> filtrar_por_asignatura(String filtro) {
        List<apunte_modelo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta = "SELECT * FROM " + tabla_apuntes + " WHERE asignatura = ?";
        Cursor cursor = db.rawQuery(consulta, new String[]{filtro});
        if (cursor.moveToFirst()) {
            do {
                apunte_modelo apunte = new apunte_modelo();
                apunte.set_id_apunte(cursor.getInt(0));
                apunte.set_asignatura(cursor.getString(1));
                apunte.set_tema(cursor.getString(2));
                apunte.set_fecha(cursor.getString(3));
                apunte.set_descripcion(cursor.getString(4));
                apunte.set_ruta_audio(cursor.getString(5));
                lista.add(apunte);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public int eliminar_apunte(int id_apunte) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete(tabla_apuntes, "id_apunte = ?", new String[]{String.valueOf(id_apunte)});
        db.close();
        return filas;
    }

    public int actualizar_apunte(apunte_modelo apunte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("asignatura", apunte.get_asignatura());
        valores.put("tema", apunte.get_tema());
        valores.put("fecha", apunte.get_fecha());
        valores.put("descripcion", apunte.get_descripcion());
        valores.put("ruta_audio", apunte.get_ruta_audio());
        int filas = db.update(tabla_apuntes, valores, "id_apunte = ?", new String[]{String.valueOf(apunte.get_id_apunte())});
        db.close();
        return filas;
    }
}