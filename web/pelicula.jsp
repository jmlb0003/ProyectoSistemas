<%-- 
    Document   : Pelicula
    Created on : 14-dic-2013, 13:25:12
    Author     : Sonia
--%>

<%@page import="java.net.URL"%>
<%@page import="srccine.vista.Vista"%>
<%@page import="srccine.controlador.ErrorInicioSistema"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="srccine.vista.VistaInterface"%>
<%@page import="srccine.modelo.Pelicula"%>
<%@page import="srccine.modelo.Recomendacion"%>
<%@page import="java.util.Iterator"%>
<%@page import="srccine.modelo.Usuario"%>
<%@page import="srccine.modelo.Modelo"%>
<%@page import="srccine.controlador.Controlador"%>
<%@page import="srccine.controlador.ControladorInterface"%>
<%@page import="srccine.controlador.ControladorInterface"%>
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
        
        <script type="text/javascript" src="jQuery/jquery-2.0.3.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/srccine.js"></script>         
        
<%      //Aqui va el codigo JAVA
        HttpSession sesion = request.getSession();                        
        ControladorInterface controlador = (ControladorInterface) sesion.getAttribute("controlador");
        VistaInterface vista = (VistaInterface) sesion.getAttribute("vista");
        if (controlador==null){
            try{
                controlador = new Controlador(new Modelo());
                sesion.setAttribute("controlador", controlador);
                sesion.setAttribute("vista", controlador.obtieneVista());
            } catch (ErrorInicioSistema ex){
                Vista.notificarError(request, response, "error.jsp", "Inicio de sistema incompleto", "No se ha podido iniciar el sistema"+ex.getMessage());
            }
        }
        response.encodeURL("pelicula.jsp");
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Cache-Control","no-cache"); 
        response.setHeader("Pragma","no-cache"); 
        response.setDateHeader ("Expires", -1); %>
                
    </header>
    
    <body>         
            
        <div id="contenedor">
          
            <div id="cabecera">

            <% if (controlador.obtieneUsuarioIdentificado()==null){%>
                <div id="login">
                <form action="IniciarSesion" method="post" >
                    <input type="hidden" name="url" value="<%= request.getServletPath()+"?id="+request.getParameter("id") %>" >
                    <input name="idUsuario" type="text" class="input-medium search-query" placeholder="Usuario">
                    <input name="clave" type="password" class="input-medium search-query" placeholder="Contraseña">            
                    <button onclick="validarLogin();" type="submit" class="btn">Entrar</button>
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
                    <form action="CerrarSesion" method="post">
                        <p> Bienvenido/a <%= controlador.obtieneUsuarioIdentificado().obtieneID() %> </p>                   
                        <input type="hidden" name="url" value="<%= request.getServletPath()+"?id="+request.getParameter("id") %>" >
                        <button type="submit" class="btn">Cerrar Sesión</button> 
                    </form>
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
                <table border="0" width="100%" cellspacing="0" cellpadding="40">
                    <tr>
                    <% if (request.getParameter("id")!=null){
                            long id;
                            try{
                                id = Long.parseLong(request.getParameter("id"));
                                vista.setPeliculaSeleccionada(id);
                                controlador.peticionVerInformacionPelicula();
                                Pelicula pelicula = controlador.obtienePeliculaSeleccionada();
                                if (pelicula!=null){  %> 
                        <td width="50%" align="right">
                            <div im>
                                <img id="poster0" width="303px" height="448px">
                                <script>getImagenURL('<%=pelicula.obtieneTitulo()%>',0)</script>
                            </div>
                        </td>
                        <td width="50%" align="left">                      
                                <left>                                                              
                                </b><font size=6 color=#5882FA><%= pelicula.obtieneTitulo() %></font> 
                                </br>
                                </br>                

                                <b>Sinopsis: </b>
                            <%  if (pelicula.obtieneDetalles().obtieneDetalles().containsKey("sipnosis")){ %>
                            <%= pelicula.obtieneDetalles().obtieneDetalles().get("sipnosis") %>
                            <%  }else{%>
                                &Eacute;ste es el argumento de la pel&iacute;cula.
                                <% } %>
                                </br>
                                </br> 
                                <b>Fecha Lanzamiento: </b>
                                <p id="fecha_lanzamiento"> </p>
                                </br>
                                <form method="post" action="Valorar">
                                    <input type="hidden" name="idPelicula" value="<%= pelicula.obtieneID()%>" >
    <%                          if (controlador.obtieneUsuarioIdentificado()!=null){
                                    int antV = 0;
                                    if (controlador.obtieneUsuarioIdentificado().obtieneValoraciones().containsKey(id)){
                                        antV = controlador.obtieneUsuarioIdentificado().obtieneValoraciones().get(id).obtienePuntuacion();
                                    }
                                    for (int i=1; i<=5; i++){
                                        if (antV == i){ %>
                                    <input checked="true" type="radio" name="puntuacion" value="<%= i%>" id="<%= i%>"><%= i%>
                                    <%  }else{ %>
                                    <input type="radio" name="puntuacion" value="<%= i%>" id="<%= i%>"><%= i%>
                                    <%  }
                                    }
                                    if (controlador.obtieneUsuarioIdentificado().obtieneValoraciones().containsKey(id)){ %>
                                    <button type="submit" class="btn">Cambiar Valoracion</button>
                            <%      }else{%>
                                    <button type="submit" class="btn">Valorar</button>
                            <%      }
                                } else{
                                    for (int i=1; i<=5; i++){ %>
                                    <input type="radio" name="puntuacion" value="<%= i%>" id="<%= i%>"><%= i%>
                                    <%  
                                    } %>
                                    <button type="submit" class="btn" disabled>Valorar</button>
                            <%  } %>
                                </form>
                                <b>Media: </b> <%= String.format("%.2f", pelicula.obtieneMedia()) %> <img src="img/estrellaAmarilla.png" ALT="valoracion">
                                </br>                                               
                                <% 
                                Map detalles = pelicula.obtieneDetalles().obtieneDetalles();
                                Iterator it = detalles.entrySet().iterator();
                                while (it.hasNext()) {
                                    Entry entry = (Entry) it.next();
                                    if (! entry.getKey().equals("titulo")){
                                    %>
                                    <b><% if(entry.getKey().equals("ano")){ 
                                       %>
                                          Año:
                                       <%
                                          } else{
                                            entry.getKey();                                         
                                          }
                                        %> </b><%= entry.getValue() %><br><br>                                         
                                <%
                                    }
                                }
                            } %> 
                            <div trailer>
                                <iframe id="trailer0" width="330" height="186" frameborder="0" allowfullscreen></iframe>
                            </div> 
                                
                                </left>
                        <%  }catch (NumberFormatException e){
                                id = -1;
                            }
                            }else{ %>
                                Acceso invalido.
                        <%  }
                            %>
                    </td></tr> 
                </table>
            </div>

            <div id="pie">
                
            </div>
          
          

        
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>

