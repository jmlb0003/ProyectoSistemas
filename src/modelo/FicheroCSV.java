package modelo;

import modelo.excepciones.ErrorLecturaFichero;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.algoritmos.AlgSimilitud;
import modelo.persistencia.DAOPelicula;
import modelo.persistencia.DAOUsuario;
import modelo.persistencia.DAOValoracion;
import modelo.persistencia.excepciones.ErrorInsertarPelicula;
import modelo.persistencia.excepciones.ErrorInsertarUsuario;
import modelo.persistencia.excepciones.ErrorInsertarValoracion;

/**
 *
 * @author Jesús
 */
class FicheroCSV{
    
    //Contenedores de usuarios, peliculas y valoraciones
    private HashMap<String, Usuario> _usuarios;
    private HashMap<Long, Pelicula> _peliculas;    
    private List _valoraciones;
    
    /**
     * Constructor por defecto
     */
    FicheroCSV(){
        _usuarios = new HashMap<>();
        _peliculas = new HashMap<>();
        _valoraciones = new ArrayList();
    }
    public void leerparaalgortimos(){
        try {
            leerFicheroPeliculas();
            System.out.println(_peliculas.size());        
                                AlgSimilitud algortimosSimilitud = new AlgSimilitud();


            //PrimerTest
            leerFicheroValoraciones("recursos/ratings3-1.csv");
            leerFicheroValoraciones("recursos/ratings3-2.csv");
            leerFicheroValoraciones("recursos/ratings3-3.csv");
            leerFicheroValoraciones("recursos/ratings3-4.csv");
            //Hacer modelo similitud
            System.out.println("Leido del fichero");
            HashMap<Long, TreeSet<Similitud>> modeloSimilitudCoseno = algortimosSimilitud.getModeloSimilitudCoseno(500,_peliculas);
                        System.out.println("Tabla creada");
            Iterator<Map.Entry<Long, TreeSet<Similitud>>> iterator = modeloSimilitudCoseno.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Long, TreeSet<Similitud>> next = iterator.next();
                TreeSet<Similitud> value = next.getValue();
                Long key = next.getKey();
                //System.out.println("-----------------------------");
               //System.out.println("fila id peli "+key);
                Iterator<Similitud> iterator1 = value.iterator();
                while (iterator1.hasNext()){
                    Similitud next1 = iterator1.next();
                    //System.out.println(next1.getIdPelicula()+" | "+next1.getSimilitud());
                }
            }
/**
            //Segundo Test
            leerFicheroValoraciones("recursos/ratings3-1.csv");
            leerFicheroValoraciones("recursos/ratings3-2.csv");
            leerFicheroValoraciones("recursos/ratings3-3.csv");
            leerFicheroValoraciones("recursos/ratings3-4.csv");
            modeloSimilitud_byCoseno = algortimosSimilitud.
            getModeloSimilitud_byCoseno(7, _usuarios,_peliculas);
             */

        } catch (ErrorLecturaFichero ex) {
            Logger.getLogger(FicheroCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * Crea la EEDD desde los ficheros y las inserta en la BBDD
     */
    void importarDatos() throws ErrorLecturaFichero{
        //Lee los ficheros de peliculas y valoraciones
        leerFicheroPeliculas();
        leerFicheroValoraciones("recursos/peliculas.csv");
        System.out.println("Ficheros leidos correctamente \nInsertando en la BBDD...");
        
        try { 
            //Inserta las EEDD en la BBDD
            DAOValoracion.instancia().insert(_valoraciones);
            System.out.println("Valoraciones insertadas.");
            DAOPelicula.instancia().insert(_peliculas);
            System.out.println("Peliculas insertadas.");
            _peliculas.clear();
            DAOUsuario.instancia().insert(_usuarios);
            _usuarios.clear();
            System.out.println("Usuarios insertados.");
            _valoraciones.clear();

        } catch ( ErrorInsertarValoracion | ErrorInsertarPelicula | ErrorInsertarUsuario ex) {
            Logger.getLogger(FicheroCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Importa las peliculas de un fichero csv a la bbdd del sistema
     */
    private void leerFicheroPeliculas () throws ErrorLecturaFichero{
        
        BufferedReader br = null;
        
        try {
            //Abrimos el fichero de peliculas
            URL url = this.getClass().getClassLoader().getResource("recursos/peliculas.csv");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            //Leemos la primera linea donde contiene los titulos de las columnas
            String linea = br.readLine();            
            
            //Leemos todas las peliculas del fichero
            while ((linea = br.readLine()) != null){
                //Obtenemos los atributos de cada linea
                String[] split = linea.split(",");
                Map<String, Object> detalles = new HashMap<>();
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
                detalles.put("ano", ano);                
                detalles.put("titulo",split[2].trim());
                detalles.put("valoraciones", new HashMap<Long, Valoracion>());
                detalles.put("suma_valoraciones", (long) 0);
                
                //Creamos la pelicula, sin que se persista en la base de datos
                Pelicula pelicula = new Pelicula(id, detalles);
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
     */
    private void leerFicheroValoraciones (String nombre) throws ErrorLecturaFichero{
        
        BufferedReader br = null; 
        
        try {
            //Abrimos el fichero de peliculas
            URL url = this.getClass().getClassLoader().getResource(nombre);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            //Leemos la primera linea donde contiene los titulos de las columnas
            String linea = br.readLine();            
            
            int i = 0;
            //Leemos todas las peliculas del fichero
            while ((linea = br.readLine()) != null){
                //Obtenemos los atributos de cada linea
                String[] split = linea.split(",");
                Map<String, Object> detalles = new HashMap<>();
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
                    Usuario usuario = new Usuario(idUsuario, detallesUsuario);                    
                    _usuarios.put(idUsuario, usuario);
                }
                
                //Si no hubo error en la lectura del fichero añadimos la valoracion
                if (idPelicula != -1 && puntuacion != -1){
                    if (_peliculas.containsKey(idPelicula)){
                        //Creamos la valoracion
                        detalles.put("idPelicula",idPelicula);
                        detalles.put("idUsuario",idUsuario);
                        detalles.put("valoracion",puntuacion);
                        Valoracion v = new Valoracion(puntuacion,idUsuario,idPelicula);
                        _valoraciones.add(v);

                        //Obtenemos y modificamos la pelicula
                        Pelicula pelicula = _peliculas.get(idPelicula);
                        Map mapaV = (Map<String, Valoracion>) pelicula.obtieneDetalles().
                                obtieneDetalles().get("valoraciones");
                        mapaV.put(idUsuario, v);
                        long suma = (long) pelicula.obtieneDetalles().obtieneDetalles().get("suma_valoraciones");
                        suma = suma + puntuacion;
                        pelicula.obtieneDetalles().obtieneDetalles().put("suma_valoraciones", suma);
                        float media =  (float)suma / mapaV.size();
                        pelicula.obtieneDetalles().obtieneDetalles().put("valoracion_media", media);  

                        //Obtenemos y modificamos el usuario
                        Usuario usuario = _usuarios.get(idUsuario);
                        Map mapaV2 = (Map<Long, Valoracion>)usuario.obtieneDetalles().
                                obtieneDetalles().get("valoraciones");
                        mapaV2.put(idPelicula,v);
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
     * Imprime por pantalla los resultados para comprobar que todo esta Ok
     */
    void recuperarDato(){
        Pelicula p = DAOPelicula.instancia().get((long)571);
        DetallesPelicula detalles = p.obtieneDetalles();
        System.out.println(detalles.obtieneDetalle("titulo") + "-- titulo");
        System.out.println(detalles.obtieneDetalle("ano") + "-- ano");
        System.out.println(detalles.obtieneDetalle("suma_valoraciones") + "-- suma_valoraciones");
        System.out.println(detalles.obtieneDetalle("valoracion_media") + "-- valoracion_media");
        Map<String, Valoracion> val = (Map<String, Valoracion>) p.obtieneDetalles().obtieneDetalle("valoraciones");
        System.out.println(val.size() + "-- numero valoraciones recibidas.");

        Iterator<Map.Entry<String, Valoracion>> iterator = val.entrySet().iterator();
        
        while (iterator.hasNext()){
            Valoracion v = iterator.next().getValue();
            System.out.println("-------\n\t id pelicula: "+v.getIdPelicula());
            System.out.println("\t id usuario: "+v.getIdUsuario());
            System.out.println("\t valoracion: "+v.getPuntuacion());
        }

    }
    
}
