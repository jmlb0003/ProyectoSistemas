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
    //private Map<String,Usuario> _usuariosTest;
    
    /**
     * Constructor por defecto
     */
    FicheroCSV(){
        _usuarios = new HashMap();
        _peliculas = new HashMap();
        _valoraciones = new ArrayList();
        //_usuariosTest = new HashMap();
    }
    
    /**
     * Lee el fichero CSV para realizar las pruebas de los algoritmos
     * @param particion particion que se debe leer
     * @throws ErrorLecturaFichero Error en la lectura del fichero
     *
    void leerCSVTest(int particion) throws ErrorLecturaFichero{
        _usuarios.clear();
        _peliculas.clear();
        _valoraciones.clear();
        _usuariosTest.clear();
        
        leerFicheroPeliculas();
        
        switch(particion){
            case 1:
                leerFicheroValoraciones("recursos/csv/ratings3-1.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-2.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-3.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-4.csv");

                leerFicheroValoracionesTest("recursos/csv/ratings3-5.csv");
                break;
            case 2:
                leerFicheroValoraciones("recursos/csv/ratings3-2.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-3.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-4.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-5.csv");

                leerFicheroValoracionesTest("recursos/csv/ratings3-1.csv");
                break;            
            case 3:
                leerFicheroValoraciones("recursos/csv/ratings3-3.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-4.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-5.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-1.csv");

                leerFicheroValoracionesTest("recursos/csv/ratings3-2.csv");
                break;            
            case 4:
                leerFicheroValoraciones("recursos/csv/ratings3-4.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-5.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-1.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-2.csv");

                leerFicheroValoracionesTest("recursos/csv/ratings3-3.csv");
                break;
            case 5:
                leerFicheroValoraciones("recursos/csv/ratings3-5.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-1.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-2.csv");
                leerFicheroValoraciones("recursos/csv/ratings3-3.csv");

                leerFicheroValoracionesTest("recursos/csv/ratings3-4.csv");
                break;
        }    
    }*/
    
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
                detallesPelicula.put("valoraciones", new HashMap<Long, Valoracion>());
                detallesPelicula.put("suma", (long) 0);
                detallesPelicula.put("media", (double) 0.0);
                
                //Creamos la pelicula, sin que se persista en la base de datos
                Pelicula pelicula = new Pelicula(id, detallesPelicula);
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
                    Map<String, Object> detallesUsuario = new HashMap<>();
                    detallesUsuario.put("clave", String.valueOf(idUsuario));                    
                    detallesUsuario.put("valoraciones", new HashMap<Long,Valoracion>());
                    detallesUsuario.put("suma", (long)0);
                    Usuario usuario = new Usuario(idUsuario, detallesUsuario);                    
                    _usuarios.put(idUsuario, usuario);
                }
                
                //Si no hubo error en la lectura del fichero añadimos la valoracion
                if (idPelicula != -1 && puntuacion != -1){
                    if (_peliculas.containsKey(idPelicula)){
                        Valoracion v = new Valoracion(puntuacion,idUsuario,idPelicula);
                        _valoraciones.add(v);

                        //Obtenemos y modificamos la pelicula actualizando la suma y la media
                        Pelicula pelicula = _peliculas.get(idPelicula);
                        Map mapaV = (Map<String, Valoracion>) pelicula.obtieneDetalles().
                                obtieneDetalles().get("valoraciones");
                        mapaV.put(idUsuario, v);
                        long suma = (long) pelicula.obtieneDetalles().obtieneDetalles().get("suma");
                        suma = suma + puntuacion;
                        pelicula.obtieneDetalles().obtieneDetalles().put("suma", suma);
                        double media =  (double)suma / mapaV.size();
                        pelicula.obtieneDetalles().obtieneDetalles().put("media", media);  

                        //Obtenemos y modificamos el usuario actualizando la suma y la media
                        Usuario usuario = _usuarios.get(idUsuario);
                        Map mapaV2 = (Map<Long, Valoracion>)usuario.obtieneDetalles().
                                obtieneDetalles().get("valoraciones");
                        mapaV2.put(idPelicula,v);
                        suma = (long) usuario.obtieneDetalles().obtieneDetalles().get("suma");
                        suma = suma + puntuacion;
                        usuario.obtieneDetalles().obtieneDetalles().put("suma", suma);
                        media =  (double)suma / mapaV2.size();
                        usuario.obtieneDetalles().obtieneDetalles().put("media", media);
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

    /**
     * Extrae las peliculas de la particion de test del fichero de valoraciones
     * @throws ErrorLecturaFichero Error al leer el fichero
     *
    private void leerFicheroValoracionesTest(String nombre) throws ErrorLecturaFichero {
      BufferedReader br = null; 
        
        try {
            //Abrimos el fichero de peliculas
            URL url = this.getClass().getClassLoader().getResource(nombre);
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
                
                try{
                    puntuacion = Integer.parseInt(split[2].trim());
                }catch (NumberFormatException e){
                    puntuacion = -1;
                }
                
                String idUsuario = split[0];
                if (!_usuariosTest.containsKey(idUsuario)){
                    Map<String, Object> detallesUsuario = new HashMap<>();
                    detallesUsuario.put("clave", String.valueOf(idUsuario));                    
                    detallesUsuario.put("suma", (long)0);
                    detallesUsuario.put("valoraciones", new HashMap<Long,Valoracion>());
                    Usuario usuario = new Usuario(idUsuario, detallesUsuario);                    
                    _usuariosTest.put(idUsuario, usuario);
                }
                
                //Si no hubo error en la lectura del fichero añadimos la valoracion
                if (idPelicula != -1 && puntuacion != -1){
                    if (_peliculas.containsKey(idPelicula)){
                        Valoracion v = new Valoracion(puntuacion,idUsuario,idPelicula);
                        _valoraciones.add(v);

                        //Obtenemos y modificamos el usuario actualizando la suma y la media
                        Usuario usuario = _usuariosTest.get(idUsuario);
                        Map mapaV2 = (Map<Long, Valoracion>)usuario.obtieneDetalles().
                                obtieneDetalles().get("valoraciones");
                        mapaV2.put(idPelicula,v);
                        long suma = (long) usuario.obtieneDetalles().obtieneDetalles().get("suma");
                        suma = suma + puntuacion;
                        usuario.obtieneDetalles().obtieneDetalles().put("suma", suma);
                        double media =  (double)suma / mapaV2.size();
                        usuario.obtieneDetalles().obtieneDetalles().put("media", media);
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
    }*/
       
    /**
     * Devuelve una lista con las claves de los usuarios incluidos en la particion de test
     * @return List<String> Usuarios de la particion de test
     *
    List<String> getClavesUsuariosTest() {
        return new ArrayList(_usuariosTest.keySet());
    } */   
    
    /**
     * Devuelve una lista con los usuarios incluidos en la particion de test
     * @return List<Usuario> Usuarios de la particion de test
     *
    List<Usuario> getUsuariosTest() {
        return new ArrayList(_usuariosTest.values());
    }*/
    
}
