<%-- 
    Document   : Registrarse
    Created on : 30-nov-2013, 12:18:17
    Author     : Sonia g
--%>

<%@page import="pr.controlador.Controlador"%>
<%@page import="pr.controlador.ControladorInterface"%>
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
            controlador = new Controlador();
            sesion.setAttribute("controlador", controlador);
        }
        
%>            
    </header>
    <body>
                   
            <script> Messenger src="http://code.jquery.com/jquery.js"> </script>
            <script src="js/bootstrap.min.js"></script>
            
      <div id="contenedor">
          
            <div id="cabecera">
                <div id="login">                   
                   
                  <input type="text" class="input-medium search-query" placeholder="Usuario">
                  <input type="password" class="input-medium search-query" placeholder="Contraseña">            
                  <button type="submit" class="btn">Entrar</button>
                  <br>
                  <label class="checkbox">
                    <input type="checkbox"> Recordarme
                  </label>
                    
                </div>    
            
                <div id="registrarse"> 
                    <a class="btn" href="Registrarse.jsp"> Registrarse </a>
                </div>   
                
                <div id="logo">              
                  <a href="index.jsp"> <img src="img/Logo.png"> </a>                 
                </div>              
                  
                <div id="buscar">              
                  <input type="text" class="input-xlarge search-query" placeholder="Ej. Los juegos del hambre">
                  <button type="submit" class="btn"> Buscar </button>                   
                </div>
            </div>

          
          
          
          
            <div id="contenido">                
          
             <div id="formular">
                <br>
                <br>
                 <h3> Crear un nuevo usuario: </h3>
   
                  <input type="text"  aling="left" class="input-large search-query" placeholder="Identificador de Usuario">              
                  <br>
                  <br>
                  <input type="text" class="input-large search-query" placeholder="Nombre">
                  <br>
                  <br>
                  <input type="text" class="input-large search-query" placeholder="Apellidos">
                  <br>
                  <br>
                  

                  <select class="form-control input-mini">
                     <% for (int i=1;i<=31;i++){ %>
                    <option value= <%= i %>> <%= i %> </option>
                    <%}%>
                  </select>
                  
                  <select class="form-control input-mini">                  
                    <% for (int i=1;i<=12;i++){ %>
                    <option value= <%= i %>> <%= i %> </option>
                    <%}%>  
                  </select>
          
                  <select class="form-control input-small">                  
                    <% for (int i=2013;i>=1900;i--){ %>
                        <option value= <%= i %>> <%= i %> </option>
                    <%}%>  
                  </select>
                
                  <br>
                  <br>
                  <input type="password" class="input-large search-query" placeholder="Contraseña"> 
                  <br>
                  <br>

               
                  
                 <center>              
                 <a class="btn" href="index.jsp">Salir</a>    
                 <button type="submit" class="btn">Aceptar</button>                                       
                 
                 </center>
                            
              </div>
                                
            </div>    
                                
                                
                                
            <div id="pie">
                
            </div>
          
          

        
      </div>

  
    </body>
</html>
