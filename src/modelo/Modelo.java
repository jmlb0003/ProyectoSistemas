package modelo;

import modelo.excepciones.ErrorGrabarResultadosTest;
import modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import modelo.algoritmos.AlgEvaluacion;
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
    
    public Modelo(){
        /**try {
            GestorPersistencia.crearConexion();
        } catch (ErrorConexionBD ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
    
    /**
     * Aplica los algortimos de similitud del coseno y los de prediccion WS e IA+A
     * @throws ErrorLecturaFichero
     * @throws ErrorGrabarModeloSimilitud
     * @throws ErrorGrabarResultadosTest 
     */
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
    }    
    
    /**
     * Aplica los algoritmos de similitud de Pearson, los de IA+A y WS
     * @throws ErrorLecturaFichero
     * @throws ErrorGrabarModeloSimilitud
     * @throws ErrorGrabarResultadosTest 
     */
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
    }

    
    @Override
    public void cerrar() {
        GestorPersistencia.desconectar();
    }
    
    /**
    * Crea la EEDD desde los ficheros y las inserta en la BBDD
    */
    void importarDatos() throws ErrorLecturaFichero, ErrorInsertarValoracion, 
            ErrorInsertarPelicula, ErrorInsertarUsuario{
        
        //Creamos el lector de CSV, leemos y obtenemos las EEDD
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSV();
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones();
                
        //Inserta las EEDD en la BBDD
        DAOValoracion.instancia().insert(valoraciones);
        DAOPelicula.instancia().insert(peliculas);
        DAOUsuario.instancia().insert(usuarios);
    
    }
    
    /**
     * Graba en disco el modelo de similitud obtenido en la ejecucion del test
     * @param modeloSimilitud Modelo de similitud a almacenar
     * @param ruta Ruta donde grabar el nuevo fichero
     * @param tiempo Tiempo en obtener el modelo
     * @throws ErrorGrabarModeloSimilitud 
     */
    private void grabarModeloSimilitud(HashMap<Long, TreeSet<Similitud>> modeloSimilitud,
            String ruta, long tiempo) throws ErrorGrabarModeloSimilitud {
        
        //Crea el lector del archivo
        ObjectOutputStream oos =null;
            
        try{
            //Crea el fichero y comprueba si existe
            File f=new File(ruta);

            if (!f.exists()){
                //Si no existe lo crea    
                f.createNewFile();  
            }            
            
            //Crea el lector del archivo y lo abre
            oos =new ObjectOutputStream(new FileOutputStream(f,false));

            //Graba el modelo de similitud en el fichero
            oos.writeObject(modeloSimilitud);
            oos.writeObject(tiempo);
        
        }catch (IOException ex) {
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
     */
    private void grabarResultados(String particion, String algSimilitud, String algPrediccion, 
            long tiempo, double MAE) throws ErrorGrabarResultadosTest {
        
        URL url = this.getClass().getClassLoader().getResource("recursos/algoritmos/resultados.txt");
        File f;
        PrintWriter pw = null;
        try {
            //Crea el fichero y comprueba si existe
            f=new File(url.getPath());   
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
    }    
}
