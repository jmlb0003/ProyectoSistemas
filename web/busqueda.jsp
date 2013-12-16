<%-- 
    Document   : busqueda de peliculas
    Created on : 14-dic-2013, 1:24:25
    Author     : Jesus
--%>

<%@page import="srccine.vista.Vista"%>
<%@page import="srccine.controlador.ErrorInicioSistema"%>
<%@page import="java.util.Map"%>
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
        response.encodeURL("busqueda.jsp");
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
                    <form action="CerrarSesion" method="post">
                        <p> Bienvenido/a <%= controlador.obtieneUsuarioIdentificado().obtieneID() %> </p>                   
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
                    <% 
            if (request.getParameter("consulta") != null){
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
                            <p>Media: <%= String.format("%.2f", pelicula.obtieneMedia()) %></p>
                            <p>Valoración:
                                <%  int valoracion = 0;
                                    if(controlador.obtieneUsuarioIdentificado()!=null){ 
                                        if (controlador.obtieneUsuarioIdentificado().obtieneValoraciones().containsKey(pelicula.obtieneID())){
                                            valoracion=controlador.obtieneUsuarioIdentificado().obtieneValoraciones().get(pelicula.obtieneID()).getPuntuacion();
                                        }
                                    }
                                    //Obtenemos la valoración  
                                    for(int i=1;i<=valoracion;i++){ %>
                                    <img src="img/estrellaAmarilla.png" ALT="valoracion">
                                <%  }
                                    for(int i=1;i<=5-valoracion;i++){ %>
                                    <img src="img/estrellaGris.png" ALT="valoracion">
                                <%  } %>                   
                            </p>  
                        </td>
                                <% }%>
                    </tr>  <% } %> 
                    </table>
                            
                <%  }else{ %>                        
                    No se encontraron resultados o no introdujo ninguna consulta        
                <%  } 
            }else{ 
                Map mapa = request.getParameterMap();
                Iterator it= mapa.entrySet().iterator();
                while (it.hasNext()){%> 
                <%= it.next()%> 
            <%  }
            } %>
            </div>

            <div id="pie">
                
            </div>
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>
