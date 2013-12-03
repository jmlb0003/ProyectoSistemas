package srccine.modelo;

import srccine.modelo.persistencia.ErrorLeerModeloSimilitud;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.algoritmos.AlgPrediccion;
import srccine.modelo.algoritmos.AlgSimilitud;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.persistencia.DAOPelicula;
import srccine.modelo.persistencia.DAOUsuario;
import srccine.modelo.persistencia.DAOValoracion;
import srccine.modelo.persistencia.GestorPersistencia;
import srccine.modelo.persistencia.excepciones.ErrorActualizarUsuario;
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
    
    public static final int K = 50;
    public static final int N = 4;
    public static final int NUM_RECOMENDACIONES = 100;
    
    private HashMap<Long, TreeSet<Similitud>> _modeloSimilitud;    
    private Map<Long,Pelicula> _peliculas;    
    private List<ObservadorNuevoUsuario> _observadores;
    
    public Modelo(){
        _modeloSimilitud = null;
        _peliculas = null;
    }
    
    /**
     * Constructor de la clase, se encargara de comenzar la conexion con ls BBDD
     * @throws srccine.modelo.persistencia.excepciones.ErrorConexionBBDD
     * @throws srccine.modelo.persistencia.ErrorLeerModeloSimilitud
     * @throws srccine.modelo.excepciones.ErrorLecturaFichero
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario
     * @throws srccine.modelo.excepciones.ErrorGrabarModeloSimilitud
     */
    @Override
    public void inicializar() throws ErrorConexionBBDD, ErrorLeerModeloSimilitud, 
            ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarPelicula, 
            ErrorInsertarUsuario, ErrorGrabarModeloSimilitud{
        
        crearConexionBBDD();
        importarDatos();
        /*// Comprueba si la base de datos esta vacia
        if (DAOPelicula.instancia().getNumPeliculas()==0){
            // importa el conjunto de datos desde ficheros
            importarDatos();
        }else{
            // Carga el modelo de similitud desde disco
            cargarModeloSimilitud();//1344086
            _peliculas = DAOPelicula.instancia().getPeliculas();
            Usuario usuario = DAOUsuario.instancia().get("1344086");
            TreeSet<Recomendacion> recibirRecomendaciones = recibirRecomendaciones(usuario);
            usuario.anadeRecomendaciones(recibirRecomendaciones);
            
            try {
                DAOUsuario.instancia().update(usuario);
            } catch (ErrorActualizarUsuario ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            Usuario get = DAOUsuario.instancia().get("1344086");
            TreeSet<Recomendacion> obtieneDetalle = get.obtieneRecomendaciones();
            
            Iterator<Recomendacion> iterator = obtieneDetalle.iterator();
            System.out.println("Usuario: 1344086 | Recomendaciones recibidas: "+recibirRecomendaciones.size());

            while (iterator.hasNext()) {
                Recomendacion recomendacion = iterator.next();
                System.out.println("peli: "+ recomendacion.getPelicula().obtieneID());
                System.out.println("v: "+ recomendacion.getValoracion());
            }              
            
        }*/
    }
    
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
    private void importarDatos() throws ErrorLecturaFichero, ErrorInsertarValoracion, 
            ErrorInsertarPelicula, ErrorInsertarUsuario, ErrorGrabarModeloSimilitud{
        
        //Creamos el lector de CSV, leemos y obtenemos las EEDD
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSV();
        Map<Long, Pelicula> peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones();

        // Calculamos el modelo de similitud con el coeficiente del coseno
        // y lo grabamos en disco
        _modeloSimilitud = AlgSimilitud.getModeloSimilitudCoseno(K, new ArrayList(peliculas.values()));                
        grabarModeloSimilitud(_modeloSimilitud);
        
        //Inserta las EEDD en la BBDD
        DAOValoracion.instancia().insert(valoraciones);
        DAOPelicula.instancia().insert(peliculas);
        DAOUsuario.instancia().insert(usuarios);
    
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
     * Carga en memoria el modelo de similitud desde el fichero de disco
     * @throws ErrorLeerModeloSimilitud Error al leer el fichero
     */
    private void cargarModeloSimilitud() throws ErrorLeerModeloSimilitud{
        ObjectInputStream ois = null;

        try {
            
            URL url = this.getClass().getClassLoader().getResource("srccine/recursos/algoritmos/modeloSimilitud.bin");
            File f=new File(url.toURI());          
            ois = new ObjectInputStream(new FileInputStream(f));
            _modeloSimilitud = (HashMap<Long, TreeSet<Similitud>>) ois.readObject();
            
        } catch (IOException ex) {
            throw new ErrorLeerModeloSimilitud();
        } catch (URISyntaxException ex) {
            throw new ErrorLeerModeloSimilitud();
        } catch (ClassNotFoundException ex) {
            throw new ErrorLeerModeloSimilitud();
        } finally {            
            try {
                if (ois!=null){
                    ois.close();
                }
            } catch (IOException ex) {
                throw new ErrorLeerModeloSimilitud();
            }
        }
    }
    
    private TreeSet<Recomendacion> recibirRecomendaciones(Usuario usuario){
        
        Map<Long, Valoracion> valoraciones = usuario.obtieneValoraciones();        
        List<Long> predecibles = new ArrayList();
        
        for (Map.Entry<Long,Pelicula> e : _peliculas.entrySet()) {            
            if (!predecibles.contains(e.getKey())&&!valoraciones.containsKey(e.getKey())){
                predecibles.add(e.getKey());
            }
        }

        TreeSet<Recomendacion> recomendaciones = new TreeSet();
        for (Iterator<Long> it = predecibles.iterator(); it.hasNext();) {
            long id = (Long) it.next();
            Pelicula pelicula = _peliculas.get(id);
            double media = pelicula.obtieneMedia();
            double prediccion = AlgPrediccion.calcularPrediccionIAmasA(N,
                    usuario, media, _modeloSimilitud.get(id));
            
            if (prediccion!=-1){
                recomendaciones.add(new Recomendacion(pelicula,prediccion));                
                if (recomendaciones.size()>NUM_RECOMENDACIONES){
                    recomendaciones.remove(recomendaciones.last());
                }                
            }
        }
        
        return recomendaciones;        
    }

    private void notificarObservadorNuevoUsurio(){
        for (ObservadorNuevoUsuario o : _observadores) {
            o.usuarioNuevoRegistrado();            
        }
    }
}
