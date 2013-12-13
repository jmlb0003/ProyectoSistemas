package srccine.modelo;

import srccine.modelo.persistencia.excepciones.ErrorActualizarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorActualizarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorActualizarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorBorrarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
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
    public List<Pelicula> buscaPeliculasMejorValoradas();

    public void eliminaRecomendaciones(SortedSet<Recomendacion> recomendaciones) 
            throws ErrorBorrarRecomendacion;
    
    public void inicializar()throws ErrorConexionBBDD, ErrorLeerModeloSimilitud, 
            ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarPelicula, 
            ErrorInsertarUsuario, ErrorGrabarModeloSimilitud,ErrorInsertarRecomendacion;
    
    public void cerrar();
    
    public SortedSet<Recomendacion> recibirRecomendaciones(Usuario u);
    
    public void registrarObservadorNuevoUsuario(ObservadorNuevoUsuario o);
    
}
