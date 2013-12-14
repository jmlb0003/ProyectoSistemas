<%-- 
    Document   : busqueda de peliculas
    Created on : 14-dic-2013, 1:24:25
    Author     : Jesus
--%>

<%@page import="java.util.List"%>
<%@page import="srccine.vista.VistaInterface"%>
<%@page import="srccine.modelo.Pelicula"%>
<%@page import="java.util.Iterator"%>
<%@page import="srccine.modelo.Modelo"%>
<%@page import="srccine.controlador.Controlador"%>
<%@page import="srccine.controlador.ControladorInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <header>
        <title>Sistema de recomendaci&oacute;n de pel&iacute;culas</title>   
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap -->
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">    
            
        <!-- Mis estilos -->   
        <link rel="stylesheet"  href="css/estilos.css" type="text/css"> 

<%      //Aqui va el codigo JAVA
        HttpSession sesion = request.getSession();                        
        ControladorInterface controlador = (ControladorInterface) sesion.getAttribute("controlador");
        VistaInterface vista = (VistaInterface) sesion.getAttribute("vista");
        if (controlador==null){
            controlador = new Controlador(new Modelo());
            sesion.setAttribute("controlador", controlador);
            sesion.setAttribute("vista", controlador.obtieneVista());
        }
        response.encodeURL("busqueda.jsp");
%>
    </header>
    
    <body>         
        <script> Messenger src="http://code.jquery.com/jquery.js"> </script>
        <script src="js/bootstrap.min.js"></script>
            
        <div id="contenedor">
          
            <div id="cabecera">

            <% if (controlador.obtieneUsuarioIdentificado()==null){%>
                <div id="login">
                <form action="Login" method="post" >
                    <input name="idUsuario" type="text" class="input-medium search-query" placeholder="Usuario">
                    <input name="clave" type="password" class="input-medium search-query" placeholder="Contraseña">            
                    <button type="submit" class="btn">Entrar</button>
                    <br>
                    <label class="checkbox">
                        <input type="checkbox"> Recordarme
                    </label> 
                </form>
                </div>    

                <div id="registrarse"> 
                    <a class="btn" href="Registrarse.jsp"> Registrarse </a>
                </div>    
            <% }else{ %>
                <div id="login">
                    <p > <%= controlador.obtieneUsuarioIdentificado().obtieneID() %> </p>                   
                    <button type="submit" class="btn">Cerrar Sesión</button>                    
                </div> 
            <% }   %>       
                <div id="logo">              
                  <a href="index.jsp"> <img src="img/Logo.png"> </a>                   
                </div>              
                  
                <div id="buscar">
                    <form action="busqueda.jsp" method="post">
                        <input name="consulta" type="text" class="input-xlarge search-query" placeholder="Ej. Los juegos del hambre">
                        <button type="submit" class="btn"> Buscar </button>      
                    </form>
                </div>
            </div>

            <div id="contenido">
                    <% 
            
                    String consulta = request.getParameter("consulta");
                    List<Pelicula> peliculas = null;
                    if (consulta != null){
                        vista.setDetallesBusqueda(consulta);
                        controlador.peticionBusquedaPeliculas();
                        peliculas = controlador.obtienePeliculasBuscadas();
                    }
                    if (peliculas != null){%>                    
                    <table border="1" width="100%" cellspacing="0" cellpadding="5">

                    <%
                            Iterator<Pelicula> iterator = peliculas.iterator(); 
                            for (int f=1; f<10 && iterator.hasNext();f++){ %>
                    <tr> 
<%                              for (int c=1; c<4 && iterator.hasNext();c++){
                                    Pelicula pelicula = iterator.next(); %> 
                        <td height="25%">                      
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > <img src="img/pelicula.png" ALT="Foto película"> </a>
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > <p><%= pelicula.obtieneTitulo() %></p> </a>
                            <p>Media: <%= pelicula.obtieneMedia() %></p>
                            <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>
                        </td>
                                <% }%>
                    </tr>  <% } %> 
                    </table>
                            
                <% }else{ %>                        
                    No se encontraron resultados o no introdujo ninguna consulta        
                  <%  } %> 
            </div>

            <div id="pie">
                
            </div>
          
          

        
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>
