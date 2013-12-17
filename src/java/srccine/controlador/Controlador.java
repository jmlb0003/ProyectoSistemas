package srccine.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import srccine.modelo.ModeloInterface;
import srccine.modelo.ObservadorNuevoUsuario;
import srccine.modelo.Pelicula;
import srccine.modelo.Recomendacion;
import srccine.modelo.Usuario;
import srccine.modelo.Valoracion;
import srccine.modelo.excepciones.*;
import srccine.modelo.persistencia.excepciones.*;
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
    
    public Controlador(ModeloInterface aModelo) throws ErrorInicioSistema {
        try {            
            _modelo = aModelo;  
            _modelo.inicializar();
            _usuarioIdentificado = null; 
            _peliculaSeleccionada = null;
            _peliculasBuscadas = null;
            
            //El controlador tiene que crear la vista y hace que se muestre y que 
            //inicie la ejecucion del ciclo de eventos
            _vista = new Vista();
            
            /**
             * El controlador se registra a sí mismo como observador de la lista
             * de usuarios del modelo. Cuando se añade un usuario el controlador
             * será notificado
             */
            _modelo.registrarObservadorNuevoUsuario(this);
        
        } catch (ErrorConexionBBDD ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorLecturaFichero ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorLeerModeloSimilitud ex){ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorInsertarValoracion ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorInsertarUsuario ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorInsertarRecomendacion ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorGrabarModeloSimilitud ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }catch (ErrorInsertarPelicula ex){ ex.printStackTrace();
            throw new ErrorInicioSistema();
        }
    }    
    
    @Override
    public void peticionValorarPelicula() throws ErrorValoraPelicula{
        try {
            //Sacar los datos de la valoracion de la vista            
            Map detallesValoracion = _vista.obtenerDetallesValoracion();
            
            //buscamos la valoracion en la base de datos
            String idUsuario = _usuarioIdentificado.obtieneID();
            Long idPelicula = (Long) detallesValoracion.get("idPelicula");
            int nota = (Integer) detallesValoracion.get("puntuacion");
            Date fecha = (Date) detallesValoracion.get("fecha");
            
            Valoracion v = _modelo.buscaValoracion(idUsuario, idPelicula);
            if (v!=null){
                int antiguaNota = v.obtienePuntuacion();
                v.asignaPuntuacion(nota);
                v.asignaFecha(fecha);
                _usuarioIdentificado.actualizaValoracion(idPelicula, antiguaNota);
                _peliculaSeleccionada.actualizaValoracion(idUsuario, antiguaNota);
                _modelo.actualizaValoracion(v);
            }else{
                v = new Valoracion(nota, idUsuario, idPelicula, fecha);
                _usuarioIdentificado.anadeValoracion(idPelicula, v);
                _peliculaSeleccionada.anadeValoracion(idUsuario, v);
                _modelo.anadeValoracion(v);
            }
            
            _modelo.actualizaUsuario(_usuarioIdentificado);
            _modelo.actualizaPelicula(_peliculaSeleccionada);
            
            new LanzarRecomendaciones().start();            
            
        } catch (ErrorActualizarUsuario  ex) {
            throw new ErrorValoraPelicula();
        } catch (ErrorInsertarValoracion ex) {
            throw new ErrorValoraPelicula();
        } catch (ErrorActualizarValoracion ex) {
            throw new ErrorValoraPelicula();
        } catch (ErrorActualizarPelicula ex) {
            throw new ErrorValoraPelicula();
        }
    }

    @Override
    public void peticionBusquedaPeliculas() {
        _peliculaSeleccionada = null;
        
        _peliculasBuscadas = _modelo.buscaPeliculas(_vista.obtenerDetallesBusqueda()); 
    }

    
    @Override
    public void peticionRegistrarUsuario() throws ErrorUsuarioRegistrado {
        Map detallesUsuario = _vista.obtenerDetallesRegistro();
        
        String idUsuario = (String) detallesUsuario.get("idUsuario");
        
        Usuario usu = _modelo.buscaUsuario(idUsuario);
        if (usu == null) {            
            try {
                _modelo.anadeUsuario(new Usuario(idUsuario, detallesUsuario));
            } catch (ErrorInsertarUsuario ex) {
                throw new ErrorUsuarioRegistrado();
            }
        }else{
            throw new ErrorUsuarioRegistrado();
        }
    }

    @Override
    public void peticionIniciarSesion() throws ErrorUsuarioIdentificado {        
        Map detallesUsuario = _vista.obtenerDetallesInicioSesion();
        
        String idUsuario = (String) detallesUsuario.get("idUsuario");
        
        Usuario usu = _modelo.buscaUsuario(idUsuario);
        
        if (usu != null) {
            String c1 = (String) usu.obtieneDetalles().obtieneDetalle("clave");
            String c2 = (String) detallesUsuario.get("clave");
            
            if (c1.equals(c2)) {
                _usuarioIdentificado = usu; 
            }else{
                throw new ErrorUsuarioIdentificado();
            }
        }else{
            throw new ErrorUsuarioIdentificado();
        }
    }

    @Override
    public void peticionCerrarSesion() {
        _usuarioIdentificado = null;
        _peliculaSeleccionada = null;
        _peliculasBuscadas = null;
    }

    @Override
    public void peticionVerInformacionPelicula() {
        Pelicula p = _modelo.buscaPelicula(_vista.obtenerIDPelicula());
        
        _peliculaSeleccionada = p; 
    }

    @Override
    public void usuarioNuevoRegistrado() {
        if (_vista.obtenerDetallesRegistro()!=null){
            String idUsuario = (String) _vista.obtenerDetallesRegistro().get("idUsuario");

            if (!idUsuario.equals("")) {
                Usuario usu = _modelo.buscaUsuario(idUsuario);
                if (usu != null) {
                    _usuarioIdentificado = usu;                 
                }
            }
        }
    } 

    @Override
    public VistaInterface obtieneVista() {
        return _vista;
    }

    @Override
    public Usuario obtieneUsuarioIdentificado() {
        return _usuarioIdentificado;
    }
    
    @Override
    public List<Pelicula> obtieneMejoresPeliculas(){
        return _modelo.buscaPeliculasMejorValoradas();
    }

    @Override
    public List<Pelicula> obtienePeliculasBuscadas() {
        return _peliculasBuscadas;
    }
    
    @Override
    public Pelicula obtienePeliculaSeleccionada(){
        return _peliculaSeleccionada;
    }
    
private class LanzarRecomendaciones extends Thread{
        
    public LanzarRecomendaciones(){
    }
    public void run(){
        try {
            List<Recomendacion> l = new ArrayList();
            Usuario usuario = _usuarioIdentificado;
            _modelo.eliminaRecomendaciones(usuario.obtieneRecomendaciones());
            TreeSet<Recomendacion> recibirRecomendaciones = (TreeSet<Recomendacion>) _modelo.recibirRecomendaciones(usuario);
            usuario.anadeRecomendaciones(recibirRecomendaciones);
            _modelo.actualizaUsuario(usuario);
            l.addAll(recibirRecomendaciones.descendingSet());
           _modelo.anadeRecomendacion(l);
        } catch (ErrorActualizarUsuario ex) {            
        } catch (ErrorInsertarRecomendacion ex) {            
        } catch (ErrorBorrarRecomendacion ex) {
        }
    } 
}
   
}

