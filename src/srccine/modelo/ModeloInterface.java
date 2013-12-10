package srccine.modelo;

import java.util.List;
import java.util.SortedSet;
import srccine.modelo.excepciones.*;
import srccine.modelo.persistencia.excepciones.*;

/**
 * Interfaz que debe implementar el modelo
 * @author Jesus
 */
interface ModeloInterface {
    
    public void actualizarPelicula(Pelicula p)throws ErrorActualizarPelicula ;
    public void actualizarUsuario(Usuario u)throws ErrorActualizarUsuario ;
    public void actualizarValoracion(Valoracion v)throws ErrorActualizarValoracion ;
    public void actualizarRecomendacion(Recomendacion r) throws ErrorActualizarRecomendacion ;
    
    public void anadeValoracion(Valoracion v) throws ErrorInsertarValoracion;
    public void anadeUsuario(Usuario u) throws ErrorInsertarUsuario;
    
    public Usuario buscaUsuario(String idUsuario);
    public List buscaPeliculas(String criteriosBusqueda);
    
    public void eliminaRecomendaciones(SortedSet<Recomendacion> recomendaciones) 
            throws ErrorBorrarRecomendacion;
    
    public void inicializar()throws ErrorConexionBBDD, ErrorLeerModeloSimilitud, 
            ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarPelicula, 
            ErrorInsertarUsuario, ErrorGrabarModeloSimilitud,ErrorInsertarRecomendacion;
    
    public void cerrar();
    
    public SortedSet<Recomendacion> recibirRecomendaciones(Usuario u);
    
    public void registrarObservadorNuevoUsuario(ObservadorNuevoUsuario o);
    
}
