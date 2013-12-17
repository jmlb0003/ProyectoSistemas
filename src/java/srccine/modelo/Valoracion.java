package srccine.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * Clase Valoracion
 * @author Grupo 3
 */
@Entity(name="Valoracion")
public class Valoracion implements Serializable {
    @Id
    @GeneratedValue
    private long _id;
    private final long _idPelicula;
    private final String _idUsuario;
    private int _puntuacion;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _fecha;

    /**
     * Constructor por defecto
     */
    public Valoracion() {
        _id = 0;
        _idPelicula = 0;
        _idUsuario = "";
    }
    
    /**
     * Constructor del objeto de Valoracion
     * @param p puntuacion de la valoracion
     * @param idU Id del usuario que realizo la valoracion
     * @param idP Id de la pelicula sobre la que se realiza la valoracion
     */
    public Valoracion(int p, String idU, long idP) {
        _puntuacion = p;
        _idPelicula = idP;
        _idUsuario = idU;
    }
    
    /**
     * Constructor del objeto de Valoracion
     * @param p puntuacion de la valoracion
     * @param idU Id del usuario que realizo la valoracion
     * @param idP Id de la pelicula sobre la que se realiza la valoracion
     * @param fecha fecha en la que se realiza la valoracion
     */
    public Valoracion(int p, String idU, long idP, Date fecha) {
        _puntuacion = p;
        _idPelicula = idP;
        _idUsuario = idU;
        _fecha = fecha;
    }

    /**
     * Devuelve el id de la pelicula valorada
     * @return Id de la pelicula
     */
    public long obtieneIDPelicula() {
        return _idPelicula;
    }

    /**
     * Devuelve el id del usuario que valora
     * @return Id del usuario
     */
    public String obtieneIDUsuario() {
        return _idUsuario;
    }

    /**
     * Devuelve la puntuacion
     * @return Puntuacion de la valoracion
     */
    public int obtienePuntuacion() {
        return _puntuacion;
    }

    /**
     * Devuelve la fecha en la que se valoro
     * @return fecha de valoracion
     */
    public Date obtieneFecha() {
        return _fecha;
    }    

    /**
     * Modifica la puntuacion de la valoracion
     * @param puntuacion 
     */
    public void asignaPuntuacion(int puntuacion ){
        this._puntuacion = puntuacion;
    }

    /**
     * Modifica la fecha de la valoracion
     * @param f 
     */
    public void asignaFecha(Date f) {
        this._fecha = f;
    }
    
}
