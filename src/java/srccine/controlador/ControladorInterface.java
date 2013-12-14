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
        
    public List<Pelicula> obtienePeliculasBuscadas();
    
    public Pelicula obtienePeliculaSeleccionada();
    
    public void peticionValorarPelicula() throws ErrorValoraPelicula;
    
    public void peticionBusquedaPeliculas();
    
    public void peticionRegistrarUsuario() throws ErrorUsuarioRegistrado;
    
    public void peticionIniciarSesion() throws ErrorUsuarioIdentificado;
    
    public void peticionCerrarSesion();
    
    public void peticionVerInformacionPelicula();
    
}
