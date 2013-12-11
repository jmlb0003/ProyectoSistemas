package pr.aplicacion;

import pr.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import pr.modelo.persistencia.excepciones.ErrorConexionBBDD;
import pr.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import pr.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import pr.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import pr.modelo.excepciones.ErrorLecturaFichero;
import pr.modelo.excepciones.ErrorLeerModeloSimilitud;
import pr.modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pr.modelo.Modelo;
import pr.modelo.Recomendacion;
import pr.modelo.Usuario;
import pr.modelo.persistencia.DAOUsuario;

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
