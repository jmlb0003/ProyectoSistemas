package srccine.vista;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import srccine.controlador.ControladorInterface;

/**
 *
 * @author Jesus
 */
public class Registro extends HttpServlet {
    private ControladorInterface _controlador;
    private Map<String, Object> _datosRegistro;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        _datosRegistro = new HashMap();
        HttpSession sc = request.getSession(); 
        _controlador = (ControladorInterface) sc.getAttribute("controlador");
        
        if (_controlador != null){            
            _datosRegistro.put("idUsuario", request.getParameter("idUsuario"));
            _datosRegistro.put("nombre", request.getParameter("nombre"));
            _datosRegistro.put("apellidos", request.getParameter("apellidos"));
            _datosRegistro.put("clave", request.getParameter("clave"));
            int dia = Integer.parseInt(request.getParameter("dia"));
            int mes = Integer.parseInt(request.getParameter("mes"));
            int ano = Integer.parseInt(request.getParameter("ano"));
            _datosRegistro.put("fechaNacimiento", new GregorianCalendar(ano, mes, dia));
            _datosRegistro.put("fechaRegistro", new GregorianCalendar());
            _controlador.peticionRegistrarUsuario();
                        
            //Te devuelve a la pagina de inicio, recogiendo los posibles errores que haya
            try {
                RequestDispatcher dispatcher = request.getRequestDispatcher("");    
                dispatcher.forward (request, response);
            } catch (ServletException ex) {
                notificarError (request, response, "", "", "./"); 
            } catch (IOException ex) {
                notificarError (request, response, "", "", "./"); 
            }
        }
        
    }

    public Map<String, Object> getDatosRegistro() {
        return _datosRegistro;
    }
    
    private void notificarError(HttpServletRequest request, HttpServletResponse response, 
        String cabecera, String descripcion, String operacion){
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher("");    
            dispatcher.forward (request, response);
        } catch (ServletException ex) {
            notificarError (request, response, "No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001.", "./");
        } catch (IOException ex) {
            notificarError (request, response, "No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001", "./"); 
        }
    }
}
