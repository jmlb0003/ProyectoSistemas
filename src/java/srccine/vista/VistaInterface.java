package srccine.vista;

import java.util.Map;

/**
 *
 * @author José
 */
public interface VistaInterface {

    public Map obtenerDetallesValoracion();
    
    public Map obtenerDetallesRegistro();

    public String obtenerDetallesBusqueda();

    public Map obtenerDetallesInicioSesion();

    public Long obtenerIDPelicula();

    public void setDetallesRegistro(Map<String, Object> detalles);

    public void setDetallesInicioSesion(Map<String, Object> detalles);

    public void setDetallesBusqueda(String consulta);
    
    public void setPeliculaSeleccionada(Long id);
    
    public void setDetallesValoracion(Map<String, Object> detalles);
    
}
