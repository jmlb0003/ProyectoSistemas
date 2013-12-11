package pr.modelo;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase que   representa la encapsulacion de los atributos de lasa peliculas
 * Los detalles disponibles son: titulo, valoraciones, ano, media, suma,....
 * @author
 */
public class DetallesPelicula implements Serializable {
    //No puede ser de tipo final para poder meterlos en la base de datos
    //Mapa con todos los atributos de la pelicula
    private Map<String, Object> _detalles;
        
    /**
     * Constructor de la clase
     * @param detalles detalles con los que será inicializado el objeto
     */
    public DetallesPelicula(Map detalles){
        _detalles = detalles;
    }

    /**
     * Obtiene todos los detalles de una pelicula
     * @return Devuelve un objeto de tipo Map con los detalles de la pelicula
     */    
    public Map obtieneDetalles(){ return _detalles;}
    
    
    /**
     * Obtiene un detalle en concreto de la pelicula
     * @param nombre Identificador del atributo a obtener
     * @return Devuelve un Object con el atributo que coincida con el identificador
     */
    public Object obtieneDetalle(String nombre){
        return _detalles.get(nombre);
    }
    
}
