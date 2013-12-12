/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package srccine.vista;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
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
public class Registro extends HttpServlet{
    ControladorInterface _controlador;
    Map<String, Object> _datosRegistro;
    
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
            _datosRegistro.put("fechaNacimiento", new GregorianCalendar(mes, mes, dia));
            _datosRegistro.put("fechaRegistro", new GregorianCalendar());          
        }
        
    }
}
