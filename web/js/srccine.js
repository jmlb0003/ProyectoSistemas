    function getImagenURL(titulo, id){        
        $.getJSON("http://api.themoviedb.org/3/search/movie?api_key=05c9544702dc546bbe3cac04d5e55356&query="+titulo, 
            function(json) {
                document.getElementById("poster"+id).src="img/pelicula.png";
                document.getElementById("poster"+id).src = "https://image.tmdb.org/t/p/w342/"+json.results[0].poster_path;
                document.getElementById("fecha_lanzamiento").textContent = json.results[0].release_date;
                getTrailerURL(json.results[0].id, id)
                
        });    
    }    
    
    function getTrailerURL(idPelicula, id){        
        $.getJSON("http://api.themoviedb.org//3/movie/"+idPelicula+"/trailers?api_key=05c9544702dc546bbe3cac04d5e55356", 
            function(json) {
                if (json.youtube.length >0 ){
                    document.getElementById("trailer"+id).src = "//www.youtube.com/embed/"+json.youtube[0].source;
                }else{
                    document.getElementById("trailer"+id).style.visibility = "hidden";
                }
        });    
    }
    
    function validarLogin(){                                  

        if(document.getElementById('usuario').value.length!==0 && document.getElementById('clave').value.length!==0){
                document.getElementById('usuario').style.borderColor="#D8D8D8";                      
                document.getElementById('clave').style.borderColor="#D8D8D8";
                document.IniciarSesion.submit();
        } 

        if(document.getElementById('usuario').value.length==0 && document.getElementById('clave').value.length!==0){
                document.getElementById('usuario').style.borderColor="red";                      
                document.getElementById('clave').style.borderColor="#D8D8D8";
        } 

        if(document.getElementById('usuario').value.length!==0 && document.getElementById('clave').value.length==0){
                document.getElementById('usuario').style.borderColor="#D8D8D8";                      
                document.getElementById('clave').style.borderColor="red";
        }               

        if(document.getElementById('usuario').value.length==0 && document.getElementById('clave').value.length==0){
                document.getElementById('usuario').style.borderColor="red";                      
                document.getElementById('clave').style.borderColor="red";
        }
    } 

    function validarRegistro(){                                  

        if(document.getElementById('idUsuario').value.length!==0 && document.getElementById('nombre').value.length!==0 && document.getElementById('contrasena').value.length!==0){
            document.getElementById('idUsuario').style.borderColor="#D8D8D8";
            document.getElementById('nombre').style.borderColor="#D8D8D8";
            document.getElementById('contrasena').style.borderColor="#D8D8D8";
            document.formulario.submit();
        }

        if(document.getElementById('idUsuario').value.length==0 && document.getElementById('nombre').value.length!==0 && document.getElementById('contrasena').value.length!==0){
             document.getElementById('contrasena').style.borderColor="#D8D8D8";
             document.getElementById('nombre').style.borderColor="#D8D8D8";
             document.getElementById('idUsuario').style.borderColor="red";   
        }

        if(document.getElementById('idUsuario').value.length!==0 && document.getElementById('nombre').value.length==0 && document.getElementById('contrasena').value.length!==0){
            document.getElementById('idUsuario').style.borderColor="#D8D8D8";
            document.getElementById('contrasena').style.borderColor="#D8D8D8";
            document.getElementById('nombre').style.borderColor="red";
        }

        if(document.getElementById('idUsuario').value.length!==0 && document.getElementById('nombre').value.length!==0 && document.getElementById('contrasena').value.length==0){
            document.getElementById('idUsuario').style.borderColor="#D8D8D8";
            document.getElementById('nombre').style.borderColor="#D8D8D8";
            document.getElementById('contrasena').style.borderColor="red"; 
        }

        if(document.getElementById('idUsuario').value.length==0 && document.getElementById('nombre').value.length==0 && document.getElementById('contrasena').value.length==0){
            document.getElementById('idUsuario').style.borderColor="red";
            document.getElementById('nombre').style.borderColor="red";
            document.getElementById('contrasena').style.borderColor="red"; 
        }

        if(document.getElementById('idUsuario').value.length!==0 && document.getElementById('nombre').value.length==0 && document.getElementById('contrasena').value.length==0){
            document.getElementById('idUsuario').style.borderColor="#D8D8D8";
            document.getElementById('nombre').style.borderColor="red";
            document.getElementById('contrasena').style.borderColor="red"; 
        }

        if(document.getElementById('idUsuario').value.length==0 && document.getElementById('nombre').value.length==0 && document.getElementById('contrasena').value.length!==0){
            document.getElementById('idUsuario').style.borderColor="red";
            document.getElementById('nombre').style.borderColor="red";
            document.getElementById('contrasena').style.borderColor="#D8D8D8"; 
        }

        if(document.getElementById('idUsuario').value.length==0 && document.getElementById('nombre').value.length!==0 && document.getElementById('contrasena').value.length==0){
            document.getElementById('idUsuario').style.borderColor="red";
            document.getElementById('nombre').style.borderColor="#D8D8D8";
            document.getElementById('contrasena').style.borderColor="red"; 
        }          

    }         

