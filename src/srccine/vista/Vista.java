
package srccine.vista;

import srccine.controlador.ControladorInterface;
import srccine.modelo.ModeloInterface;

/**
 *
 * @author José
 */
public class Vista implements VistaInterface {

    ModeloInterface _modelo;
    ControladorInterface _controlador;
    
    
    //Quitar que esto es de prueba
    public Vista(ModeloInterface modelo, ControladorInterface controlador) {
            _modelo = modelo;
            _controlador = controlador;
    }
   
    //Quitar que esto es de prueba
    public void mostrarVentanaPrincipal(){
        
    }
            
}
