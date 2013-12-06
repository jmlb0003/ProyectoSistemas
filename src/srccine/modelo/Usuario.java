package srccine.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * Clase Usuario
 * @author 
 */
@Entity(name="Usuario")
public class Usuario implements Serializable{
    @Id
    private String _id;

    private long _suma;

    private double _media;
    
    @Column(columnDefinition = "LONGBLOB")
    private DetallesUsuario _detalles;
    
    @OneToMany(cascade= CascadeType.ALL)
    private Map<Long,Valoracion> _valoraciones;
    
    @OneToMany
    @Sort(type=SortType.COMPARATOR, comparator = Recomendacion.class)
    private SortedSet<Recomendacion> _recomendaciones;  

    /**
     * Constructor por defecto
     */
    public Usuario() {
        _id = "";
        _detalles = null;
        _suma = 0;
        _media = 0.0;
        _valoraciones = new HashMap();
        _recomendaciones = new TreeSet();
    }
    
    /**
     * Crea un nuevo usuario
     * @param id String Identificador unico de usuario
     * @param detalles Atributos del usuario
     */
    public Usuario(String id, Map detalles) {
        _id = id;
        _suma = 0;
        _media = 0.0;
        _detalles = new DetallesUsuario(detalles);
        _valoraciones = new HashMap ();
        _recomendaciones = new TreeSet();
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
    
    public void anadirValoracion(Long id, Valoracion v){        
        _valoraciones.put(id, v);
        _suma += v.getPuntuacion();
        _media = (double) _suma / _valoraciones.size();                
    }
    
    public Valoracion obtieneValoracion(Long id){
        return _valoraciones.get(id);
    }
    
    public int numValoraciones(){
        return _valoraciones.size();
    }    
    
    public double obtieneMedia(){
        return _media;
    }
    
    public Map<Long,Valoracion> obtieneValoraciones( ){
        return _valoraciones;
    }
    
    public void anadeRecomendaciones(SortedSet<Recomendacion> recomendaciones){
        _recomendaciones = recomendaciones;
    }
    
    public SortedSet<Recomendacion> obtieneRecomendaciones(){
        return _recomendaciones;
    }
    
    public void actualizarValoracion(Long idPelicula, Valoracion v){
        _suma -=  _valoraciones.get(idPelicula).getPuntuacion();
        _suma += v.getPuntuacion();
        
        _valoraciones.put(idPelicula, v);//Reemplaza al antiguo
        
        _media = _suma / _valoraciones.size();
        
    }
}
