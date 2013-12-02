package srccine.modelo;

import java.io.BufferedWriter;
import srccine.modelo.excepciones.ErrorGrabarResultadosTest;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.algoritmos.AlgEvaluacion;
import srccine.modelo.algoritmos.AlgSimilitud;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.persistencia.DAOPelicula;
import srccine.modelo.persistencia.DAOUsuario;
import srccine.modelo.persistencia.DAOValoracion;
import srccine.modelo.persistencia.GestorPersistencia;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;

/**
 * Clase modelo que implementa ModeloInterface y
 * que sera el gestor de todo el modelo del sistema
 * @author Jesus
 */
public class Modelo implements ModeloInterface{
    
    private static final int K = 50;
    
    /**
     * Constructor de la clase, se encargara de comenzar la conexion con ls BBDD
     * @throws srccine.modelo.persistencia.excepciones.ErrorConexionBBDD
     */
    public Modelo() throws ErrorConexionBBDD{
        crearConexionBBDD();
    }
    
    /**
     * Aplica los algortimos de similitud del coseno y los de prediccion WS e IA+A
     * @throws ErrorLecturaFichero
     * @throws ErrorGrabarModeloSimilitud
     * @throws ErrorGrabarResultadosTest 
     *
    public void aplicarAlgoritmosCoseno() throws ErrorLecturaFichero, ErrorGrabarModeloSimilitud, ErrorGrabarResultadosTest{
        //Crea el fichero lector de CSV
        FicheroCSV fichero = new FicheroCSV();
        
        for (int i=1;i<=5;i++){
            // Lee las particiones correspondientes del CSV y obtiene las diferentes
            // EEDD necesarias
            fichero.leerCSVTest(i);
            Map<Long, Pelicula> peliculas = fichero.getPeliculas();
            List<String> clavesUsuariosTest = fichero.getClavesUsuariosTest();
            List<Usuario> usuariosTest = fichero.getUsuariosTest();

            //Aplica los algoritmos de similitud para los diferentes valores de K
            for (int k=20;k<=50;k=k+15){
                
                // Aplicamos el algoritmo de similitud del coseno
                long tiempoMS = System.currentTimeMillis();
                HashMap<Long, TreeSet<Similitud>> mSimilitudCoseno = AlgSimilitud.getModeloSimilitudCoseno(k, new ArrayList(peliculas.values()), clavesUsuariosTest);                
                tiempoMS = System.currentTimeMillis() - tiempoMS;

                // Grabamos en disco el Modelo de Similitud
                grabarModeloSimilitud(mSimilitudCoseno,"modelosSimilitud\\Coseno-"+k+"parte"+i+".bin",tiempoMS);
                                    
                // Aplicamos el algoritmo de prediccion IA+A para los diferentes valores de n
                int n[] = {0,2,4,8};
                for (int m=0;m<4;m++){
                    // Realiza el test 
                    long tiempoIAA = System.currentTimeMillis();        
                    double MAE = AlgEvaluacion.testIAmasA(n[m], mSimilitudCoseno,peliculas, usuariosTest);
                    tiempoIAA = System.currentTimeMillis() - tiempoIAA;
                    long tiempo = tiempoMS + tiempoIAA;
                                        
                    // Grabamos los resultados de ejecucion del test
                    grabarResultados("Particion: "+i, "Algoritmo del Coseno. k = " + k, 
                            "IA+A. n = "+n[m], tiempo, MAE );
                }

                // Aplicamos el algoritmo de prediccion WS
                long tiempoWS = System.currentTimeMillis();        
                double MAE = AlgEvaluacion.testWS(mSimilitudCoseno,usuariosTest);
                tiempoWS = System.currentTimeMillis() - tiempoWS;
                long tiempo = tiempoMS + tiempoWS;

                //Grabamos los resultados de ejecucion del Test
                grabarResultados("Particion: "+i, "Algoritmo del Coseno. k = " + k, 
                            "Algoritmo de prediccion WS.", tiempo, MAE );
            }
        }
    }    */
    
    /**
     * Aplica los algoritmos de similitud de Pearson, los de IA+A y WS
     * @throws ErrorLecturaFichero
     * @throws ErrorGrabarModeloSimilitud
     * @throws ErrorGrabarResultadosTest 
     *
    public void aplicarAlgoritmosPearson() throws ErrorLecturaFichero, ErrorGrabarModeloSimilitud, ErrorGrabarResultadosTest{
        //Crea el fichero lector de CSV
        FicheroCSV fichero = new FicheroCSV();
        
        for (int i=1;i<=5;i++){
            // Lee las particiones correspondientes del CSV y obtiene las diferentes
            // EEDD necesarias
            fichero.leerCSVTest(i);
            Map<Long, Pelicula> peliculas = fichero.getPeliculas();
            List<String> clavesUsuariosTest = fichero.getClavesUsuariosTest();
            List<Usuario> usuariosTest = fichero.getUsuariosTest();

            //Aplica los algoritmos de similitud para los diferentes valores de K
            for (int k=20;k<=50;k=k+15){
                
                // Aplicamos el algoritmo de similitud de Pearson
                long tiempoMS = System.currentTimeMillis();
                HashMap<Long, TreeSet<Similitud>> mSPearson = AlgSimilitud.getModeloSimilitudPearson(k, new ArrayList(peliculas.values()), clavesUsuariosTest);                
                tiempoMS = System.currentTimeMillis() - tiempoMS;

                // Grabamos en disco el Modelo de Similitud
                grabarModeloSimilitud(mSPearson,"modelosSimilitud\\pearson-"+k+"parte"+i+".bin",tiempoMS);
                
                // Aplicamos el algoritmo de prediccion IA+A para los diferentes valores de n
                int n[] = {0,2,4,8};
                for (int m=0;m<4;m++){
                    // Realiza el test 
                    long tiempoIAA = System.currentTimeMillis();        
                    double MAE = AlgEvaluacion.testIAmasA(n[m], mSPearson,peliculas, usuariosTest);
                    tiempoIAA = System.currentTimeMillis() - tiempoIAA;
                    long tiempo = tiempoMS + tiempoIAA;
                                        
                    //Grabamos los resultados de ejecucion del test
                    grabarResultados("Particion: "+i, "Algoritmo del Pearson. k = " + k, 
                            "IA+A. n = "+n[m], tiempo, MAE );
                }

                //Aplicamos el algoritmo de prediccion WS
                long tiempoWS = System.currentTimeMillis();        
                double MAE = AlgEvaluacion.testWS(mSPearson,usuariosTest);
                tiempoWS = System.currentTimeMillis() - tiempoWS;
                long tiempo = tiempoMS + tiempoWS;

                //Grabamos los resultados de ejecucion del test
                grabarResultados("Particion: "+i, "Algoritmo del Pearson. k = " + k, 
                            "Algoritmo de prediccion WS.", tiempo, MAE );
            }
        }
    }*/

    /**
     * Cierra la conexion con la BBDD
     */
    @Override
    public void cerrar() {
        GestorPersistencia.desconectar();
    }
    
    /**
     * Crea la conexion con la BBDD
     * @throws ErrorConexionBBDD Error al realizar la conexion con la BBDD
     */
    private void crearConexionBBDD() throws ErrorConexionBBDD{
        GestorPersistencia.crearConexion();
    }
    
    /**
     * Crea la EEDD desde los ficheros y las inserta en la BBDD
     * @throws srccine.modelo.excepciones.ErrorLecturaFichero
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario
     * @throws srccine.modelo.excepciones.ErrorGrabarModeloSimilitud
     */
    public void importarDatos() throws ErrorLecturaFichero, ErrorInsertarValoracion, 
            ErrorInsertarPelicula, ErrorInsertarUsuario, ErrorGrabarModeloSimilitud{
        
        //Creamos el lector de CSV, leemos y obtenemos las EEDD
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSV();
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones();
        System.out.println("fichero leido");            
        // Calculamos el modelo de similitud con el coeficiente del coseno
        // y lo grabamos en disco
        HashMap<Long, TreeSet<Similitud>> msCoseno = AlgSimilitud.getModeloSimilitudCoseno(K, new ArrayList(peliculas.values()), new ArrayList(usuarios.keySet()));                
        grabarModeloSimilitud(msCoseno);
        System.out.println("modelosimilitud creado");
        
        //Inserta las EEDD en la BBDD
        DAOValoracion.instancia().insert(valoraciones);
        DAOPelicula.instancia().insert(peliculas);
        DAOUsuario.instancia().insert(usuarios);
    
        System.out.println("grabado en BBDD");
    }
    
    /**
     * Graba en disco el modelo de similitud obtenido en la ejecucion del test
     * @param modeloSimilitud Modelo de similitud a almacenar
     * @throws ErrorGrabarModeloSimilitud 
     */
    private void grabarModeloSimilitud(HashMap<Long, TreeSet<Similitud>> modeloSimilitud) 
            throws ErrorGrabarModeloSimilitud {
                
        URL url = this.getClass().getClassLoader().getResource("srccine/recursos/algoritmos/modeloSimilitud.bin");
        
        //Crea el lector del archivo
        ObjectOutputStream oos =null;
            
        try{
            //Inicializa el fichero
            File f=new File(url.toURI());          
            
            //Crea el lector del archivo y lo abre
            oos =new ObjectOutputStream(new FileOutputStream(f,false));

            //Graba el modelo de similitud en el fichero
            oos.writeObject(modeloSimilitud);
        
        }catch (IOException ex) {
            throw new ErrorGrabarModeloSimilitud();
        } catch (URISyntaxException ex) {
            throw new ErrorGrabarModeloSimilitud();
        }finally {
            try {
                if (oos != null){
                    oos.close();
                }
            } catch (IOException ex) {
                throw new ErrorGrabarModeloSimilitud();
            }
        }
    }

    /**
     * Graba en disco los resultados de la ejecucion de un determinado test
     * @param particion Numero de la combinacion de las particiones
     * @param algSimilitud Algortimo de similitud utilizado en la ejecucion
     * @param algPrediccion Algortimo de prediccion utilizado en el test
     * @param tiempo Tiempo en la ejecucion del test
     * @param MAE MAE del test
     * @throws ErrorGrabarResultadosTest 
     *
    private void grabarResultados(String particion, String algSimilitud, String algPrediccion, 
            long tiempo, double MAE) throws ErrorGrabarResultadosTest {
        
        File f;
        PrintWriter pw = null;
        try {
            //Crea el fichero y comprueba si existe
            f=new File("resultados\\resultados.txt");   
            pw = new PrintWriter(new FileWriter(f,true));
        } catch (IOException ex) {
            throw new ErrorGrabarResultadosTest();
        }       
        
        pw.println("------------------------------------------------");
        pw.println(particion);
        pw.println("Similitud: " + algSimilitud);
        pw.println("Prediccion:" + algPrediccion);
        
        //Escribimos los resultados y cerramos el fichero
        pw.println("Tiempo en ejecutarse: "+tiempo+" ms.");        
        pw.println("MAE: "+MAE);
        
        pw.close();        
    } */   
}
