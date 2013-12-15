/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package srccine.vista;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import srccine.controlador.ControladorInterface;
import srccine.controlador.ErrorUsuarioRegistrado;
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
                    vista.setDatosValoracion(datosValoracion);  
                    controlador.peticionValorarPelicula();
                    RequestDispatcher dispatcher = request.getRequestDispatcher(response.encodeURL("pelicula.jsp?id="+request.getParameter("idPelicula")));
                    dispatcher.forward (request, response);
                }catch (NumberFormatException e){
                   // notificarError (request, response, "Nombre de usuario existente", 
                     //       "El nombre de usuario escogido ya existe en el sistema", "registrarse.jsp"); 
                } catch (ErrorValoraPelicula ex) {
                    Logger.getLogger(Valorar.class.getName()).log(Level.SEVERE, null, ex);
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
