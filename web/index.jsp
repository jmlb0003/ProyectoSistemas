<%-- 
    Document   : index
    Created on : 24-nov-2013, 18:30:17
    Author     : Sonia g
--%>

<%@page import="srccine.vista.Vista"%>
<%@page import="srccine.controlador.ErrorInicioSistema"%>
<%@page import="java.util.List"%>
<%@page import="srccine.modelo.Pelicula"%>
<%@page import="srccine.modelo.Recomendacion"%>
<%@page import="java.util.Iterator"%>
<%@page import="srccine.modelo.Usuario"%>
<%@page import="srccine.modelo.Modelo"%>
<%@page import="srccine.controlador.Controlador"%>
<%@page import="srccine.controlador.ControladorInterface"%>

<%--<html xmlns="http://www.w3.org/1999/xhtml">
<header>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Sistema de recomendaci&oacute;n de pel&iacute;culas</title>
<link rel="stylesheet"  href="bootstrap.css" type="text/css">
</header>

<body>
    <div>
       <H1 id="logo">  
       </H1>
    </div>

    <form action="login" method="POST">       
       <input class="botonEntrar" type="submit" value ="Entrar">
    </form>
     <br>
     <br>
     <form name="registrarse" method="post" action="register.jsp">
        <input class="registrarse" type="submit" value="Registrarse">
     </form>
     </center>
</body>
</html>--%>

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
        if (controlador==null){
            try{
                controlador = new Controlador(new Modelo());
                sesion.setAttribute("controlador", controlador);
                sesion.setAttribute("vista", controlador.obtieneVista());
            } catch (ErrorInicioSistema ex){
                Vista.notificarError(request, response, "error.jsp", "Inicio de sistema incompleto", "No se ha podido iniciar el sistema"+ex.getMessage());
            }
        }
        response.encodeURL("index.jsp");
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
                    <form action="IniciarSesion" name="IniciarSesion" method="post" >
                        <input type="hidden" name="url" value="<%= request.getServletPath()%>" >
                        <input id="usuario" name="idUsuario" type="text" class="input-medium search-query" 
                               placeholder="Usuario">
                        <input id="clave" name="clave" type="password" class="input-medium search-query" 
                               placeholder="Contraseña">            
                        <button onclick="validarLogin();" type="button" class="btn">Entrar</button>
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
                        <p><b> Bienvenido/a <%= controlador.obtieneUsuarioIdentificado().obtieneID() %> </b></p>
                        <input type="hidden" name="url" value="<%= request.getServletPath() %>" >
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
                <table border="1" width="100%" cellspacing="0" cellpadding="5">
                    <% if (controlador.obtieneUsuarioIdentificado()!=null){
                        Usuario usuario = controlador.obtieneUsuarioIdentificado();
                        if (usuario.obtieneRecomendaciones().size() > 0){
                            Iterator<Recomendacion> iterator = usuario.obtieneRecomendaciones().iterator(); 
                            for (int f=0; f<7 && iterator.hasNext();f++){ %>
                    <tr> 
<%                              for (int c=0; c<3 && iterator.hasNext();c++){
                                    Recomendacion recomendacion = iterator.next(); 
                                    Pelicula pelicula = recomendacion.obtienePelicula(); %> 
                        <td height="20%">                      
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" >   
                                <img id="poster<%=pelicula.obtieneID()%>" width="108px" height="141px">
                                <script>getImagenURL('<%=pelicula.obtieneTitulo()%>',<%=pelicula.obtieneID()%>)</script> 
                            </a>
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > <p><%= pelicula.obtieneTitulo() %></p></a>
                            <p>Media: <%= String.format("%.2f", pelicula.obtieneMedia()) %></p>
                            <p>Valoración:
                                <%  int valoracion = 0;
                                    if (controlador.obtieneUsuarioIdentificado().obtieneValoraciones().
                                            containsKey(pelicula.obtieneID())){
                                        valoracion=controlador.obtieneUsuarioIdentificado().obtieneValoraciones().
                                                get(pelicula.obtieneID()).obtienePuntuacion();
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
                    </tr>  <% }
                        }else{ //si no hay recomendaciones para el usuario                       
                        List mejoresPeliculas = controlador.obtieneMejoresPeliculas();
                        if (mejoresPeliculas!=null){
                        Iterator<Pelicula> iterator = mejoresPeliculas.iterator(); 
                        for (int f=0; f<7 && iterator.hasNext();f++){ %>
                    <tr> 
<%                          for (int c=0; c<3 && iterator.hasNext();c++){ 
                                Pelicula pelicula = iterator.next(); %> 
                        <td height="20%">                      
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > 
                                <img id="poster<%=pelicula.obtieneID()%>" width="108px" height="141px">
                                <script>getImagenURL('<%=pelicula.obtieneTitulo()%>',<%=pelicula.obtieneID()%>)</script> 
                            </a>
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > <p><%= pelicula.obtieneTitulo() %></p> </a>
                            <p>Media: <%= String.format("%.2f", pelicula.obtieneMedia()) %></p>
                            <p>Valoración:
                                <%  int valoracion = 0;
                                    if(controlador.obtieneUsuarioIdentificado()!=null){ 
                                        if (controlador.obtieneUsuarioIdentificado().obtieneValoraciones().containsKey(pelicula.obtieneID())){
                                            valoracion=controlador.obtieneUsuarioIdentificado().obtieneValoraciones().get(pelicula.obtieneID()).obtienePuntuacion();
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
                    <%      } %>
                    </tr>  
                <%      }
                    }
                } 
            //No hay usuario identificado
            }else{
                    List mejoresPeliculas = controlador.obtieneMejoresPeliculas();
                    if (mejoresPeliculas!=null){
                    Iterator<Pelicula> iterator = mejoresPeliculas.iterator(); 
                    for (int f=0; f<7 && iterator.hasNext();f++){ %>
                    <tr> 
<%                      for (int c=0; c<3 && iterator.hasNext();c++){
                            Pelicula pelicula = iterator.next(); %> 
                        <td height="25%">                      
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > 
                                <img id="poster<%=pelicula.obtieneID()%>" width="108px" height="141px">
                                <script>getImagenURL('<%=pelicula.obtieneTitulo()%>',<%=pelicula.obtieneID()%>)</script> 
                            </a>                                                                               
                            <a href="pelicula.jsp?id=<%=pelicula.obtieneID()%>" > <p><%= pelicula.obtieneTitulo() %></p> </a>
                            <p>Media: <%= String.format("%.2f", pelicula.obtieneMedia()) %></p>
                            <p>Valoración:
                            <%
                            int valoracion=0;
                            //Obtenemos la valoración  
                            for(int i=1;i<=valoracion;i++){ %>
                            <img src="img/estrellaAmarilla.png" ALT="valoracion">
                        <%  }
                            for(int i=1;i<=5-valoracion;i++){ %>
                            <img src="img/estrellaGris.png" ALT="valoracion">
                        <%  } %>
                            </p>     
                        </td>
                    <% } %>
                    </tr>  <% }
                        }
                    } %> 
               </table>
            </div>

            <div id="pie">
                
            </div>
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>
