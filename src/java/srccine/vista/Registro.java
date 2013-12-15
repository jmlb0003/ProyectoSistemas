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
import srccine.controlador.ErrorUsuarioRegistrado;

/**
 * Clase
 * @author Jesus
 */
public class Registro extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Object> datosRegistro = new HashMap();
        HttpSession sc = request.getSession();
        response.encodeURL("Registro");
        ControladorInterface controlador = (ControladorInterface) sc.getAttribute("controlador");
        int dia, mes, ano;
        if (controlador != null){       
            if(request.getParameter("idUsuario")!=null && request.getParameter("clave")!=null){
                datosRegistro.put("idUsuario", request.getParameter("idUsuario"));
                datosRegistro.put("nombre", request.getParameter("nombre"));
                datosRegistro.put("apellidos", request.getParameter("apellidos"));
                datosRegistro.put("clave", request.getParameter("clave"));
                try{
                    dia = Integer.parseInt(request.getParameter("dia"));
                }catch (NumberFormatException e){
                    dia = -1;
                }            
                try{
                    mes = Integer.parseInt(request.getParameter("mes"));
                }catch (NumberFormatException e){
                    mes = -1;
                }
                try{
                    ano = Integer.parseInt(request.getParameter("ano"));
                }catch (NumberFormatException e){
                    ano = -1;
                }

                if (dia !=-1 && mes !=-1 && ano!=-1){
                    datosRegistro.put("fechaNacimiento", new GregorianCalendar(ano, mes, dia));
                }
                datosRegistro.put("fechaRegistro", new GregorianCalendar());

                //Proporcionamos los datos a la vista
                VistaInterface vista = (VistaInterface) sc.getAttribute("vista");
                vista.setDetallesRegistro(datosRegistro);
            
                try {
                    controlador.peticionRegistrarUsuario();
                } catch (ErrorUsuarioRegistrado ex) {
                    notificarError (request, response, "Nombre de usuario existente", 
                            "El nombre de usuario escogido ya existe en el sistema", "registrarse.jsp"); 
                }
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
        return "Registro";
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
