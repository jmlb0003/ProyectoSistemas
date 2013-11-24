package modelo;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.algoritmos.AlgSimilitud;
import modelo.excepciones.ErrorLecturaFichero;
import modelo.persistencia.DAOPelicula;
import modelo.persistencia.DAOUsuario;
import modelo.persistencia.DAOValoracion;
import modelo.persistencia.GestorPersistencia;
import modelo.persistencia.excepciones.ErrorInsertarPelicula;
import modelo.persistencia.excepciones.ErrorInsertarUsuario;
import modelo.persistencia.excepciones.ErrorInsertarValoracion;

/**
 *
 * @author Jesus
 */
public class Modelo implements ModeloInterface{

    private FicheroCSV _fichero;
    private Map<String, Usuario> _usuarios;
    private Map<Long, Pelicula> _peliculas;
    
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
    
     /**
     * Crea la EEDD desde los ficheros y las inserta en la BBDD
     */
    void importarDatos() throws ErrorLecturaFichero{
        //Lee los ficheros de peliculas y valoraciones
        leerFicheroPeliculas();
        leerFicheroValoraciones("recursos/peliculas.csv");
        System.out.println("Ficheros leidos correctamente \nInsertando en la BBDD...");
        
        try { 
            //Inserta las EEDD en la BBDD
            DAOValoracion.instancia().insert(_valoraciones);
            System.out.println("Valoraciones insertadas.");
            DAOPelicula.instancia().insert(_peliculas);
            System.out.println("Peliculas insertadas.");
            _peliculas.clear();
            DAOUsuario.instancia().insert(_usuarios);
            _usuarios.clear();
            System.out.println("Usuarios insertados.");
            _valoraciones.clear();

        } catch ( ErrorInsertarValoracion | ErrorInsertarPelicula | ErrorInsertarUsuario ex) {
            Logger.getLogger(FicheroCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Imprime por pantalla los resultados para comprobar que todo esta Ok
     */
    void recuperarDato(){
        Pelicula p = DAOPelicula.instancia().get((long)571);
        DetallesPelicula detalles = p.obtieneDetalles();
        System.out.println(detalles.obtieneDetalle("titulo") + "-- titulo");
        System.out.println(detalles.obtieneDetalle("ano") + "-- ano");
        System.out.println(detalles.obtieneDetalle("suma") + "-- suma_valoraciones");
        System.out.println(detalles.obtieneDetalle("media") + "-- valoracion_media");
        Map<String, Valoracion> val = (Map<String, Valoracion>) p.obtieneDetalles().obtieneDetalle("valoraciones");
        System.out.println(val.size() + "-- numero valoraciones recibidas.");

        Iterator<Map.Entry<String, Valoracion>> iterator = val.entrySet().iterator();
        
        while (iterator.hasNext()){
            Valoracion v = iterator.next().getValue();
            System.out.println("-------\n\t id pelicula: "+v.getIdPelicula());
            System.out.println("\t id usuario: "+v.getIdUsuario());
            System.out.println("\t valoracion: "+v.getPuntuacion());
        }
    }
}
