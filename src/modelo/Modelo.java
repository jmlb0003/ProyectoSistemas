package modelo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.algoritmos.AlgSimilitud;
import modelo.excepciones.ErrorLecturaFichero;
import modelo.persistencia.DAOPelicula;
import modelo.persistencia.DAOUsuario;
import modelo.persistencia.DAOValoracion;
import modelo.persistencia.GestorPersistencia;
import modelo.persistencia.excepciones.ErrorConexionBD;
import modelo.persistencia.excepciones.ErrorInsertarPelicula;
import modelo.persistencia.excepciones.ErrorInsertarUsuario;
import modelo.persistencia.excepciones.ErrorInsertarValoracion;

/**
 *
 * @author Jesus
 */
public class Modelo implements ModeloInterface{
    
    public Modelo(){
        try {
            GestorPersistencia.crearConexion();
        } catch (ErrorConexionBD ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void aplicarAlgoritmos(){
        //Leemos el fichero csv para los Test
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSVTest();
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones(); 
        
        System.out.println("Ficheros leidos correctamente \nAplicando modelo de similitud del coseno...");       
        
        AQUI FALTAN LAS LISTAS DE PELICULAS QUE FORMAN PARTE DE LA PARTICION TEST
                HAY QUE METERLAS EN UN ARRAYLIST
        
        //Aplicamos el algoritmo de similitud del coseno para k = 20
        AlgSimilitud algSimilitud = new AlgSimilitud();
        algSimilitud.getModeloSimilitudCoseno(20, peliculas);                
        System.out.println("tiempo | MAE");
        
        //Aplicamos el algoritmo de similitud del coseno para k = 35
        algSimilitud.getModeloSimilitudCoseno(35, peliculas);        
        System.out.println("tiempo | MAE");
        
        //Aplicamos el algoritmo de similitud del coseno para k = 50
        algSimilitud.getModeloSimilitudCoseno(50, peliculas);        
        System.out.println("tiempo | MAE");

        
    }
    
    @Override
    public void cerrar() {
        GestorPersistencia.desconectar();
    }
    
     /**
     * Crea la EEDD desde los ficheros y las inserta en la BBDD
     */
    void importarDatos() throws ErrorLecturaFichero{
        //Creamos el lector de CSV, leemos y obtenemos las EEDD
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSV();
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones();
        
        System.out.println("Ficheros leidos correctamente \nInsertando en la BBDD...");
        
        try { 
            //Inserta las EEDD en la BBDD
            DAOValoracion.instancia().insert(valoraciones);
            System.out.println("Valoraciones insertadas.");
            DAOPelicula.instancia().insert(peliculas);
            System.out.println("Peliculas insertadas.");
            DAOUsuario.instancia().insert(usuarios);
            System.out.println("Usuarios insertados.");

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
