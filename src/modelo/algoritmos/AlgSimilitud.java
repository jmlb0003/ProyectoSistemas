/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo.algoritmos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import modelo.*;

/**
 * Clase que contiene los distintos algoritmos de calculo de similitud entre 
 * items. Los algoritmos disponibles son similitud del coseno y Coeficiente de 
 * Correlacion de Pearson.
 * @author Jesus
 */
public class AlgSimilitud {
        
    /**
     * Método calcular la similitud entre dos Películas utilizando el algoritmo del Coseno.
     * @param p1 Primera pelicula a comparar.
     * @param p2 Segunda película a comparar.
     * @param usuariosTest Lista de peliculas que han valorado los usuarios incluidas en 
     * la particion de test. Deben descartarse.
     * @return Devuelve el valor de similitud con un real entre 0 y 1
    */
    private static double similitudCoseno(Pelicula p1, Pelicula p2, List<String> usuariosTest){        
        double suma1 = 0;
        double suma2 = 0;
        int val1, val2;
        String idUsuario;
        double numerador = 0;
        
        Map<String, Valoracion> valoracionesP1 = (Map<String, Valoracion>) p1.obtieneDetalles().
                obtieneDetalle("valoraciones");
        Map<String, Valoracion> valoracionesP2 = (Map<String, Valoracion>) p2.obtieneDetalles().
                obtieneDetalle("valoraciones");
                
        /**
         * 1- Recorremos la pelicula que tiene menos valoraciones para ahorrar 
         *    calculos
         * 2- Si la otra pelicula tambien ha sido valorada por el usuario se 
         *    aplica la formula de similitud
         */
        if (valoracionesP1.size() < valoracionesP2.size()){
            //Se recorren las valoraciones de la pelicula
            for (Entry<String,Valoracion> e : valoracionesP1.entrySet()) {
                idUsuario = e.getKey();
                
                //############// 2. Descartamos los usuarios de la partición test
                if (!usuariosTest.contains(idUsuario)){
                    //Si la otra película ha sido valorada por el usuario
                    if (valoracionesP2.containsKey(idUsuario)){  
                        val1 = e.getValue().getPuntuacion();
                        val2 = valoracionesP2.get(idUsuario).getPuntuacion();

                        suma1 = suma1 + val1 * val1;
                        suma2 = suma2 + val2 * val2;

                        numerador = numerador + val1 * val2;
                    }
                    //SI no ha sido valorada, no se tiene en cuenta
                }
            }
        }else{
            for (Entry<String,Valoracion> e : valoracionesP2.entrySet()) {
                idUsuario = e.getValue().getIdUsuario();
                
                //############// 2. Descartamos los usuarios de la partición test
                if (!usuariosTest.contains(idUsuario)){
                
                    //Si la otra película ha sido valorada por el usuario
                    if (valoracionesP1.containsKey(idUsuario)){
                        val2 = e.getValue().getPuntuacion();
                        val1 = valoracionesP1.get(idUsuario).getPuntuacion();

                        suma1 = suma1 + val1 * val1;
                        suma2 = suma2 + val2 * val2;

                        numerador = numerador + val1 * val2;
                    }
                    //SI no ha sido valorada, no se tiene en cuenta
                }
            }
        }
        
        if (suma1 != 0 && suma2 !=0){
            double sim = numerador / (Math.sqrt(suma1) * Math.sqrt(suma2));
            
            if (sim > 1){
                return 1;
            }
            return sim;
        }else{
            return 0;
        }
        
    }    
    
    /**
     * Metodo calcular el modelo de similitud o tabla de similitud entre peliculas
     * utilizando el algoritmo de similitud del coseno.
     * @param k Numero de vecinos mas cercanos que se tienen en cuenta.
     * @param peliculas Conjunto de peliculas de las que se calcula la similitud.
     * @param usuariosTest
     * @return Devuelve el modelo de similitud de las peliculas.
    */    
    public static HashMap<Long, TreeSet<Similitud>> getModeloSimilitudCoseno(int k, List<Pelicula> peliculas, 
            List<String> usuariosTest) {
        /**
         * La estructura en la que almacenamos el modelo de similitud es:
         * clave: id de pelicula.
         * valor: similitudes con cada pelicula pares (idPelicula-Similitud)
         */
        HashMap<Long, TreeSet<Similitud>> modelo_similitud = new HashMap();
        TreeSet<Similitud> fila1, fila2;
        Pelicula it1, it2;
        long id1, id2;
        double similitud;
        long nPelis = peliculas.size();
        
        ///Iterator<Entry<Long, Pelicula>> iterator = peliculas.entrySet().iterator();
        ///while (iterator.hasNext()){

        for (int i=0; i<nPelis; ++i){
            ///System.out.println(" pelicula "+i+" de "+numPeliculas);
            ///###// 1.1: Sacar la película numero i. Nota: estudiar si se pueden sacar todas de golpe.
            ///Pelicula it1 = getPeliculaBD_byPos(instancia, i);
            ///Entry<Long, Pelicula> entrada = iterator.next();
            it1 = (Pelicula) peliculas.get(i);
            id1 = it1.obtieneID();
            
            ///Iterator<Entry<Long, Petlicula>> iterator2 = new Iterator<Entry<Long, Pelicula>>();
            
            for (int j=i+1; j<nPelis; ++j){
                ///###// 1.2: Sacar la película numero j vv.
                ///Pelicula it2 = getPeliculaBD_byPos(instancia, j);
                ///Entry<Long, Pelicula> entrada2 = iterator2.next();
                it2 = peliculas.get(j);
                id2 = it2.obtieneID();
           
                //Calculamos la similitud entre it1 e it2.
                similitud = similitudCoseno(it1, it2, usuariosTest);
                ///System.out.println(id1 + " " + id2 + " | " + similitud);
                /// 1.3: Guardar la similitud en una estructura.
                    //### 1.3: En el modelo definitivo, la similitud se guardará en la base de datos.
                    //###//Similitud s1 = new Similitud(it1.id,it2.id,similitud);
                ///     NOTA: Hay que guardar, a la vez, tanto la similitud sim(id1,id2) como sim (id2,id1)
                //Si ya hay valores de similitud para it1 se mete en la fila que ya hay
                if (modelo_similitud.containsKey(id1)){                    
                    fila1 =  modelo_similitud.get(id1); //Saca la fila existente
                    fila1.add(new Similitud(id2,similitud)); //Le añade la similitud
                    //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                    if (fila1.size() > k){
                        fila1.remove(fila1.last());
                    }
                    
                    //Añadimos ahora sim(id2,id1). Si esta la fila se añade ahi y si no se crea una nueva
                    if (modelo_similitud.containsKey(id2)){
                        fila2 =  modelo_similitud.get(id2); 
                        fila2.add(new Similitud(id1,similitud));
                        //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                        if (fila2.size() > k){
                            fila2.remove(fila2.last());
                        }
                    }else{
                        modelo_similitud.put(id2, new TreeSet<Similitud>());
                        modelo_similitud.get(id2).add(new Similitud(id1,similitud));
                    }
                }else{
                    //Si no hay valores de similitud para it1 metemos la nueva fila
                    modelo_similitud.put(id1, new TreeSet<Similitud>());
                    //y le metemos la similitud que hemos calculado
                    modelo_similitud.get(id1).add(new Similitud(id2,similitud));
                    
                    if (modelo_similitud.containsKey(id2)){
                        fila2 =  modelo_similitud.get(id2);
                        fila2.add(new Similitud(id1,similitud));
                        //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                        if (fila2.size() > k){
                            fila2.remove(fila2.last());
                        }
                    }else{
                        modelo_similitud.put(id2, new TreeSet<Similitud>());
                        modelo_similitud.get(id2).add(new Similitud(id1,similitud));
                    }
                }
            }
        }

        
        return modelo_similitud;
    }
    
    
    /**
     * Método calcular la similitud entre dos Películas utilizando el algoritmo 
     * de similitud del Coeficiente de Correlacion de Pearson.
     * @param p1 Primera pelicula a comparar.
     * @param p2 Segunda película a comparar.
     * @param usuarioTest Lista de peliculas que han valorado los usuarios incluidas en 
     * la particion de test. Deben descartarse.
     * @return Devuelve el valor de similitud con un real entre 0 y 1
     */
    private static double similitudPearson(Pelicula p1, Pelicula p2, List<String> usuarioTest){
        // Variables auxiliares:
        double suma1 = 0;
        double suma2 = 0;
        int val1, val2;
        String idUsuario;
        double numerador = 0;
        double media1 = (double) p1.obtieneDetalles().obtieneDetalle("media");
        double media2 = (double) p2.obtieneDetalles().obtieneDetalle("media");
        
        Map<String, Valoracion> valoracionesP1 = (Map<String, Valoracion>) p1.obtieneDetalles().
                obtieneDetalle("valoraciones");
        Map<String, Valoracion> valoracionesP2 = (Map<String, Valoracion>) p2.obtieneDetalles().
                obtieneDetalle("valoraciones");
        
        
        /**
         * 1- Recorremos la pelicula que tiene menos valoraciones para ahorrar 
         *    calculos
         * 2- Si la otra pelicula tambien ha sido valorada por el usuario se 
         *    aplica la formula de similitud
         */
        if (valoracionesP1.size() < valoracionesP2.size()){
            for (Entry<String,Valoracion> e : valoracionesP1.entrySet()) {
                idUsuario = e.getKey();
                
                //############// 2. Descartamos los usuarios de la partición test
                if (!usuarioTest.contains(idUsuario)){
                    //Si la otra pelicula ha sido valorada por el usuario
                    if (valoracionesP2.containsKey(idUsuario)){
                        //Realizamos los cálculos de similitud
                        val1 = e.getValue().getPuntuacion();
                        val2 = valoracionesP2.get(idUsuario).getPuntuacion();

                        suma1 = suma1 + (val1 - media1)*(val1 - media1);
                        suma2 = suma2 + (val2 - media2)*(val2 - media2);

                        numerador = numerador + (val1 - media1)*(val2 - media2);
                    }
                    //Si no ha sido valorada no se tiene en cuenta
                }
            }
        }else{
            for (Entry<String,Valoracion> e : valoracionesP2.entrySet()) {
                idUsuario = e.getKey();
                
                //############// 2. Descartamos los usuarios de la partición test
                if (!usuarioTest.contains(idUsuario)){
                    //Si la otra pelicula ha sido valorada por el usuario
                    if (valoracionesP1.containsKey(idUsuario)){
                        //Realizamos los cálculos de similitud
                        val2 = e.getValue().getPuntuacion();
                        val1 = valoracionesP1.get(idUsuario).getPuntuacion();

                        suma1 = suma1 + (val1 - media1)*(val1 - media1);
                        suma2 = suma2 + (val2 - media2)*(val2 - media2);

                        numerador = numerador + (val1 - media1)*(val2 - media2);
                    }
                    //Si no ha sido valorada no se tiene en cuenta
                }
            }
        }
        
        if (suma1 != 0 && suma2 !=0){
            double sim = numerador / (Math.sqrt(suma1*suma2));
            
            //Se transforma a una medida de similitud entre 0 y 1
            sim = (sim + 1)/2;
            
            if (sim > 1){
                return 1;
            }
            return sim;
        }else{
            return 0;
        }
        
    }
    
    
    /**
     * Metodo calcular el modelo de similitud o tabla de similitud entre peliculas
     * utilizando el algoritmo de similitud de correlacion de Pearson.
     * @param k Numero de vecinos mas cercanos que se tienen en cuenta.
     * @param peliculas Conjunto de peliculas de las que se calcula la similitud.
     * @param usuariosTest Lista de peliculas que ha valorado los usuarios incluidas en 
     * la particion de test. Deben descartarse.
     * @return Devuelve el modelo de similitud de las peliculas.
    */
    public static HashMap<Long, TreeSet<Similitud>> getModeloSimilitudPearson(int k, List<Pelicula> peliculas, 
            List<String> usuariosTest) {
        // Estructura que representa el modelo de similitud (clave: id de pelicula; valor: lista de idPelicula-Similitud).
        HashMap<Long, TreeSet<Similitud>> modelo_similitud = new HashMap();
        // Variables auxiliares:
        TreeSet<Similitud> fila1, fila2;
        long id1, id2;
        double similitud;
        Pelicula it1, it2;
        long nPelis = peliculas.size();
        
        
        
        for (int i=0; i<nPelis; ++i){
            //System.out.println(" pelicula "+i+" de "+numPeliculas);
            //###// 1.1: Sacar la película numero i. Nota: estudiar si se pueden sacar todas de golpe.
            //Pelicula it1 = getPeliculaBD_byPos(instancia, i);
            it1 = peliculas.get(i);
            id1 = it1.obtieneID();
            
            for (int j=i+1; j<nPelis; ++j){
                //###// 1.2: Sacar la película numero j.
                //Pelicula it2 = getPeliculaBD_byPos(instancia, j);
                it2 = peliculas.get(j);
                id2 = it2.obtieneID();
                
                //Calculamos la similitud entre it1 e it2.
                similitud = similitudPearson(it1, it2, usuariosTest);
                
                /// 1.3: Guardar la similitud en una estructura.
                    //### 1.3: En el modelo definitivo, la similitud se guardará en la base de datos.
                    //###//Similitud s1 = new Similitud(it1.id,it2.id,similitud);
                ///     NOTA: Hay que guardar, a la vez, tanto la similitud sim(id1,id2) como sim (id2,id1)
                //Si ya hay valores de similitud para it1 se mete en la fila que ya hay
                if (modelo_similitud.containsKey(id1)){
                    fila1 =  modelo_similitud.get(id1); //Saca la fila existente
                    fila1.add(new Similitud(id2,similitud)); //Le añade la similitud
                    //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                    if (fila1.size() > k){
                        fila1.remove(fila1.last());
                    }
                    
                    //Añadimos ahora sim(id2,id1). Si esta la fila se añade ahi y si no se crea una nueva
                    if (modelo_similitud.containsKey(id2)){
                        fila2 =  modelo_similitud.get(id2);
                        fila2.add(new Similitud(id1,similitud));
                        //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                        if (fila2.size() > k){
                            fila2.remove(fila2.last());
                        }
                    }else{
                        modelo_similitud.put(id2, new TreeSet<Similitud>());
                        modelo_similitud.get(id2).add(new Similitud(id1,similitud));
                    }
                }else{
                    //Si no hay valores de similitud para it1 metemos la nueva fila
                    modelo_similitud.put(id1, new TreeSet<Similitud>());
                    //y le metemos la similitud que hemos calculado
                    modelo_similitud.get(id1).add(new Similitud(id2,similitud));
                    
                    if (modelo_similitud.containsKey(id2)){
                        fila2 =  modelo_similitud.get(id2);
                        fila2.add(new Similitud(id1,similitud));
                        //Si se ha superado el numero de vecinos establecido, se quita el ultimo
                        if (fila2.size() > k){
                            fila2.remove(fila2.last());
                        }
                    }else{
                        modelo_similitud.put(id2, new TreeSet<Similitud>());
                        modelo_similitud.get(id2).add(new Similitud(id1,similitud));
                    }
                }
            }
        }
        
        return modelo_similitud;
    }

}
