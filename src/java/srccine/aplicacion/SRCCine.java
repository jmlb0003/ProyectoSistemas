package srccine.aplicacion;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.controlador.Controlador;
import srccine.modelo.Modelo;
import srccine.modelo.Pelicula;
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
        List<Pelicula> obtieneMejoresPeliculas = m.obtieneMejoresPeliculas();
        if (obtieneMejoresPeliculas!=null){
            System.out.println(obtieneMejoresPeliculas.size());
        }
    }
    
}
