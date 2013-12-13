
package srccine.controlador;

import java.util.List;
import srccine.modelo.Pelicula;
import srccine.modelo.Usuario;
import srccine.vista.VistaInterface;

/**
 *
 * @author José
 */
public interface ControladorInterface {
    
    public VistaInterface obtieneVista();
    
    public Usuario obtieneUsuarioIdentificado();
    
    public List<Pelicula> obtieneMejoresPeliculas();
    
    public void peticionValorarPelicula();
    
    public void peticionBusquedaPeliculas();
    
    public void peticionRegistrarUsuario() throws ErrorUsuarioRegistrado;
    
    public void peticionIniciarSesion() throws ErrorUsuarioIdentificado;
    
    public void peticionCerrarSesion();
    
    public void peticionVerInformacionPelicula();
    
    public void registrarObservadorListaPeliculasRecomendadas(ObservadorListaPeliculasRecomendadas o);
    
    public void registrarObservadorNotaMediaPelicula(ObservadorNotaMediaPelicula o);
    
    public void registrarObservadorPeliculaSeleccionada(ObservadorPeliculaSeleccionada o);
    
    public void registrarObservadorUsuarioIdentificado(ObservadorUsuarioIdentificado o);
    
    public void registrarObservadorPeliculasBuscadas(ObservadorPeliculasBuscadas o);
    
}
