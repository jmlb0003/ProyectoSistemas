<%-- 
    Document   : index
    Created on : 24-nov-2013, 18:30:17
    Author     : Sonia g
--%>

<%@page import="pr.controlador.Controlador"%>
<%@page import="pr.controlador.ControladorInterface"%>
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
                <table border="1" width="100%" cellspacing="0" cellpadding="5">
                 <tr>
                   <td height="33%">                      
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Los juegos del hambre </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>                     
                   </td>
                   
                     <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Somos los miller </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                     </td>
                     
                     <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Don Jon </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                     </td>
                   
                 </tr>
                    
                 <tr>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Thor 2 </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>       
                   </td>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Titanic </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                   </td>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Scary Movie 3 </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                   </td>
                 </tr>
                  
                 <tr>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Saw 4 </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                   </td>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Prisioneros </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>   
                   </td>
                   <td height="33%">
                       <img src="img/pelicula.png" ALT="Foto película"> 
                       <p> Titulo: Avatar </p>    
                       <p> Valoración: <img src="img/sinvalorar.png" ALT="valoracion"></p>  
                   </td>
                     
                 </tr>
                  
                    
               </table>
            </div>

            <div id="pie">
                
            </div>
          
          

        
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>
