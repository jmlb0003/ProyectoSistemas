package modelo.persistencia;

import modelo.persistencia.excepciones.ErrorActualizarPelicula;
import modelo.persistencia.excepciones.ErrorBorrarPelicula;
import modelo.persistencia.excepciones.ErrorInsertarPelicula;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.EntityManager;
import modelo.Pelicula;

/**
 * Clase que sigue el patron DAO para persistir los objetos de la clase pelicula
 * @author Jesús
 */
public class DAOPelicula {
    private static DAOPelicula instancia=null;   
    
    /**
     * Constructor por defecto
     */
    DAOPelicula(){}
    
    /**
     * Inserta un Pelicula nuevo en la base de datos
     * @param p Pelicula nuevo que se introducira
     * @throws ErrorInsertarPelicula Error debido a que el Pelicula ya existe 
     */
    public void insert(Pelicula p) throws ErrorInsertarPelicula{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Guarda el objeto en la base de datos
            em.persist(p);
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarPelicula();
        }
    }
    
    /**
     * Recupera un Pelicula de la base de datos
     * @param id nombre del Pelicula a recuperar
     * @return Pelicula encontrado en la base de datos
     */
    public Pelicula get(Long id){
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        //Devuelve lo que encuentre en la base de datos
        return em.find(Pelicula.class, id);
    }
    
    /**
     * Actualiza un Pelicula ya existente en la base de datos
     * @param p Pelicula a actualizar
     * @throws ErrorActualizarPelicula error al actualizar el usuario en la bbdd
     */
    public void update(Pelicula p) throws ErrorActualizarPelicula{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.merge(p);
            //Realiza la operacion
            em.getTransaction().commit();
        }catch (Exception ex){
            em.clear();
            throw new ErrorActualizarPelicula();
        }
        
    }
    
    /**
     * Borra un Pelicula de la base de datos
     * @param p Pelicula que se debe borrar
     * @throws ErrorBorrarPelicula error al borrar el Pelicula
     */
    public void remove(Pelicula p) throws ErrorBorrarPelicula{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.remove(p);
            //Realiza la operacion
            em.getTransaction().commit();            
        }catch (Exception ex){
            em.clear();
            throw new ErrorBorrarPelicula();
        }
    }
    
    /**
     * Devuelve la instancia de DAOPelicula existente en el sistema, sino existe la crea
     * @return DAOPelicula instancia del DAO
     */
    public static DAOPelicula instancia(){
        //Comprueba que exista la instancia
        if (instancia==null){
            instancia=new DAOPelicula();
        }
        
        //La devuelve
        return instancia;
    }

    /**
     * Inserta todas las peliculas de un contenedor dentro de la BBDD
     * @param peliculas Contenedor de peliculas
     * @throws ErrorInsertarPelicula Error en la insercion de una pelicula
     */
    public void insert(Map peliculas) throws ErrorInsertarPelicula{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Recorre todo el mapa
            Iterator<Map.Entry> iterator = peliculas.entrySet().iterator();
            while (iterator.hasNext()){
                //Guarda el objeto en la base de datos
                Pelicula p = (Pelicula) iterator.next().getValue();
                em.persist(p);
            }
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarPelicula();
        }
    }
    
}
