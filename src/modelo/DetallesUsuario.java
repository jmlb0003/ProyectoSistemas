package modelo;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase que representa la encapsulacion de los atributos de los usuarios
 * Los detalles disponibles son: clave, suma, media, valoraciones,...
 * @author 
 */
public class DetallesUsuario implements Serializable {
    //No puede ser de tipo final para poder meterlos en la base de datos
    //Mapa con todos los atributos del usuario
    private Map<String, Object> _detalles;
        
    /**
     * Constructor de la clase
     * @param detalles detalles con los que será inicializado el objeto
     */
    public DetallesUsuario(Map detalles){
        _detalles = detalles;
    }

    /**
     * Obtiene todos los detalles de un usuario
     * @return Devuelve un objeto de tipo Map con los detalles del usuario
     */    
    public Map obtieneDetalles(){ return _detalles;}
    
    
    /**
     * Obtiene un detalle en concreto del usuario
     * @param nombre Identificador del atributo a obtener
     * @return Devuelve un Object con el atributo que coincida con el identificador
     */
    public Object obtieneDetalle(String nombre){
        return _detalles.get(nombre);
    }   
    
}