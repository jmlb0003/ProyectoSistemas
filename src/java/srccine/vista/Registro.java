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
        //Creamos el maapa donde introducir los datos del registro del usuario
        Map<String, Object> datosRegistro = new HashMap();
        //Recogemos la informacion de la sesion y obtenemos el controlador
        HttpSession sc = request.getSession();
        ControladorInterface controlador = (ControladorInterface) sc.getAttribute("controlador");
        int dia, mes, ano;
        // Se comprueba si el controlador existe
        if (controlador != null){       
            // Se comprueba si los parametros necesarios existen.
            if(request.getParameter("idUsuario")!=null && request.getParameter("clave")!=null){
                datosRegistro.put("idUsuario", request.getParameter("idUsuario"));
                datosRegistro.put("clave", request.getParameter("clave"));
                datosRegistro.put("fechaRegistro", new GregorianCalendar());

                //Comprueba si incluye los nombres y apellidos
                if (request.getParameter("nombre")!=null){
                    datosRegistro.put("nombre", request.getParameter("nombre"));
                }                
                if (request.getParameter("apellidos")!=null){
                    datosRegistro.put("apellidos", request.getParameter("apellidos"));
                }
                try{
                    //Se parsea la fecha y se introduce
                    dia = Integer.parseInt(request.getParameter("dia"));
                    mes = Integer.parseInt(request.getParameter("mes"));
                    ano = Integer.parseInt(request.getParameter("ano"));
                    datosRegistro.put("fechaNacimiento", new GregorianCalendar(ano, mes, dia));
                }catch (NumberFormatException e){  }                

                //Proporcionamos los datos a la vista
                VistaInterface vista = (VistaInterface) sc.getAttribute("vista");
                vista.setDetallesRegistro(datosRegistro);
            
                try {
                    //Realizamos al controlador la peticion de registrar usuario
                    controlador.peticionRegistrarUsuario();
                } catch (ErrorUsuarioRegistrado ex) {
                    Vista.notificarError (request, response, "error.jsp", 
                            "Registro sin exito", "El nombre de usuario escogido ya existe en el sistema"); 
                }
            }        
            //Te devuelve a la pagina de inicio, recogiendo los posibles errores que haya
            try {
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
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
