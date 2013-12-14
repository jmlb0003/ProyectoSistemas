package srccine.modelo;

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Jesus
 */
@Entity (name="Recomendacion")
public class Recomendacion implements Comparable<Recomendacion>, Serializable, Comparator<Recomendacion>{
    @Id
    @GeneratedValue
    private long _id;
    
    @OneToOne(cascade=CascadeType.ALL)
    private Pelicula _pelicula;
    
    private double _valoracion;

    public Recomendacion() {
        _pelicula = null;
        _valoracion = 0.0;
    }

    Recomendacion(Pelicula pelicula, double prediccion) {
        _pelicula = pelicula;
        _valoracion = prediccion;
    }
    
    /**
     * Funcion de comparacion para establecer la Recomendacion
     * @param o Recomendacion con la que se establece la comparacion
     * @return 1 si la valoracion es menor que o, -1 si es mayor que o
     */
    @Override
    public int compareTo(Recomendacion o) {
        if(_valoracion < o._valoracion){
            return 1;
        }
        if (_valoracion > o._valoracion){
            return -1;
        }
        if (_valoracion == o._valoracion && _pelicula.obtieneID() != o._pelicula.obtieneID()){
            return 1;
        }
        return 0;
    }

    public Pelicula getPelicula() {
        return _pelicula;
    }

    public double getValoracion() {
        return _valoracion;
    }

    @Override
    public int compare(Recomendacion o1, Recomendacion o2) {
        if(o1._valoracion < o2._valoracion){
            return 1;
        }
        if (o1._valoracion > o2._valoracion){
            return -1;
        }
        if (o1._valoracion == o2._valoracion && o1._pelicula.obtieneID() != o2._pelicula.obtieneID()){
            return 1;
        }
        return 0;
    }
    
}
