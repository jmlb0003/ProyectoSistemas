package srccine.vista;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import srccine.controlador.ControladorInterface;
import srccine.controlador.ErrorUsuarioIdentificado;

/**
 * Clase 
 * @author Jesus
 */
public class IniciarSesion extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Object> datosLogin = new HashMap();
        HttpSession sc = request.getSession();
        response.encodeURL("IniciarSesion");
        
        ControladorInterface controlador = (ControladorInterface) sc.getAttribute("controlador");
        if (controlador != null){            
            datosLogin.put("idUsuario", request.getParameter("idUsuario"));
            datosLogin.put("clave", request.getParameter("clave"));
           
            //Proporcionamos los datos a la vista
            VistaInterface vista = (VistaInterface) sc.getAttribute("vista");
            vista.setDetallesInicioSesion(datosLogin);
            
            try {
                controlador.peticionIniciarSesion();
            } catch (ErrorUsuarioIdentificado ex) {
                Vista.notificarError (request, response, "error.jsp", 
                        "Identificacion incorrecta", "Compruebe que ha introducido correctamente sus datos.");  
            }

            //Te devuelve a la pagina de inicio, recogiendo los posibles errores que haya
            try {
                RequestDispatcher dispatcher = request.getRequestDispatcher(request.getParameter("url"));
                dispatcher.forward (request, response);
            } catch (ServletException ex) {
                Vista.notificarError (request, response, "error.jsp", 
                         "Redireccion incomplenta", "Error interno de la aplicacion, disculpe las moliestias."); 
            } catch (IOException ex) {
                Vista.notificarError (request, response, "error.jsp", 
                         "Redireccion incomplenta", "Error interno de la aplicacion, disculpe las moliestias."); 
            }
        }else{
            Vista.notificarError (request, response, "error.jsp", 
                     "Acceso invalido", "Se ha accedido al registro de usuarios de una manera incorrecta."); 
        }
        
    }
    
    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Inicio de sesion";
    }
    
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
