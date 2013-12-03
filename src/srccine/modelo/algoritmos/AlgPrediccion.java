package srccine.modelo.algoritmos;

import srccine.modelo.Usuario;
import srccine.modelo.Similitud;
import srccine.modelo.Valoracion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Clase que contiene los distintos algoritmos de prediccion de valoraciones. 
 * Los algoritmos disponibles son Item Average + Adjustment (IA+A) y Weighted 
 * Sum (WS)
 * @author José
 */
public class AlgPrediccion {    
    
    /**
     * Método para predecir la valoracion de un usuario sobre una pelicula. 
     * Se tendra en cuenta solo los vecinos mas cercanos y el algoritmo de IA+A.
     * @param n Numero de evaluaciones que se conocen del usuario (para el 
     *          enfoque dados-n). Si vale 0, se usa el enfoque todos menos 1
     * @param u Usuario actual
     * @param mediaPelicula    Media de la puntuacion de la pelicula cuya valoracion se
     *                  va a predecir
     * @param vecinos Conjunto de vecinos mas cercanos a la pelicula de la que 
     *                  se va a precedir la valoracion.
     * @return Devuelve la prediccion de la valoracion o -1 si no se ha 
     *          podido predecir
    */
    public static double calcularPrediccionIAmasA(int n, Usuario u, double mediaPelicula, TreeSet<Similitud> vecinos) {
        //Lista de las Valoraciones que el usuario ha realizado sobre los 
        //k-vecinos mas cercanos a idPelicula
        ArrayList<Valoracion> valoracionesVecinos = new ArrayList();
        //Valoraciones del usuario actual (u)
        Map<Long, Valoracion> uValoraciones = 
                (Map<Long, Valoracion>) u.obtieneDetalles().obtieneDetalles().get("valoraciones");
        
        //Se recorren las peliculas vecinas para quedarnos solamente con las
        //que hayan sido votados por el usuario actual
        for(Similitud s : vecinos){
            //Si el usuario ha valorado a la pelicula vecina
            if ( uValoraciones.containsKey(s.getIdPelicula()) ){
                valoracionesVecinos.add(uValoraciones.get(s.getIdPelicula()));
                if (s.getSimilitud()!=0.0){ System.out.println("SIMI "+s.getSimilitud());
                System.out.println("id pe "+s.getIdPelicula());
            }}
        }
        
        //Ahora la formula de la prediccion Item Average+Adjustment
        if (!valoracionesVecinos.isEmpty()){
            //Calculo de la prediccion.
            double numerador = 0;
            double denominador = 0;
            Similitud similitud;
            Valoracion v;
            
            /**
             * El enfoque todos menos 1 se aplica cuando n es todas las valoraciones disponibles
             */
            /**
             * ENFOQUE DADOS-N. Seleccionamos N valoraciones cercanas.
             * Si no hubiera suficientes valoraciones hay que dar la prediccion 
             * por imposible (devolveria -1).
             */            
            if (n > 0){
                //Si n>0 y hay suficientes valoraciones se hace enfoque dados-n
                if (n < valoracionesVecinos.size()) {                    
                    int rand;
                    int cont = 0;
                    ArrayList<Valoracion> aux = new ArrayList();

                    while (cont < n){
                        rand = (int) (Math.random() * valoracionesVecinos.size());
                        v = valoracionesVecinos.get(rand);
                        if (!aux.contains(v)){
                            aux.add(v);
                            ++cont;
                        }
                    }

                    valoracionesVecinos = aux;
                } else {
                    return -1;
                }
            }
            // FIN ENFOQUE DADOS-N
            
            double mediaU = (double) u.obtieneDetalles().obtieneDetalle("media");
            //A partir de aquí FORMULA de IA+A
            Iterator<Valoracion> it1 = valoracionesVecinos.iterator();
            
            while(it1.hasNext()){
                v = it1.next();

                //Buscamos las similitudes de los vecinos
                similitud = buscarVecino(v.getIdPelicula(), vecinos);
                
                if (similitud != null){
                    numerador = numerador + similitud.getSimilitud()*(v.getPuntuacion()-mediaU) ;
                    denominador = denominador + similitud.getSimilitud() ;
                }
            }

            if (denominador != 0){                
                return mediaPelicula + numerador/denominador;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
        
    } 
    
    /**
     * Metodo para predecir la valoracion de un usuario sobre una película, 
     * teniendo en cuenta solo los vecinos más cercanos, utilizando el algoritmo 
     * de predicción Weigthed Sum.
     * @param usuario Usuario actual
     * @param vecinos Conjunto de vecinos mas cercanos a la pelicula de la que 
     *                  se va a precedir la valoracion.
     * @return Devuelve la prediccion de la valoracion o -1 si no se ha 
     *          podido predecir
     */
    public static double calcularPrediccionWS(Usuario usuario, TreeSet<Similitud> vecinos) {
        // Estructura con solamente las valoraciones que un usuario ha realizado sobre los k vecinos mas cercanos a idPelicula
        ArrayList<Valoracion> valoracionesVecinos = new ArrayList();
        //Valoraciones del usuario actual (usuario)
        Map<Long, Valoracion> uValoraciones = 
                (Map<Long, Valoracion>) usuario.obtieneDetalles().obtieneDetalles().get("valoraciones");
        
        //Se recorren las peliculas vecinas para quedarnos solamente con las
        //que hayan sido votados por el usuario actual
        for(Similitud s : vecinos){            
            //Si el usuario ha valorado a la pelicula vecina
            if ( uValoraciones.containsKey(s.getIdPelicula()) ){                    
                valoracionesVecinos.add(uValoraciones.get(s.getIdPelicula()));
            }
        }
        
        //Ahora la formula de Weighted Sum
        if (!valoracionesVecinos.isEmpty()){
            // PASO 3: Cálculo de la predicción.
            double numerador = 0;
            double denominador = 0;
            long idPeliculaAux;
            Similitud itemSim;
            Iterator<Valoracion> it1 = valoracionesVecinos.iterator();
            Valoracion v;

            while(it1.hasNext()){
                v = it1.next();
                idPeliculaAux = v.getIdPelicula();
                itemSim = buscarVecino(idPeliculaAux, vecinos);

                numerador = numerador + itemSim.getSimilitud()*v.getPuntuacion();
                denominador = denominador + itemSim.getSimilitud();
            }

            if (denominador != 0){
                return numerador/denominador;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
        
    }    
    
    /*************************/
    /**
     * Funciones auxiliares
     */
    
    /**
     * Funcion para buscar la similitud para una pelicula
     * @param idPelicula   Identificador de la pelicula de la que se quiere conocer
     *              la similitud con la pelicula que se va a predecir la valoracion.
     * @param vecinos Conjunto de k-vecinos mas cercanos a la pelicula de la que 
     *              se va a precedir una valoracion.
     * @return Devuelve un objeto Similitud de idP si existe o null en caso 
     * contrario. La clase Similitud contiene pares pelicula-valor de similitud.
    */
    private static Similitud buscarVecino(long idPelicula, TreeSet<Similitud> vecinos) {
        Iterator<Similitud> it = vecinos.iterator();
        Similitud s;
        
        while (it.hasNext()){
            s = it.next();
            
            if (s.getIdPelicula() == idPelicula){
                return s;
            }
        }
        
        return null;
    }
}
