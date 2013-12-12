
package srccine.controlador;

/**
 *
 * @author José
 */
public interface ControladorInterface {
    
    public void peticionValorarPelicula();
    
    public void peticionBusquedaPeliculas();
    
    public void peticionRegistrarUsuario();
    
    public void peticionIniciarSesion();
    
    public void peticionCerrarSesion();
    
    public void peticionVerInformacionPelicula();
    
    public void registrarObservadorListaPeliculasRecomendadas(ObservadorListaPeliculasRecomendadas o);
    
    public void registrarObservadorNotaMediaPelicula(ObservadorNotaMediaPelicula o);
    
    public void registrarObservadorPeliculaSeleccionada(ObservadorPeliculaSeleccionada o);
    
    public void registrarObservadorUsuarioIdentificado(ObservadorUsuarioIdentificado o);
    
    public void registrarObservadorPeliculasBuscadas(ObservadorPeliculasBuscadas o);
    
}
