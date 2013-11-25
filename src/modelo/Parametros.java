/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

/**
 *
 * @author Jesus
 */
public class Parametros {
    private double MAE;
    private double cobertura;
    
    public Parametros(double m, double c){
        MAE = m;
        cobertura = c;
    }

    public Parametros() {
    }

    public double getMAE() {
        return MAE;
    }

    public double getCobertura() {
        return cobertura;
    }

}
