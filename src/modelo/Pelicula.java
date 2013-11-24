package modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Clase Pelicula
 * @author Jesús
 */
@Entity(name="Pelicula")
public class Pelicula implements Serializable{
    @Id
    private long _id;
    @Column(columnDefinition = "LONGBLOB") 
    private DetallesPelicula _detalles;

    /**
     * Constructor por defecto
     */
    public Pelicula() { 
        _id = 0;
        _detalles = null;
    }

    /**
     * Constructor de Pelicula
     * @param id Long Identificador unico de pelicula
     * @param detalles Atributos de la pelicula
     */
    public Pelicula(long id, Map detalles ) {
        _id = id;
        _detalles = new DetallesPelicula(detalles);
    }    
    
    /**
     * Devuelve los atributos de una pelicula
     * @return DetallesPelicula Los detalles de la pelicula
     */
    public DetallesPelicula obtieneDetalles(){
        return _detalles;
    }
    
    /**
     * Devuelve una copia de los atributos de una pelicula
     * @return Map con los detalles de la pelicula
     */
    public Map obtieneCopiaDetalles(){
        
        Map mapa = new HashMap();
        
        for (Object o: _detalles.obtieneDetalles().keySet()) {

            String nombre = (String) o;
            
            mapa.put(nombre, _detalles.obtieneDetalle(nombre));
        }
        
        return mapa;
    }
    
    /**
     * Modifica los detalles de la pelicula
     * @param detalles Nuevos detalles que tendra la pelicula
     */
    public void modificar(Map detalles){
        _detalles = new DetallesPelicula(detalles);
    }
    
    /**
     * Devuelve el identificador de la pelicula
     * @return Identificador de la pelicula
     */
    public Long obtieneID(){
        return _id;
    }
    
}
