package modelo;

import modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.algoritmos.AlgEvaluacion;
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
    
    public void aplicarAlgoritmos() throws ErrorLecturaFichero, ErrorGrabarModeloSimilitud{
        //Leemos el fichero csv para los Test
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSVTest(1);
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        //List<Long> peliculasTest = fichero.getPeliculasTest();
        List<String> clavesUsuariosTest = fichero.getClavesUsuariosTest();
        List<Usuario> usuariosTest = fichero.getUsuariosTest();
        fichero = null;
        System.out.println("Ficheros leidos correctamente \nAplicando modelo de similitud del coseno...");     

        int n = 10, k = 20;        

        //Aplicamos el algoritmo de similitud del coseno y lo grabamos en disco
        long tiempo = System.currentTimeMillis();
        HashMap<Long, TreeSet<Similitud>> modeloSimilitudCoseno = AlgSimilitud.getModeloSimilitudCoseno(k,
                peliculas, clavesUsuariosTest);                
        tiempo = System.currentTimeMillis() - tiempo;

        // Grabamos en disco el Modelo de Similitud
        grabarModeloSimilitud(modeloSimilitudCoseno,"recursos/algoritmos/Coseno-"+k,tiempo);                

        //Aplicamos el algoritmo de prediccion IA+A
        long tiempoTest = System.currentTimeMillis();        
        Parametros param = AlgEvaluacion.testIAmasA(n, modeloSimilitudCoseno,peliculas, usuariosTest);
        tiempoTest = System.currentTimeMillis() - tiempoTest;
        tiempo = tiempo + tiempoTest;

        //Grabamos los resultados de ejecucion del Test
        grabarResultados("recursos/algoritmos/CosenoIAmasA-"+k+".txt", "Algoritmo del Coseno. k = " + k, 
                "IA+A. n = "+n, tiempo, param );
            
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
                
        try { 
            //Inserta las EEDD en la BBDD
            DAOValoracion.instancia().insert(valoraciones);
            DAOPelicula.instancia().insert(peliculas);
            DAOUsuario.instancia().insert(usuarios);

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
        System.out.println(detalles.obtieneDetalle("titulo") + " -- titulo");
        System.out.println(detalles.obtieneDetalle("ano") + " -- ano");
        System.out.println(detalles.obtieneDetalle("suma") + " -- suma_valoraciones");
        System.out.println(detalles.obtieneDetalle("media") + " -- valoracion_media");
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

    private void grabarModeloSimilitud(HashMap<Long, TreeSet<Similitud>> modeloSimilitudCoseno, 
            String ruta, long tiempo) throws ErrorGrabarModeloSimilitud {
                
        URL url = this.getClass().getClassLoader().getResource(ruta+".bin");
        //Crea el lector del archivo
        ObjectOutputStream oos =null;
            
        try{
            //Crea el fichero y comprueba si existe, en ese caso lo sobrescribe
            File f=new File(url.getPath());
            if (!f.exists()){
                //Si no existe lo crea
                f.createNewFile();
            }
            
            //Crea el lector del archivo y lo abre
            oos =new ObjectOutputStream(new FileOutputStream(f,false));
            
            //Graba el modelo de similitud en el fichero
            oos.writeObject(modeloSimilitudCoseno);
            oos.writeObject(tiempo);
        
        }catch (IOException ex) {
            throw new ErrorGrabarModeloSimilitud();
        }finally {
            try {
                oos.close();
            } catch (IOException ex) {
                throw new ErrorGrabarModeloSimilitud();
            }
        }

    }

    private void grabarResultados(String ruta, String algSimilitud, String algPrediccion, 
            long tiempo, Parametros parametros) {
        
        URL url = this.getClass().getClassLoader().getResource(ruta);
        File f;
        PrintWriter pw = null;
        try {
          f = new File(url.getPath());
          f.createNewFile();
          pw = new PrintWriter(f);
        } catch (IOException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
        pw.println("------------------------------------------------");
        pw.println("Similitud: " + algSimilitud);
        pw.println("Prediccion:" + algPrediccion);
        
        //Escribimos los resultados y cerramos el fichero
        pw.println("Tiempo en ejecutarse: "+tiempo+" ms.");        
        pw.println("MAE: "+parametros.getMAE()+" | Cobertura: "+parametros.getCobertura());
        
        pw.close();        
    }
}
