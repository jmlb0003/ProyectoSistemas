package pr.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pr.modelo.DetallesUsuario;
import pr.modelo.Modelo;
import pr.modelo.ModeloInterface;
import pr.modelo.Pelicula;
import pr.modelo.Usuario;
import pr.modelo.Valoracion;
import pr.modelo.excepciones.ErrorGrabarModeloSimilitud;
import pr.modelo.excepciones.ErrorLecturaFichero;
import pr.modelo.excepciones.ErrorLeerModeloSimilitud;
import pr.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import pr.modelo.persistencia.excepciones.ErrorActualizarUsuario;
import pr.modelo.persistencia.excepciones.ErrorActualizarValoracion;
import pr.modelo.persistencia.excepciones.ErrorConexionBBDD;
import pr.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import pr.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import pr.modelo.persistencia.excepciones.ErrorInsertarUsuario;
import pr.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import pr.vista.Vista;
import pr.vista.VistaInterface;

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
    
    
    public Controlador() {
        try {
            _modelo = new Modelo();
            _modelo.inicializar();
            
            usuarioIdentificado = null;    
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
            
            usuarioIdentificado.anadeValoracion(idPelicula, v);
            Pelicula pelicula = _modelo.buscaPelicula(idPelicula);
            
            pelicula.anadeValoracion(idUsuario, v);
            _modelo.actualizarUsuario(usuarioIdentificado);
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
    
}
