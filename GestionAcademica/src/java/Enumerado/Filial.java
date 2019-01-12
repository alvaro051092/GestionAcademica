/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado Filial
 *
 * @author Alvaro
 */
public enum Filial {
    COLONIA("Colonia", 1), ROSARIO("Rosario",2);
    
    Filial(){
        
    }
    
    private String vFilialNom;
    private int vFilialCod;

    Filial(String pFilNom, int pFil) {
        this.vFilialNom = pFilNom;
        this.vFilialCod = pFil;
   }

    /**
     *
     * @return Retorna la filial
     */
    public int getFilial() {
        return vFilialCod;
    }
    
    /**
     *
     * @return Retorna el nombre de la filial
     */
    public String getFilialNom()
    {
        return this.vFilialNom;
    }
    
    /**
     *
     * @param filCod Recibe un código de filial
     * @return Retorna la filial definida al inicio de la clase
     */
    public static Filial fromCode(int filCod) {
        for (Filial filial :Filial.values()){
            if (filial.getFilial() == filCod){
                return filial;
            }
        }
        throw new UnsupportedOperationException(
                "La filial " + filCod + " is not supported!");
    }
    
}
