package srccine.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Clase Pelicula
 * @author Jesús
 */
@Entity(name="Pelicula")
public class Pelicula implements Serializable{
    //Entero long identificador de la pelicula
    @Id
    private long _id;
    //Detalles y atributos de la pelicula
    @Column(columnDefinition = "LONGBLOB") 
    private DetallesPelicula _detalles;
    
    private long _suma;

    private double _media;
    
    @OneToMany
    private Map<String,Valoracion> _valoraciones;

    /**
     * Constructor por defecto
     */
    public Pelicula() { 
        _id = 0;
        _detalles = null;
        _suma = 0;
        _media = 0.0;
        _valoraciones = new HashMap();
    }

    /**
     * Constructor de Pelicula
     * @param id Long Identificador unico de pelicula
     * @param detalles Atributos de la pelicula
     */
    public Pelicula(long id, Map detalles ) {
        _id = id;
        _detalles = new DetallesPelicula(detalles);        
        _suma = 0;
        _media = 0.0;
        _valoraciones = new HashMap();
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
        
    public void anadeValoracion(String id, Valoracion v){        
        _valoraciones.put(id, v);
        _suma += v.getPuntuacion();
        _media = (double) _suma / _valoraciones.size();                
    }
    
    public Valoracion obtieneValoracion(String id){
        return _valoraciones.get(id);
    }    
    
    public Map<String,Valoracion> obtieneValoraciones( ){
        return _valoraciones;
    }    
    
    public int numValoraciones(){
        return _valoraciones.size();
    }

    public double obtieneMedia() {
        return _media;
    }

}
