package modelo;

import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.algoritmos.AlgSimilitud;
import modelo.excepciones.ErrorLecturaFichero;
import modelo.persistencia.GestorPersistencia;
import modelo.persistencia.excepciones.ErrorConexionBD;

/**
 *
 * @author Jesus
 */
public class Modelo implements ModeloInterface{

    private FicheroCSV _fichero;
    
    public Modelo(){
        _fichero = new FicheroCSV();
        /**try {
            GestorPersistencia.crearConexion();
            _fichero.importarDatos();
            _fichero.recuperarDato();
        } catch (ErrorConexionBD ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorLecturaFichero ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        _fichero.leerparaalgortimos();
        
    }
    
    public void aplicarAlgoritmos(){
        
        AlgSimilitud algortimosSimilitud = new AlgSimilitud();
        
    }
    
    @Override
    public void cerrar() {
        GestorPersistencia.desconectar();
    }
    
    
}
