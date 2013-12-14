package srccine.aplicacion;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.controlador.Controlador;
import srccine.controlador.ErrorInicioSistema;
import srccine.modelo.Modelo;
import srccine.modelo.Pelicula;

/**
 *
 * @author Jesús
 */
public class SRCCine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Controlador m = new Controlador(new Modelo());
            List<Pelicula> obtieneMejoresPeliculas = m.obtieneMejoresPeliculas();
            if (obtieneMejoresPeliculas!=null){
                System.out.println(obtieneMejoresPeliculas.size());
            }
        } catch (ErrorInicioSistema ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
