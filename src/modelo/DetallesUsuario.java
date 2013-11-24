package modelo;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase que representa la encapsulacion de los atributos de los usuarios
 * Los detalles disponibles son: id, media,...
 * @author 
 */
public class DetallesUsuario implements Serializable {
    //No puede ser de tipo final para poder meterlos en la base de datos
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
        
    /**
     * Comprueba si los criterios de busqueda coinciden con los detalles del
     * usuario
     * @param criteriosBusqueda Detalles que deben coincidir con el usuario
     * @return boolean Si coincide o no
     *
    public boolean coincide(DetallesUsuario criteriosBusqueda){
                
        // Se recorren todos los criterios de la busqueda
        for (Iterator it = criteriosBusqueda.obtieneDetalles().keySet().iterator(); it.hasNext();) {
            
            String nombre = (String)it.next();
            //Si es una busqueda, hay que comprobar si el texto de la busqueda, 
            //esta contenido en el nombre, en el apellido o en el dni
            if (nombre.equals("id")){
                //Comparamos los String, pero antes se pasa todo a minusculas
                if ( ! _detalles.get("dni").toString().toLowerCase().contains(
                        criteriosBusqueda.obtieneDetalle(nombre).toString().toLowerCase()) ){

                    if ( ! _detalles.get("nombre").toString().toLowerCase().contains(
                            criteriosBusqueda.obtieneDetalle(nombre).toString().toLowerCase()) ){
                        
                        if ( ! _detalles.get("apellidos").toString().toLowerCase().contains(
                            criteriosBusqueda.obtieneDetalle(nombre).toString().toLowerCase()) ){
                            return false;
                        }
                    }
                }
            } else{
            //Se comprueba que sea un String, entonces verifica si el criterio esta
            //contenido en el detalle del usuario
            if (criteriosBusqueda.obtieneDetalle(nombre) instanceof String) {
                //Comparamos los String, pero antes se pasa todo a minusculas
                String valor = (String)_detalles.get(nombre);
                String criterio = (String)criteriosBusqueda.obtieneDetalle(nombre);
                if (!(valor.toLowerCase()).contains(criterio.toLowerCase())) {                
                    return false;
                }
                
            } else {
                //Si no es un String, compara que ambos valores sean iguales
                if (!criteriosBusqueda.obtieneDetalle(nombre).equals(_detalles.get(nombre))) {                
                    return false;
                }
            }
            }
        }
        
        //Si verifica todos los criterios devuelve verdadero
        return true;
    }*/

    
    
}
    
