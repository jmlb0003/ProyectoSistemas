/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.io.Serializable;

/**
 *
 * @author Jesus
 */
public class Similitud implements Comparable<Similitud>, Serializable{
    private long _idPelicula;
    private double _similitud;

    public Similitud(long id, double similitud) {
        _idPelicula = id;
        _similitud = similitud;
    }
    
    
        @Override
    public int compareTo(Similitud o) {
        if(_similitud < o._similitud){
            return 1;
        }
        if (_similitud > o._similitud){
            return -1;
        }
        if (_similitud == o._similitud && _idPelicula != o._idPelicula){
            return 1;
        }
        return 0;
    }

    public long getIdPelicula() {
        return _idPelicula;
    }

    public void setIdPelicula(long _idPelicula) {
        this._idPelicula = _idPelicula;
    }

    public double getSimilitud() {
        return _similitud;
    }

    public void setSimilitud(double _similitud) {
        this._similitud = _similitud;
    }
    
}
