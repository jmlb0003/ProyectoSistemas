package srccine.modelo;

import srccine.modelo.persistencia.excepciones.ErrorActualizarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorActualizarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorActualizarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorBorrarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.persistencia.DAOValoracion;
import srccine.modelo.persistencia.DAOPelicula;
import srccine.modelo.persistencia.DAOUsuario;
import srccine.modelo.persistencia.GestorPersistencia;
import srccine.modelo.persistencia.DAORecomendacion;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import srccine.modelo.algoritmos.AlgSimilitud;
import srccine.modelo.algoritmos.AlgPrediccion;
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
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Clase modelo que implementa ModeloInterface y
 * que sera el gestor de todo el modelo del sistema
 * @author Jesus
 */
public class Modelo implements ModeloInterface{
    
    public static final int K = 50;
    public static final int N = 4;
    public static final int NUM_RECOMENDACIONES = 20;
    
    private HashMap<Long, TreeSet<Similitud>> _modeloSimilitud;    
    private Map<Long,Pelicula> _peliculas;    
    private List<ObservadorNuevoUsuario> _observadores;
    
    public Modelo(){
        _modeloSimilitud = null;
        _peliculas = null;
        _observadores = new ArrayList<ObservadorNuevoUsuario>();
    }
    
    /**
     * Constructor de la clase, se encargara de comenzar la conexion con ls BBDD
     * @throws srccine.modelo.persistencia.excepciones.ErrorConexionBBDD
     * @throws srccine.modelo.excepciones.ErrorLeerModeloSimilitud
     * @throws srccine.modelo.excepciones.ErrorLecturaFichero
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario
     * @throws srccine.modelo.excepciones.ErrorGrabarModeloSimilitud
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion
     */
    @Override
    public void inicializar() throws ErrorConexionBBDD, ErrorLeerModeloSimilitud, 
            ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarPelicula, 
            ErrorInsertarUsuario, ErrorGrabarModeloSimilitud, ErrorInsertarRecomendacion{
        
        crearConexionBBDD();

        // Comprueba si la base de datos esta vacia
        if (DAOPelicula.instancia().getNumPeliculas()==0){
            // importa el conjunto de datos desde ficheros
            importarDatos();
        }else{
            // Carga el modelo de similitud desde disco
            cargarModeloSimilitud();    
            _peliculas = DAOPelicula.instancia().get();
        }
        
        notificarObservadorNuevoUsuario();
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
    private void importarDatos() throws ErrorLecturaFichero, ErrorInsertarValoracion, ErrorInsertarRecomendacion,
            ErrorInsertarPelicula, ErrorInsertarUsuario, ErrorGrabarModeloSimilitud, ErrorLeerModeloSimilitud{
        
        //Creamos el lector de CSV, leemos y obtenemos las EEDD
        FicheroCSV fichero = new FicheroCSV();
        fichero.leerCSV();
        _peliculas = fichero.getPeliculas();
        Map<String, Usuario> usuarios = fichero.getUsuarios();
        List valoraciones = fichero.getValoraciones();
            
        // Calculamos el modelo de similitud con el coeficiente del coseno
        // y lo grabamos en disco
        _modeloSimilitud = AlgSimilitud.getModeloSimilitudCoseno(K, new ArrayList(_peliculas.values()));
        grabarModeloSimilitud();
        
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            Usuario usuario = entry.getValue();
            SortedSet<Recomendacion> recibirRecomendaciones = recibirRecomendaciones(usuario);
            usuario.anadeRecomendaciones(recibirRecomendaciones);               
        }
        
        List<Recomendacion> l = new ArrayList();
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            String string = entry.getKey();
            Usuario usuario = entry.getValue();
            TreeSet<Recomendacion> recibirRecomendaciones = (TreeSet<Recomendacion>) recibirRecomendaciones(usuario);
            usuario.anadeRecomendaciones(recibirRecomendaciones);
            l.addAll(recibirRecomendaciones.descendingSet());               
        }
        
        //Inserta las EEDD en la BBDD
        DAOValoracion.instancia().insert(valoraciones);       
        DAOPelicula.instancia().insert(_peliculas);        
        DAORecomendacion.instancia().insert(l);       
        DAOUsuario.instancia().insert(usuarios);
    }
    
    /**
     * Graba en disco el modelo de similitud obtenido en la ejecucion del test
     * @throws ErrorGrabarModeloSimilitud 
     */
    private void grabarModeloSimilitud() 
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
            oos.writeObject(_modeloSimilitud);
        
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
            System.out.println(url.toURI());
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
    
    @Override
    public SortedSet<Recomendacion> recibirRecomendaciones(Usuario usuario){
        
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
                    recomendaciones.pollLast();
                }                
            }
        }
        
        return recomendaciones;        
    }    
    
    @Override
    public void actualizarPelicula(Pelicula p) throws ErrorActualizarPelicula {
        DAOPelicula.instancia().update(p);
    }
    
    
    /**
     * Busca una valoracion en la base de datos
     * @param idUsuario
     * @param idPelicula
     * @return Valoracion encontrada en la BBDD, Null si no se ha encontrado ninguna
     * coincidencia
     */
    @Override
    public Valoracion buscaValoracion(String idUsuario, Long idPelicula) {
        return DAOValoracion.instancia().get(idUsuario, idPelicula);        
    }
    
    @Override
    public void actualizarUsuario(Usuario u) throws ErrorActualizarUsuario {
        DAOUsuario.instancia().update(u);
    }

    @Override
    public void actualizarValoracion(Valoracion v) throws ErrorActualizarValoracion {
        DAOValoracion.instancia().update(v);
    }

    @Override
    public void actualizarRecomendacion(Recomendacion r) throws ErrorActualizarRecomendacion{
        DAORecomendacion.instancia().update(r);
    }

    @Override
    public void anadeValoracion(Valoracion v) throws ErrorInsertarValoracion{
        DAOValoracion.instancia().insert(v);
    }

    @Override
    public void anadeUsuario(Usuario u) throws ErrorInsertarUsuario {
        DAOUsuario.instancia().insert(u);
        notificarObservadorNuevoUsuario();
    }

    @Override
    public Usuario buscaUsuario(String idUsuario) {
        return  DAOUsuario.instancia().get(idUsuario);
    }

    @Override
    public List buscaPeliculas(String criteriosBusqueda) {
        return DAOPelicula.instancia().get(criteriosBusqueda);
    }
    
    @Override
    public void eliminaRecomendaciones(SortedSet<Recomendacion> recomendaciones) 
            throws ErrorBorrarRecomendacion {
        
        DAORecomendacion.instancia().remove(recomendaciones);
    }

    @Override
    public void registrarObservadorNuevoUsuario(ObservadorNuevoUsuario o) {
        _observadores.add(o);
        o.usuarioNuevoRegistrado();
    }
    
    private void notificarObservadorNuevoUsuario(){
        for (ObservadorNuevoUsuario o : _observadores) {
            o.usuarioNuevoRegistrado();            
        }
    }

    @Override
    public Pelicula buscaPelicula(Long idPelicula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
