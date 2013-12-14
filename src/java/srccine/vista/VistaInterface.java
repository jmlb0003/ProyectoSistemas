package srccine.vista;

import java.util.Map;

/**
 *
 * @author José
 */
public interface VistaInterface {

    public void mostrarVentanaPrincipal();

    public Map obtenerValoracionPelicula();
    
    public Map obtenerDetallesNuevoUsuario();

    public String obtenerCriteriosBusqueda();

    public Map obtenerDetallesInicioSesion();

    public Long obtenerIDPelicula();

    public void setDetallesRegistro(Map<String, Object> _datosRegistro);

    public void setDetallesLogin(Map<String, Object> datosLogin);

    public void setDetallesBusqueda(String consulta);
    
}
