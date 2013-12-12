package srccine.aplicacion;

import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.Modelo;
import srccine.modelo.Recomendacion;
import srccine.modelo.Usuario;
import srccine.modelo.persistencia.DAOUsuario;

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
