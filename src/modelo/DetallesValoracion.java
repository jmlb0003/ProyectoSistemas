package modelo;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase que representa la encapsulacion de los atributos de las valoraciones
 * Los detalles disponibles son: fecha, valor,...
 * @author 
 */
public class DetallesValoracion implements Serializable {
    //No puede ser de tipo final para poder meterlos en la base de datos
    private Map<String, Object> _detalles;
        
    /**
     * Constructor de la clase
     * @param detalles detalles con los que será inicializado el objeto
     */
    public DetallesValoracion(Map detalles){
        _detalles = detalles;
    }

    /**
     * Obtiene todos los detalles de una valoracion
     * @return Devuelve un objeto de tipo Map con los detalles de la valoracion
     */    
    public Map obtieneDetalles(){ return _detalles;}
    
    
    /**
     * Obtiene un detalle en concreto de la valoracion
     * @param nombre Identificador del atributo a obtener
     * @return Devuelve un Object con el atributo que coincida con el identificador
     */
    public Object obtieneDetalle(String nombre){
        return _detalles.get(nombre);
    }
    

    
}
