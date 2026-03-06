
package com.risosuit.DGomezTagleProgramacionNCapasMaven.ML;

public class Pais {
    private int IdPais;
   
    private String Nombre;

    public Pais() {
    }
    
    
    
    public int getIdPais(){
        return IdPais;
    }
    public void setIdPais(int IdPais){
        this.IdPais=IdPais;
    }
    public String getNombre(){
        return Nombre;
    }
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }

    @Override
    public String toString() {
        return "Pais{" + "IdPais=" + IdPais + ", Nombre=" + Nombre + '}';
    }
    
}
