package modelo.algoritmos;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import modelo.Pelicula;
import modelo.Similitud;
import modelo.Usuario;
import modelo.Valoracion;

/**
 * Clase que contiene los distintos algoritmos de evaluacion donde obtener el MAE. 
 * @author José
 */
public class AlgEvaluacion {
    
    /**
     * Realiza el Test aplicando algoritmo de prediccion de IA+A
     * @param n Valor de n para aplicarlo al enfoque dados-n del algoritmo IA+A.
     *          Si el valor de n es 0 se usara el enfoque todos menos 1.
     * @param modeloSimilitud Tabla de similitud
     * @param peliculas Lista de peliculas sobre la que realizar el Test
     * @param usuariosTest Lista de usuarios sobre la que realizar el Test
     * @return Devuelve el valor MAE tras la ejecucion del algoritmo IA+A.
     */
    public static double testIAmasA(int n, HashMap<Long, TreeSet<Similitud>> modeloSimilitud, 
        Map<Long, Pelicula> peliculas,List<Usuario> usuariosTest) {
        
        //Definimos las variables auxiliares
        Iterator<Usuario> iteradorUsuarios = usuariosTest.iterator();
        Usuario usuario;
        double mediaPelicula, valoracionEstimada, MAE = 0, diferencia;
        long idPelicula;
        int valoracionReal, numEstimaciones = 0;
        TreeSet<Similitud> vecinos;

        // Iteramos sobre cada usuario
        while (iteradorUsuarios.hasNext()){
            // Obtenemos el usuario y sus valoraciones
            usuario = iteradorUsuarios.next();
            HashMap<Long, Valoracion> valoracionesUsuario = (HashMap<Long, Valoracion>) 
                    usuario.obtieneDetalles().obtieneDetalle("valoraciones");

            // Iteramos sobre cada valoracion del usuario
            for (Entry<Long,Valoracion> e : valoracionesUsuario.entrySet()) {                

                // Obtenemos el Id, media y vecinos de cada pelicula
                idPelicula = e.getKey();
                mediaPelicula = (double) peliculas.get(idPelicula).obtieneDetalles().obtieneDetalle("media");
                vecinos = modeloSimilitud.get(idPelicula);
                //Aplicamos el algoritmo de prediccion del IA+A
                valoracionEstimada = AlgPrediccion.calcularPrediccionIAmasA(n, usuario, mediaPelicula,vecinos);                
                
                // Comprobamos si realizo la prediccion
                if (valoracionEstimada != -1){
                    //Obtenemos la valoracion real para la pelicula
                    valoracionReal = e.getValue().getPuntuacion();
                    // Calculamos la diferencia entre la valoracion y la añadimos al MAE
                    diferencia = valoracionEstimada - valoracionReal*1.0;
                    MAE = MAE + Math.abs(diferencia);
                                        
                    //Aumentamos el numero de estimaciones posibles
                    numEstimaciones++;
                }
            }   

        }
        // Actualiza el numero de estimaciones realizadas y el MAE
        if (numEstimaciones != 0){
            return (MAE/(numEstimaciones*1.0));
        }else{
            return 0.0;
        }

    }
    
    
    /**
     * Realiza el test aplicando el algoritmo de prediccion Weigthed Sum.
     * @param modeloSimilitud Tabla de similitud
     * @param usuariosTest Lista de usuarios con valoraciones incluidas en la 
     *                  particion de test. Deben descartarse.
     * @return Devuelve el valor MAE tras la ejecucion del algoritmo Weighted Sum.
     */
    public static double testWS(HashMap<Long,TreeSet<Similitud>> modeloSimilitud, List<Usuario> usuariosTest) {
        
        // Variables auxiliares:
        Iterator<Usuario> itUsuarios = usuariosTest.iterator();
        Usuario u;
        long idPelicula;
        double valoracionEstimada,dif;
        double MAE = 0, diferencia;
        int valoracionReal,numEstimaciones=0;
        TreeSet<Similitud> vecinos;
        
        
        // 1. Recorremos cada usuario de la particion test.
        while (itUsuarios.hasNext()){
            u = itUsuarios.next();            
            HashMap<Long, Valoracion> valoracionesUsuario = (HashMap<Long, Valoracion>) 
                    u.obtieneDetalles().obtieneDetalle("valoraciones");            
            
             for (Entry<Long,Valoracion> e : valoracionesUsuario.entrySet()) {
                 idPelicula = e.getValue().getIdPelicula();
                 
                 // 3. Calculamos la valoracion real y la estimada.
                 valoracionReal = e.getValue().getPuntuacion();
                 vecinos = modeloSimilitud.get(idPelicula);
                 //Aplicamos el algoritmo de prediccion Weighted SUm
                 valoracionEstimada = AlgPrediccion.calcularPrediccionWS(u,vecinos);
                 
                 // Comprobamos si realizo la prediccion
                 if (valoracionEstimada != -1){
                    //Obtenemos la valoracion real para la pelicula
                    valoracionReal = e.getValue().getPuntuacion();
                    // Calculamos la diferencia entre la valoracion y la añadimos al MAE
                    diferencia = valoracionEstimada - valoracionReal*1.0;

                    MAE = MAE + Math.abs(diferencia);

                    //Aumentamos el numero de estimaciones posibles
                    numEstimaciones++;
                 }
             }
        }
        
        // Actualiza el numero de estimaciones realizadas y el MAE
        if (numEstimaciones != 0){
            return (MAE/(numEstimaciones*1.0));
        }else{
            return 0.0;
        }        
        
    }
 
}
