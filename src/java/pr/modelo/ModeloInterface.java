package pr.modelo;

import pr.modelo.persistencia.excepciones.ErrorActualizarUsuario;
import pr.modelo.persistencia.excepciones.ErrorActualizarValoracion;
import pr.modelo.persistencia.excepciones.ErrorActualizarRecomendacion;
import pr.modelo.persistencia.excepciones.ErrorConexionBBDD;
import pr.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import pr.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import pr.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import pr.modelo.persistencia.excepciones.ErrorBorrarRecomendacion;
import pr.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import pr.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import pr.modelo.excepciones.ErrorLecturaFichero;
import pr.modelo.excepciones.ErrorLeerModeloSimilitud;
import pr.modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.util.List;
import java.util.SortedSet;

/**
 * Interfaz que debe implementar el modelo
 * @author Jesus
 */
public interface ModeloInterface {
    
    public void actualizarPelicula(Pelicula p)throws ErrorActualizarPelicula ;
    public void actualizarUsuario(Usuario u)throws ErrorActualizarUsuario ;
    public void actualizarValoracion(Valoracion v)throws ErrorActualizarValoracion ;
    public void actualizarRecomendacion(Recomendacion r) throws ErrorActualizarRecomendacion ;
    
    public void anadeValoracion(Valoracion v) throws ErrorInsertarValoracion;
    public void anadeUsuario(Usuario u) throws ErrorInsertarUsuario;
    
    public Usuario buscaUsuario(String idUsuario);
    public List buscaPeliculas(String criteriosBusqueda);
    public Pelicula buscaPelicula(Long idPelicula);
    public Valoracion buscaValoracion(String idUsuario, Long idPelicula);

    public void eliminaRecomendaciones(SortedSet<Recomendacion> recomendaciones) 
            throws ErrorBorrarRecomendacion;
    
    public void inicializar()throws ErrorConexionBBDD, ErrorLeerModeloSimilitud, 
            ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarPelicula, 
            ErrorInsertarUsuario, ErrorGrabarModeloSimilitud,ErrorInsertarRecomendacion;
    
    public void cerrar();
    
    public SortedSet<Recomendacion> recibirRecomendaciones(Usuario u);
    
    public void registrarObservadorNuevoUsuario(ObservadorNuevoUsuario o);
    
}