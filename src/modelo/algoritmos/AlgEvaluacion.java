package modelo.algoritmos;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import modelo.Parametros;
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
     * @param n 
     * @param modeloSimilitud Tabla de similitud
     * @param peliculas Lista de peliculas sobre la que realizar el Test
     * @param usuarios Lista de usuarios sobre la que realizar el Test
     * @return Devuelve el Par (MAE, Cobertura) en el objeto Parametros.
     */
    public static Parametros testIAmasA(int n, HashMap<Long, TreeSet<Similitud>> modeloSimilitud, 
        Map<Long, Pelicula> peliculas,List<Usuario> usuarios) {
        
        //Definimos las variables auxiliares
        Iterator<Usuario> iteradorUsuarios = usuarios.iterator();
        Usuario usuario;
        double mediaPelicula, valoracionEstimada, MAE = 0, diferencia;
        long idPelicula;
        int valoracionReal,numEstimacionesImposibles = 0, numEstimacionesRealizadas = 0, numEstimacionesPosibles = 0;
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
                valoracionReal = e.getValue().getPuntuacion();

                // Comprobamos si realizo la prediccion
                if (valoracionEstimada != -1){
                    // Calculamos la diferencia entre la valoracion y la añadimos al MAE
                    diferencia = valoracionEstimada - valoracionReal*1.0;

                    if (diferencia > 0){
                       MAE = MAE + diferencia;
                    }else{
                       MAE = MAE + diferencia*(-1);
                    }
                }else{
                    //Aumentamos el numero de estimaciones fallidas
                    ++numEstimacionesImposibles;
                }
            }   

            //Aumentamos el numero de estimaciones posibles
            numEstimacionesPosibles += valoracionesUsuario.size();
        }

        // Actualiza el numero de estimaciones realizadas y el MAE
        numEstimacionesRealizadas = numEstimacionesPosibles - numEstimacionesImposibles;
        if (numEstimacionesRealizadas != 0){
            MAE = MAE/(numEstimacionesRealizadas*1.0);
        }else{
            MAE = 0;
        }

        return new Parametros(MAE,(numEstimacionesRealizadas*1.0)/numEstimacionesPosibles);
    }
}
