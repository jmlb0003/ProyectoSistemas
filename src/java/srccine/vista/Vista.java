
package srccine.vista;

import java.util.Map;
import srccine.controlador.ControladorInterface;
import srccine.modelo.ModeloInterface;

/**
 *
 * @author José
 */
public class Vista implements VistaInterface {

    private ModeloInterface _modelo;
    private ControladorInterface _controlador; 
    private Map<String, Object> _detallesRegistro; 
    private Map<String, Object> _detallesLogin;
    private String _detallesBusqueda;
    private Long _idPeliculaSeleccionada;
    private Map<String, Object> _detallesValoracion;
    
    //Quitar que esto es de prueba
    public Vista(ModeloInterface modelo, ControladorInterface controlador) {
            _modelo = modelo;
            _controlador = controlador;
            _detallesRegistro = null;
    }
    
    @Override
    public Map obtenerDetallesNuevoUsuario() {
        return _detallesRegistro;
    }

    @Override
    public Map obtenerValoracionPelicula() {
        return _detallesValoracion;
    }

    @Override
    public String obtenerCriteriosBusqueda() {
        return _detallesBusqueda;
    }

    @Override
    public Map obtenerDetallesInicioSesion() {
        return _detallesLogin;
    }

    @Override
    public Long obtenerIDPelicula() {
        return _idPeliculaSeleccionada;
    }

    @Override
    public void setDetallesRegistro(Map<String, Object> datosRegistro) {
        _detallesRegistro = datosRegistro;
    }

    @Override
    public void setDetallesLogin(Map<String, Object> datosLogin) {
        _detallesLogin = datosLogin;
    }
    
    @Override
    public void setDetallesBusqueda(String consulta){
        _detallesBusqueda = consulta;
    }    
    
    @Override
    public void setPeliculaSeleccionada(Long idPelicula){
        _idPeliculaSeleccionada = idPelicula;
    }

    public void setValoracion(Map<String, Object> valoracion){
        _detallesValoracion = valoracion;
    }
   
}
