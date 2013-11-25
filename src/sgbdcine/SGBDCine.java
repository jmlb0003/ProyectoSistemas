package sgbdcine;

import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Modelo;
import modelo.excepciones.ErrorGrabarModeloSimilitud;
import modelo.excepciones.ErrorLecturaFichero;

/**
 *
 * @author Jesús
 */
public class SGBDCine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         Modelo m = new Modelo();
        try {
            m.aplicarAlgoritmosPearson();
        } catch (ErrorLecturaFichero ex) {
            Logger.getLogger(SGBDCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorGrabarModeloSimilitud ex) {
            Logger.getLogger(SGBDCine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
