<%-- 
    Document   : Registrarse 
    Created on : 30-nov-2013, 12:18:17
    Author     : Sonia g
--%>   

<%@page import="java.util.GregorianCalendar"%>
<%@page import="srccine.vista.VistaInterface"%>
<%@page import="srccine.modelo.Modelo"%>
<%@page import="srccine.controlador.Controlador"%>
<%@page import="srccine.controlador.ControladorInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <header>
        <title>Registro</title>   
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap -->
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">    
            
        <!-- Mis estilos -->   
        <link rel="stylesheet"  href="css/estilos.css" type="text/css"> 
<%
        //Aqui va el codigo JAVA
        HttpSession sesion = request.getSession();                         
        ControladorInterface controlador = (ControladorInterface) sesion.getAttribute("controlador");
        if (controlador==null){
            controlador = new Controlador(new Modelo());
            sesion.setAttribute("controlador", controlador);
            sesion.setAttribute("vista", controlador.obtieneVista());
        }
        response.encodeURL("Registrarse.jsp");

        
%>            
    </header>
    <body>
                   
        <script> Messenger src="http://code.jquery.com/jquery.js"> </script>
        <script src="js/bootstrap.min.js"></script>
            
        <div id="contenedor">
          
            <div id="cabecera">
            <% if (controlador.obtieneUsuarioIdentificado()==null){%>
                <form action="Login" method="post" >
                    <input name="idUsuario" type="text" class="input-medium search-query" placeholder="Usuario">
                    <input name="clave" type="password" class="input-medium search-query" placeholder="Contraseña">            
                    <button type="submit" class="btn">Entrar</button>
                    <br>
                    <label class="checkbox">
                        <input type="checkbox"> Recordarme
                    </label> 
                </form>   

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
            <% if (controlador.obtieneUsuarioIdentificado() == null){%>
          
            <div id="formular">
                <form action="Registro" method="post" >
                    <br>
                    <h3> Crear un nuevo usuario: </h3>   
                    <input name="idUsuario" type="text"  aling="left" class="input-large search-query" placeholder="Identificador de Usuario">              
                    <br>
                    <br>
                    <input name="nombre" type="text" class="input-large search-query" placeholder="Nombre">
                    <br>
                    <br>
                    <input name="apellidos" type="text" class="input-large search-query" placeholder="Apellidos">
                    <br>
                    <br>
                    <select name="dia" class="form-control input-mini">
                    <% for (int i=1;i<=31;i++){ %>
                        <option value= <%= i %>> <%= i %> </option>
                    <%}%>
                    </select>

                    <select name="mes" class="form-control input-mini">                  
                    <% for (int i=1;i<=12;i++){ %>
                        <option value= <%= i %>> <%= i %> </option>
                    <%}%>  
                    </select>

                    <select name="ano" class="form-control input-small">                  
                        <% for (int i=new GregorianCalendar().get(GregorianCalendar.YEAR);i>=1900;i--){ %>
                        <option  value= <%= i %>> <%= i %> </option>
                    <%}%>  
                    </select>
                    <br>
                    <br>
                    <input name="clave" type="password" class="input-large search-query" placeholder="Contraseña"> 
                    <br>
                    <br>
                    <center>              
                    <a class="btn" href="index.jsp">Salir</a>    
                    <button type="submit" class="btn">Aceptar</button>                                       
                    </center>
                </form>                              
              </div>
        <% }else{ %>
                Ya esta registrado.
        <% } %>
            </div>    
                                
            <div id="pie">
                
            </div>
      </div>  
    </body>
</html>
