package srccine.aplicacion;

import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.Modelo;
import srccine.modelo.excepciones.*;
import srccine.modelo.persistencia.excepciones.*;

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
            Modelo m = new Modelo();
            m.inicializar();
        } catch ( ErrorGrabarModeloSimilitud ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorInsertarValoracion ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorInsertarPelicula ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorInsertarUsuario ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorConexionBBDD ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorLecturaFichero ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorLeerModeloSimilitud ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorInsertarRecomendacion ex) {
            Logger.getLogger(SRCCine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
