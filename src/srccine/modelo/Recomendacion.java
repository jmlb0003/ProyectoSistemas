/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package srccine.modelo;

import java.io.Serializable;

/**
 *
 * @author Jesus
 */
public class Recomendacion implements Comparable<Recomendacion>, Serializable{
    private Pelicula _pelicula;
    private double _valoracion;

    Recomendacion(Pelicula pelicula, double prediccion) {
        _pelicula = pelicula;
        _valoracion = prediccion;
    }
    
    /**
     * Funcion de comparacion para establecer la Recomendacion
     * @param o Recomendacion con la que se establece la comparacion
     * @return 1 si la valoracion es menor que o, -1 si es mayor que o
     */
    @Override
    public int compareTo(Recomendacion o) {
        if(_valoracion < o._valoracion){
            return 1;
        }
        if (_valoracion > o._valoracion){
            return -1;
        }
        if (_valoracion == o._valoracion && _pelicula.obtieneID() != o._pelicula.obtieneID()){
            return 1;
        }
        return 0;
    }

    public Pelicula getPelicula() {
        return _pelicula;
    }

    public double getValoracion() {
        return _valoracion;
    }
    
}
