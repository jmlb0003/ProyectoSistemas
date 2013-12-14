package srccine.aplicacion;

import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.controlador.Controlador;
import srccine.modelo.Modelo;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;

/**
 *
 * @author Jesús
 */
public class SRCCine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            Controlador m = new Controlador(new Modelo());

    }
    
}
