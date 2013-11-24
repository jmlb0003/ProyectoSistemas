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
        // Estructura para guardar las valoraciones que un usuario ha realizado 
        //sobre los k vecinos mas cercanos a idP
        ArrayList<Valoracion> vecinoConValoracion = new ArrayList();
        //Valoraciones del usuario actual
        Map<Long, Valoracion> uValoraciones = (Map<Long, Valoracion>) u.obtieneDetalles().obtieneDetalles().get("valoraciones");
        
        /// PASO 1: Quedarnos con las valoraciones a las películas más cercanas.
        /// 1.1. Se recorren los vecinos mas cercanos a idP
        ///mostrarVecinos(vecinos);
        //Obtenemos las valoraciones de los vecinos mas cercanos
        try{
            //Se recorren los vecinos en busca de las valoraciones de la pelicula
            //que han valorado
            //la misma pelicula
            
            for(Similitud i : vecinos){
                //Si el usuario ha valorado al dicho vecino                 
                if ( uValoraciones.containsKey(i.getIdPelicula()) ){
                    // 1.3. Si es así se almacena en la estructura vecinoConValoracion.
                    vecinoConValoracion.add(uValoraciones.get(i.getIdPelicula()));
                }
            }
        }catch(Exception e){
            return -1;
        }
        
        if (!vecinoConValoracion.isEmpty()){
            // PASO 2: Conseguir las medias.
            // 2.1. Media de la pelicula idP
            //Va en el parametro mediaP que se pasa a la funcion

            // 2.2. Media del usuario en cuestion
//            double mediaU = u.getMedia();


            // PASO 3: Cálculo de la prediccion.
            double numerador = 0;
            double denominador = 0;
            long idPAux;
            Similitud itemSim;
            Valoracion v;
            
            /**
             * El enfoque todos menos 1 se aplica cuando n es todas las valoraciones disponibles
             */
            /**
             * ENFOQUE DADOS-N. Seleccionamos N valoraciones cercanas.
             * Si no hubiera suficientes valoraciones hay que dar la prediccion 
             * por imposible. El parametro cobertura serviria para evaluar si 
             * con el algoritmo se dan muchas predicciones por perdidas.
             */            
            if (n > 0){
                //Si n>0 y hay suficientes valoraciones se hace enfoque dados-n
                if (n < vecinoConValoracion.size()) {                    
                    int rand;
                    int cont = 0;
                    ArrayList<Valoracion> array = new ArrayList();

                    while (cont < n){
                        rand = (int) (Math.random() * vecinoConValoracion.size());
                        v = vecinoConValoracion.get(rand);
                        if (!array.contains(v)){
                            array.add(v);
                            ++cont;
                        }
                    }

                    vecinoConValoracion = array;
                } else {
                    return -1;
                }
            }
            // FIN ENFOQUE DADOS-N
            
            
            //A partir de aquí FORMULA de IA+A
            Iterator<Valoracion> it1 = vecinoConValoracion.iterator();
            
            while(it1.hasNext()){
                v = it1.next();
                idPAux = v.getIdPelicula();

                itemSim = buscarVecino(idPAux, vecinos);

                numerador = numerador + itemSim.getSimilitud()*(v.getPuntuacion()-mediaU) ;
                
                denominador = denominador + itemSim.getSimilitud() ;

            }

            if (denominador != 0){
                double ajuste = numerador/denominador;
                
                return mediaP + ajuste;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
        
    }
}
