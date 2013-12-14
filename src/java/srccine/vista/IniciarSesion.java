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
            vista.setDetallesLogin(datosLogin);
            
            try {
                controlador.peticionIniciarSesion();
            } catch (ErrorUsuarioIdentificado ex) {
                notificarError (request, response, "Identificacion incorrecta", 
                        "No se ha podido iniciar sesion con los datos de usuario introducidos", "index.jsp"); 
            }
                        
            //Te devuelve a la pagina de inicio, recogiendo los posibles errores que haya
            try {
                RequestDispatcher dispatcher = request.getRequestDispatcher(response.encodeURL("index.jsp"));
                dispatcher.forward (request, response);
            } catch (ServletException ex) {
                notificarError (request, response, "", "", "./"); 
            } catch (IOException ex) {
                notificarError (request, response, "", "", "./"); 
            }
        }else{
            notificarError (request, response, "", "", "./");
        }
        
    }
    
    private void notificarError(HttpServletRequest request, HttpServletResponse response, 
        String cabecera, String descripcion, String operacion){
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(response.encodeURL(operacion)+cabecera+descripcion); 
            dispatcher.forward (request, response);
        } catch (ServletException ex) {
            notificarError (request, response, "No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001.", operacion);
        } catch (IOException ex) {
            notificarError (request, response, "No se ha completado el registro del usuario.", 
                    "Error interno de la aplicacion. Vuelva a intentarlo más tarde. Codigo:001", operacion); 
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
