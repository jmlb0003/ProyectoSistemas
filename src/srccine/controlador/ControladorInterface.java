
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
    
    public void notificarObservadorPeliculaSeleccionada();
    
    public void notificarObservadorUsuarioIdentificado();
    
    public void notificarObservadorPeliculasBuscadas();
    
}
