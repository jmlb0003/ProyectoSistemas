package modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Clase Usuario
 * @author 
 */
@Entity(name="Usuario")
public class Usuario implements Serializable{
    @Id
    private String _id;       
    @Column(columnDefinition = "LONGBLOB")
    private DetallesUsuario _detalles;

    /**
     * Constructor por defecto
     */
    public Usuario() {
        _id = "";
        _detalles = null;
    }
    
    /**
     * Crea un nuevo usuario
     * @param id String Identificador unico de usuario
     * @param detalles Atributos del usuario
     */
    public Usuario(String id, Map detalles) {
        _id = id;
        _detalles = new DetallesUsuario(detalles);
    }
    
    /**
     * Devuelve los detalles de un usuario
     * @return Instancia de los detalles del usuario
     */
    public DetallesUsuario obtieneDetalles(){
        return _detalles;
    }
    
    /**
     * Obtiene una copia de los detalles del usuario
     * @return Map con los detalles del usuario
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
     * Modifica los detalles de un usuario
     * @param detalles Map con los atributos modificados del usuario
     */
    public void modificar(Map detalles){
        _detalles = new DetallesUsuario(detalles);
    }
    
    /**
     * Devuelve el identificador del usuario
     * @return Identificador del usuario
     */
    public String obtieneID(){
        return _id;
    }
}
