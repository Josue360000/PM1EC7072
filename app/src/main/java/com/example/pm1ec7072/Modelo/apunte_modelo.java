package com.example.pm1ec7072.Modelo;

public class apunte_modelo {
    private int id_apunte;
    private String asignatura;
    private String tema;
    private String fecha;
    private String descripcion;
    private String ruta_audio;

    public apunte_modelo() {}

    public apunte_modelo(int id_apunte, String asignatura, String tema, String fecha, String descripcion, String ruta_audio) {
        this.id_apunte = id_apunte;
        this.asignatura = asignatura;
        this.tema = tema;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.ruta_audio = ruta_audio;
    }

    public int get_id_apunte() { return id_apunte; }
    public void set_id_apunte(int id_apunte) { this.id_apunte = id_apunte; }
    public String get_asignatura() { return asignatura; }
    public void set_asignatura(String asignatura) { this.asignatura = asignatura; }
    public String get_tema() { return tema; }
    public void set_tema(String tema) { this.tema = tema; }
    public String get_fecha() { return fecha; }
    public void set_fecha(String fecha) { this.fecha = fecha; }
    public String get_descripcion() { return descripcion; }
    public void set_descripcion(String descripcion) { this.descripcion = descripcion; }
    public String get_ruta_audio() { return ruta_audio; }
    public void set_ruta_audio(String ruta_audio) { this.ruta_audio = ruta_audio; }
}