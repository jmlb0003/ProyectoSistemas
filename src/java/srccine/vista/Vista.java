
package srccine.vista;

import java.io.IOException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author José
 */
public class Vista implements VistaInterface {
    private Map<String, Object> _detallesRegistro; 
    private Map<String, Object> _detallesLogin;
    private String _detallesBusqueda;
    private long _idPeliculaSeleccionada;
    private Map<String, Object> _detallesValoracion;
    
    //Quitar que esto es de prueba
    public Vista() {
            _detallesRegistro = null;
            _detallesLogin = null;
            _detallesBusqueda = "";
            _idPeliculaSeleccionada = (long) 0;
            _detallesValoracion = null;            
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
    
    /**
     *
     * @param request
     * @param response
     */
    public static void notificarError(HttpServletRequest request, HttpServletResponse response, String url, String titulo, String mensaje){
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(response.encodeURL("error.jsp")+
                    "?titulo="+titulo+"?mensaje="+mensaje);
            dispatcher.forward (request, response);
        } catch (ServletException ex) {
            notificarError (request, response, url,"No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001.");
        } catch (IOException ex) {
            notificarError (request, response, url, "No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001"); 
        }
    }

    @Override
    public void setDatosValoracion(Map<String, Object> datosValoracion) {
        _detallesValoracion = datosValoracion;
    }
}
