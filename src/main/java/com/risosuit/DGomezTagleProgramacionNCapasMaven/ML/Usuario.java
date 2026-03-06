package com.risosuit.DGomezTagleProgramacionNCapasMaven.ML;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int IdUsuario;
    private String ImagenFile;
    private String UserName;
    private String Nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Email;
    private String Password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate FechaNacimiento;
    private String Sexo;
    private String Telefono;
    private String Celular;
    private String CURP;
    private int Activo = 0;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime UltimoAcceso;
    public Rol Rol;
    public List<Direccion> Direcciones;

    public Usuario() {
        this.Rol = new Rol();
        this.Direcciones = new ArrayList<>();
    }

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int activo) {
        Activo = activo;
    }

    public String getImagenFile() {
        return ImagenFile;
    }

    public void setImagenFile(String ImagenFile) {
        this.ImagenFile = ImagenFile;
    }

    public Rol getRol() {
        return Rol;
    }

    public void setRol(Rol rol) {
        this.Rol = rol;
    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public LocalDate getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public LocalDateTime getUltimoAcceso() {
        return UltimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime UltimoAcceso) {
        this.UltimoAcceso = UltimoAcceso;
    }

    @Override
    public String toString() {
        String Cadena = "";
        Cadena += "\n===========================================";
        Cadena += "\nUsuario";
        Cadena += "\nIdUsuario = " + IdUsuario;
        Cadena += "\nNombreUsuario = " + UserName;
        Cadena += "\nNombre = " + Nombre;
        Cadena += "\nApellidoPaterno = " + ApellidoPaterno;
        Cadena += "\nApellidoMaterno = " + ApellidoMaterno;
        Cadena += "\nEmail = " + Email;
        Cadena += "\nFechaNacimiento = " + FechaNacimiento;
        Cadena += "\nSexo = " + Sexo;
        Cadena += "\nTelefono = " + Telefono;
        Cadena += "\nCelular = " + Celular;
        Cadena += "\nUltimoAcceso = " + UltimoAcceso;
        Cadena += "\nrol = " + Rol.getNombre();
        if (Direcciones.size() > 0) {
            int i = 1;
            for (Direccion Direccione : Direcciones) {
                Cadena += "\n*******" + "Direccion: " + i + "*******";
                Cadena += Direccione.toString();
                i++;
            }
            Cadena += "\n****************************";
        }

        Cadena += "\n===========================================";

        return Cadena;
    }

}
