package srccine.modelo;

import srccine.modelo.excepciones.ErrorLecturaFichero;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que encarga de leer los ficheros CSV e importarlos a memoria.
 * @author Jesús
 */
class FicheroCSV{
    
    //Contenedores de usuarios, peliculas y valoraciones
    private Map<String, Usuario> _usuarios;
    private Map<Long, Pelicula> _peliculas;
    private List<Valoracion> _valoraciones;
    
    /**
     * Constructor por defecto
     */
    FicheroCSV(){
        _usuarios = new HashMap();
        _peliculas = new HashMap();
        _valoraciones = new ArrayList();
    }
    
    /**
     * Lee los ficheros CSV y guarda las EEDD en memoria
     * @throws ErrorLecturaFichero Error al leer el fichero
     */
    void leerCSV() throws ErrorLecturaFichero{
        //Lee los ficheros de peliculas y valoraciones
        leerFicheroPeliculas();
        leerFicheroValoraciones();        
    }
    
    /**
     * Lee el fichero de peliculas y lo guarda en memoria
     * @throws ErrorLecturaFichero Error al leer el fichero
     */
    private void leerFicheroPeliculas () throws ErrorLecturaFichero{
        
        BufferedReader br = null;
        
        try {
            //Abrimos el fichero de peliculas
            URL url = this.getClass().getClassLoader().getResource("srccine/recursos/csv/peliculas.csv");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            //Leemos la primera linea donde contiene los titulos de las columnas
            String linea = br.readLine();            
            
            //Leemos todas las peliculas del fichero
            while ((linea = br.readLine()) != null){
                //Obtenemos los atributos de cada linea
                String[] split = linea.split(",");
                Map<String, Object> detallesPelicula = new HashMap();
                long ano, id;
                
                //Extraemos el id y el año de lanzamiento, comprobando que no 
                //haya problemas en la conversion
                try{
                    ano = Long.parseLong(split[1].trim());
                }catch (NumberFormatException e){
                    ano = -1;
                }                
                try{
                    id = Long.parseLong(split[0].trim());
                }catch (NumberFormatException e){
                    id = -1;
                }

                //Insertamos los atributos en el mapa
                detallesPelicula.put("ano", ano);                
                detallesPelicula.put("titulo",split[2].trim());
                
                //Creamos la pelicula, sin que se persista en la base de datos
                Pelicula pelicula = new Pelicula(id,split[2].trim(), detallesPelicula);
                _peliculas.put(id, pelicula);                  
            }
            
        } catch (FileNotFoundException ex){
            throw new ErrorLecturaFichero();
        }catch ( IOException ex) {
            throw new ErrorLecturaFichero();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                throw new ErrorLecturaFichero();
            }
        }        
    }    
    
    /**
     * Lee el fichero de ratings y crea las valoraciones y las inserta 
     * en usuarios y peliculas 
     * @throws ErrorLecturaFichero Error al leer el fichero
     */
    private void leerFicheroValoraciones () throws ErrorLecturaFichero{
        
        BufferedReader br = null; 
        
        try {
            //Abrimos el fichero de peliculas
            URL url = this.getClass().getClassLoader().getResource("srccine/recursos/csv/ratings3.csv");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            //Leemos la primera linea donde contiene los titulos de las columnas
            String linea = br.readLine();            
            
            //Leemos todas las peliculas del fichero
            while ((linea = br.readLine()) != null){
                //Obtenemos los atributos de cada linea
                String[] split = linea.split(",");
                long idPelicula;
                int puntuacion;
                
                //Extraemos el id y el año de lanzamiento, comprobando que no 
                //haya problemas en la conversion
                try{
                    idPelicula = Long.parseLong(split[1].trim());
                }catch (NumberFormatException e){
                    idPelicula = -1;
                }                
                String idUsuario = split[0].trim();
                
                try{
                    puntuacion = Integer.parseInt(split[2].trim());
                }catch (NumberFormatException e){
                    puntuacion = -1;
                }
                
                if (!_usuarios.containsKey(idUsuario)){
                    Map<String, Object> detallesUsuario = new HashMap();
                    detallesUsuario.put("clave", String.valueOf(idUsuario));                    
                    Usuario usuario = new Usuario(idUsuario, detallesUsuario);                    
                    _usuarios.put(idUsuario, usuario);
                }
                
                //Si no hubo error en la lectura del fichero añadimos la valoracion
                if (idPelicula != -1 && puntuacion != -1){
                    if (_peliculas.containsKey(idPelicula)){
                        Valoracion v = new Valoracion(puntuacion,idUsuario,idPelicula);
                        _valoraciones.add(v);

                        //Añadimos la valoracion a la pelicula
                        Pelicula pelicula = _peliculas.get(idPelicula);
                        pelicula.anadeValoracion(idUsuario, v);                         

                        //Añadimos la valoracion al usuario
                        Usuario usuario = _usuarios.get(idUsuario);
                        usuario.anadeValoracion(idPelicula,v);
                    }                    
                }
            }
            
        } catch (FileNotFoundException ex) {
            throw new ErrorLecturaFichero();
        } catch (IOException ex) {
            throw new ErrorLecturaFichero();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                throw new ErrorLecturaFichero();
            }
        }

    }

    /**
     * Devuelve los usuarios extraidos del CSV
     * @return Map<String, Usuario> con los usuarios encontrados
     */
    Map<String, Usuario> getUsuarios() {
        return _usuarios;
    }

    /**
     * Devuelve las peliculas extraidas del CSV
     * @return Map<Long, Pelicula> peliculas leidas
     */
    Map<Long, Pelicula> getPeliculas() {
        return _peliculas;
    }

    /**
     * Devuelve las valoraciones extraidas
     * @return List Valoraciones leidas
     */
    List getValoraciones() {
        return _valoraciones;
    }    
}
