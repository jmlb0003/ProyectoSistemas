/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo.algoritmos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import modelo.*;

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
     * @param mediaP    Media de la puntuacion de la pelicula cuya valoracion se
     *                  va a predecir
     * @param vecinos Conjunto de vecinos mas cercanos a la pelicula de la que 
     *                  se va a precedir la valoracion.
     * @return Devuelve la prediccion de la valoracion o -1 si no se ha 
     *          podido predecir
    */
    public double calcularPrediccionIAmasA(int n, Usuario u, double mediaP, TreeSet<Similitud> vecinos) {
        //Lista de las Valoraciones que el usuario ha realizado sobre los 
        //k-vecinos mas cercanos a idPelicula
        ArrayList<Valoracion> valoracionesVecinos = new ArrayList();
        //Valoraciones del usuario actual (u)
        Map<Long, Valoracion> uValoraciones = 
                (Map<Long, Valoracion>) u.obtieneDetalles().obtieneDetalles().get("valoraciones");
      
       
        //Se recorren las peliculas vecinas para quedarnos solamente con las
        //que hayan sido votados por el usuario actual
        for(Similitud i : vecinos){
            //Si el usuario ha valorado a la pelicula vecina
            if ( uValoraciones.containsKey(i.getIdPelicula()) ){                    
                valoracionesVecinos.add(uValoraciones.get(i.getIdPelicula()));
            }
        }
        
        
        //Ahora la formula de la prediccion Item Average+Adjustment
        if (!valoracionesVecinos.isEmpty()){
            // PASO 3: Cálculo de la prediccion.
            double numerador = 0;
            double denominador = 0;
            Similitud itemSim;
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
                itemSim = buscarVecino(v.getIdPelicula(), vecinos);
                
                if (itemSim != null){
                    numerador = numerador + itemSim.getSimilitud()*(v.getPuntuacion()-mediaU) ;
                    denominador = denominador + itemSim.getSimilitud() ;
                }
            }

            if (denominador != 0){                
                return mediaP + numerador/denominador;
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
     * @param u Usuario actual
     * @param vecinos Conjunto de vecinos mas cercanos a la pelicula de la que 
     *                  se va a precedir la valoracion.
     * @return Devuelve la prediccion de la valoracion o -1 si no se ha 
     *          podido predecir
     */
    private double calcularPrediccionWS(Usuario u, TreeSet<Similitud> vecinos) {
        // Estructura con solamente las valoraciones que un usuario ha realizado sobre los k vecinos mas cercanos a idP
        ArrayList<Valoracion> valoracionesVecinos = new ArrayList();
        //Valoraciones del usuario actual (u)
        Map<Long, Valoracion> uValoraciones = 
                (Map<Long, Valoracion>) u.obtieneDetalles().obtieneDetalles().get("valoraciones");
        
        //Se recorren las peliculas vecinas para quedarnos solamente con las
        //que hayan sido votados por el usuario actual
        for(Similitud i : vecinos){            
            //Si el usuario ha valorado a la pelicula vecina
            if ( uValoraciones.containsKey(i.getIdPelicula()) ){                    
                valoracionesVecinos.add(uValoraciones.get(i.getIdPelicula()));
            }
        }
        
        //Ahora la formula de Weighted Sum
        if (!valoracionesVecinos.isEmpty()){
            // PASO 3: Cálculo de la predicción.
            double numerador = 0;
            double denominador = 0;
            long idPAux;
            Similitud itemSim;
            Iterator<Valoracion> it1 = valoracionesVecinos.iterator();
            Valoracion v;

            while(it1.hasNext()){
                v = it1.next();
                idPAux = v.getIdPelicula();
                itemSim = buscarVecino(idPAux, vecinos);

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
     * @param idP   Identificador de la pelicula de la que se quiere conocer
     *              la similitud con la pelicula que se va a predecir la valoracion.
     * @param vecinos Conjunto de k-vecinos mas cercanos a la pelicula de la que 
     *              se va a precedir una valoracion.
     * @return Devuelve un objeto Similitud de idP si existe o null en caso 
     * contrario. La clase Similitud contiene pares pelicula-valor de similitud.
    */
    private Similitud buscarVecino(long idP, TreeSet<Similitud> vecinos) {
        Iterator<Similitud> it = vecinos.iterator();
        Similitud i;
        
        while (it.hasNext()){
            i = it.next();
            
            if (i.getIdPelicula() == idP){
                return i;
            }
        }
        
        return null;
    }
}
