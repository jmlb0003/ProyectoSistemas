package srccine.aplicacion;

import srccine.controlador.Controlador;
import srccine.modelo.Modelo;

/**
 *
 * @author Jesús
 */
public class SRCCine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controlador c = new Controlador(new Modelo());
    }
    
}
