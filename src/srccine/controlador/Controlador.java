
package srccine.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.DetallesUsuario;
import srccine.modelo.ModeloInterface;
import srccine.modelo.Pelicula;
import srccine.modelo.Usuario;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.persistencia.excepciones.ErrorConexionBBDD;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import srccine.vista.Vista;
import srccine.vista.VistaInterface;

/**
 *
 * @author José
 */
public class Controlador implements ControladorInterface {
    
    
    private ModeloInterface _modelo;    //Modelo sobre el que trabaja el controlador
    
    private VistaInterface _vista;  //Vista con la que trabaja este controlador
    
    /**
     * Lista de usuarios buscados. Es un subconjunto de la lista completa de usuarios
     * que se guardan en el modelo. Aquellos que cumplen con el criterio de
     * busqueda seleccionado en un momento dado.
     */
    private Usuario usuarioIdentificado;
    
    private List<Pelicula> _peliculasBuscadas;
    
    
    //Observadores
    private List<ObservadorPeliculaSeleccionada> _observadoresPeliculaSeleccionada;
    
    private List<ObservadorListaPeliculasRecomendadas> _observadoresListaPeliculasRecomendadas;
    
    private List<ObservadorNotaMediaPelicula> _observadoresNotaMediaPelicula;
    
    private List<ObservadorUsuarioIdentificado> _observadoresUsuarioIdentificado;
    
    private List<ObservadorPeliculasBuscadas> _observadoresPeliculasBuscadas;
    
    
    public Controlador(ModeloInterface modelo) {
        try {
            _modelo = modelo;
            _modelo.inicializar();
            
            usuarioIdentificado = null;    
            _peliculasBuscadas = new ArrayList<>();
            
            
            //Inicializar observadores
            _observadoresPeliculaSeleccionada = new ArrayList<>();
            _observadoresListaPeliculasRecomendadas = new ArrayList<>();
            _observadoresNotaMediaPelicula = new ArrayList<>();
            _observadoresUsuarioIdentificado  = new ArrayList<>();
            _observadoresPeliculasBuscadas   = new ArrayList<>();
            
            //El controlador tiene que crear la vista y hace que se muestre y que 
            //inicie la ejecucion del ciclo de eventos
            _vista = new Vista(_modelo, this);
            _vista.mostrarVentanaPrincipal();
        
        } catch (ErrorConexionBBDD | ErrorLeerModeloSimilitud | ErrorLecturaFichero | ErrorInsertarValoracion | ErrorInsertarPelicula | ErrorInsertarUsuario | ErrorGrabarModeloSimilitud | ErrorInsertarRecomendacion ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /****************************************************/
    /**************Funciones de la interfaz**************/
    /****************************************************/
    
    private void validarUsuario(Usuario u, DetallesUsuario d) {
    }

    @Override
    public void peticionValorarPelicula() {
        //Sacar los datos de la valoracion de la vista
        Map detallesValoracion = _vista.obtenerValoracionPelicula();
        //LLamar a la funcion del modelo pa cambiar la valoracion
        
        ¿que se hace aqui?
        ahora deberia haber una funcion que se encargue en el modelo de:
                -buscar si ya esta la valoracion
                -actualizar la pelicula, su nota, su media, etc.
                -actualizar el usuario, su nota, su media, etc.
        _modelo.actualizarValoracion(detallesValoracion);
        _modelo.anadeValoracion(detallesValoracion);
    }

    @Override
    public void peticionBusquedaPeliculas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void peticionRegistrarUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void peticionIniciarSesion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void peticionCerrarSesion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void peticionVerInformacionPelicula() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notificarObservadorPeliculaSeleccionada() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notificarObservadorUsuarioIdentificado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notificarObservadorPeliculasBuscadas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
