
package srccine.vista;

import java.util.Map;
import srccine.controlador.ControladorInterface;
import srccine.modelo.ModeloInterface;

/**
 *
 * @author José
 */
public class Vista implements VistaInterface {

    ModeloInterface _modelo;
    ControladorInterface _controlador; 
    Map<String, Object> _detallesRegistro; 
    private Map<String, Object> _detallesLogin;
    
    //Quitar que esto es de prueba
    public Vista(ModeloInterface modelo, ControladorInterface controlador) {
            _modelo = modelo;
            _controlador = controlador;
            _detallesRegistro = null;
    }
   
    //Quitar que esto es de prueba
    @Override
    public void mostrarVentanaPrincipal(){
        
    }
    
    @Override
    public Map obtenerDetallesNuevoUsuario() {
        return _detallesRegistro;
    }

    @Override
    public Map obtenerValoracionPelicula() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String obtenerCriteriosBusqueda() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map obtenerDetallesInicioSesion() {
        return _detallesLogin;
    }

    @Override
    public Long obtenerIDPelicula() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDetallesRegistro(Map<String, Object> datosRegistro) {
        _detallesRegistro = datosRegistro;
    }

    @Override
    public void setDetallesLogin(Map<String, Object> datosLogin) {
        _detallesLogin = datosLogin;
    }
            
}
