package srccine.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import srccine.modelo.DetallesUsuario;
import srccine.modelo.Modelo;
import srccine.modelo.ModeloInterface;
import srccine.modelo.ObservadorNuevoUsuario;
import srccine.modelo.Pelicula;
import srccine.modelo.Usuario;
import srccine.modelo.Valoracion;
import srccine.modelo.excepciones.ErrorGrabarModeloSimilitud;
import srccine.modelo.excepciones.ErrorLecturaFichero;
import srccine.modelo.excepciones.ErrorLeerModeloSimilitud;
import srccine.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorActualizarUsuario;
import srccine.modelo.persistencia.excepciones.ErrorActualizarValoracion;
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
public class Controlador implements ControladorInterface, ObservadorNuevoUsuario {
       
    private ModeloInterface _modelo;    //Modelo sobre el que trabaja el controlador
    
    private VistaInterface _vista;  //Vista con la que trabaja este controlador
    
    /**
     * Lista de usuarios buscados. Es un subconjunto de la lista completa de usuarios
     * que se guardan en el modelo. Aquellos que cumplen con el criterio de
     * busqueda seleccionado en un momento dado.
     */
    private Usuario _usuarioIdentificado;
    
    private Pelicula _peliculaSeleccionada;
    
    private List<Pelicula> _peliculasBuscadas;
    
    //Observadores
    private List<ObservadorPeliculaSeleccionada> _observadoresPeliculaSeleccionada;
    
    private List<ObservadorListaPeliculasRecomendadas> _observadoresListaPeliculasRecomendadas;
    
    private List<ObservadorNotaMediaPelicula> _observadoresNotaMediaPelicula;
    
    private List<ObservadorUsuarioIdentificado> _observadoresUsuarioIdentificado;
    
    private List<ObservadorPeliculasBuscadas> _observadoresPeliculasBuscadas;
    
    
    public Controlador(ModeloInterface aModelo) {
        try {
            
            _modelo = aModelo;
            _modelo.inicializar();
            
            _usuarioIdentificado = null; 
            _peliculaSeleccionada = null;
            _peliculasBuscadas = new ArrayList();
            
            //Inicializar observadores
            _observadoresPeliculaSeleccionada = new ArrayList();
            _observadoresListaPeliculasRecomendadas = new ArrayList();
            _observadoresNotaMediaPelicula = new ArrayList();
            _observadoresUsuarioIdentificado  = new ArrayList();
            _observadoresPeliculasBuscadas   = new ArrayList();
            
            //El controlador tiene que crear la vista y hace que se muestre y que 
            //inicie la ejecucion del ciclo de eventos
            _vista = new Vista(_modelo, this);
            _vista.mostrarVentanaPrincipal();
            
            /**
             * El controlador se registra a sí mismo como observador de la lista
             * de usuarios del modelo. Cuando se añade un usuario el controlador
             * será notificado
             */
            _modelo.registrarObservadorNuevoUsuario(this);
        
        } catch (ErrorConexionBBDD ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorLecturaFichero ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorLeerModeloSimilitud ex){
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorInsertarValoracion ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorInsertarUsuario ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorInsertarRecomendacion ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorGrabarModeloSimilitud ex){ 
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ErrorInsertarPelicula ex){ 
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
        try {
            //Sacar los datos de la valoracion de la vista            
            Map detallesValoracion = _vista.obtenerValoracionPelicula();
            
            //buscamos la valoracion en la base de datos
            String idUsuario = (String) detallesValoracion.get("idUsuario");
            Long idPelicula = (Long) detallesValoracion.get("idPelicula");
            int nota = (Integer) detallesValoracion.get("nota");
            Date fecha = (Date) detallesValoracion.get("fecha");
            
            Valoracion v = _modelo.buscaValoracion(idUsuario, idPelicula);
            if (v!=null){
                v.setFecha(fecha);
                v.setPuntuacion(nota);
                _modelo.actualizarValoracion(v);
            }else{
                v = new Valoracion(nota, idUsuario, idPelicula, fecha);
                _modelo.anadeValoracion(v);
            }
            
            _usuarioIdentificado.anadeValoracion(idPelicula, v);
            Pelicula pelicula = _modelo.buscaPelicula(idPelicula);
            
            pelicula.anadeValoracion(idUsuario, v);
            _modelo.actualizarUsuario(_usuarioIdentificado);
            _modelo.actualizarPelicula(pelicula);
            
        } catch (ErrorActualizarUsuario  ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorInsertarValoracion ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorActualizarValoracion ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorActualizarPelicula ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public void usuarioNuevoRegistrado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
