<%-- 
    Document   : error
    Created on : 24-nov-2013, 18:30:17
    Author     : Sonia ga
--%>

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
        
    </header>
    
    <body>         
            
        <div id="contenedor">
          
            <div id="cabecera">  
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
                <%= request.getParameter("titulo")%> <br>
                <%= request.getParameter("mensaje")%>                
            </div>

            <div id="pie">
                
            </div>
      </div>

     <footer>
         
     </footer>
  
    </body>
</html>
