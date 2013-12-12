
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
    Registro _registro;
    
    //Quitar que esto es de prueba
    public Vista(ModeloInterface modelo, ControladorInterface controlador) {
            _modelo = modelo;
            _controlador = controlador;
            _registro = new Registro();
    }
   
    //Quitar que esto es de prueba
    @Override
    public void mostrarVentanaPrincipal(){
        
    }
    
    @Override
    public Map obtenerDetallesNuevoUsuario() {
        return _registro.getDatosRegistro();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long obtenerIDPelicula() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
}
