package srccine.vista;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import srccine.controlador.ControladorInterface;

/**
 * Clase 
 * @author Jesus
 */
public class CerrarSesion extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sc = request.getSession();
        response.encodeURL("CerrarSesion");
        ControladorInterface controlador = (ControladorInterface) sc.getAttribute("controlador");
        if (controlador != null){ 
            
            controlador.peticionCerrarSesion();
                        
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
        return "Cierre de sesion";
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
