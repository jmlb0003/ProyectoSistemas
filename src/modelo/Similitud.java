package modelo;

import java.io.Serializable;

/**
 * Clase que representa la similitud que tiene una pelicula con otra.
 * @author Jesus
 */
public class Similitud implements Comparable<Similitud>, Serializable{
    //Identificador de la pelicula con la que se realiza la similitud
    private long _idPelicula;
    //Similitud entre 0 y 1 entre las dos peliculas
    private double _similitud;

    /**
     * Constructor de la clase
     * @param id identificador de la pelicula con la que se establece la similitud
     * @param similitud similitud entre las dos peliculas
     */
    public Similitud(long id, double similitud) {
        _idPelicula = id;
        _similitud = similitud;
    }
    
    
    /**
     * Funcion de comparacion para establecer la similitud
     * @param o Similitud con la que se establece la comparacion
     * @return 1 si la similitud es menor que o, -1 si es mayor que o
     */
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

    /**
     * Devuelve el identificador de la pelicula con la que se compara
     * @return long identificador de la pelicula
     */
    public long getIdPelicula() {
        return _idPelicula;
    }

    /**
     * Establece el identificador de la pelicual
     * @param _idPelicula 
     *
    public void setIdPelicula(long _idPelicula) {
        this._idPelicula = _idPelicula;
    }*/

    /**
     * Devuelve la similitud con la pelicula
     */
    public double getSimilitud() {
        return _similitud;
    }

    /**public void setSimilitud(double _similitud) {
        this._similitud = _similitud;
    }*/
    
}
