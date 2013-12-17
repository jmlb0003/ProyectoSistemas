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
import srccine.controlador.ErrorValoraPelicula;

/**
 *
 * @author Jesus
 */
public class Valorar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
               
        Map<String, Object> datosValoracion = new HashMap();
        HttpSession sc = request.getSession();
        response.encodeURL("Valorar");
        ControladorInterface controlador = (ControladorInterface) sc.getAttribute("controlador");
        if (controlador != null){ 
            if(request.getParameter("puntuacion")!=null && request.getParameter("idPelicula")!=null){
                try{
                    datosValoracion.put("idPelicula", Long.parseLong(request.getParameter("idPelicula")) );
                    datosValoracion.put("fecha", new GregorianCalendar().getTime());
                    datosValoracion.put("puntuacion",  Integer.parseInt(request.getParameter("puntuacion")) );

                    //Proporcionamos los datos a la vista
                    VistaInterface vista = (VistaInterface) sc.getAttribute("vista");
                    vista.setDetallesValoracion(datosValoracion);  
                    controlador.peticionValorarPelicula();
                    RequestDispatcher dispatcher = request.getRequestDispatcher("pelicula.jsp?id="+request.getParameter("idPelicula"));
                    dispatcher.forward (request, response);
                }catch (NumberFormatException e){
                    Vista.notificarError (request, response, "error.jsp", 
                            "Valoracion incompleta", "No se ha podido procesar su voto."); 
                } catch (ErrorValoraPelicula ex) {
                    Vista.notificarError (request, response, "error.jsp", 
                            "Valoracion incompleta", "No se ha podido procesar su voto."); 
                 }
            }
        }
    }   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Valorar";
    }// </editor-fold>

}
