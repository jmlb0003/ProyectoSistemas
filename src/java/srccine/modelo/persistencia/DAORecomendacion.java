package srccine.modelo.persistencia;

import srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion;
import srccine.modelo.persistencia.excepciones.ErrorActualizarRecomendacion;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import srccine.modelo.persistencia.excepciones.ErrorBorrarRecomendacion;
import javax.persistence.EntityManager;
import srccine.modelo.Recomendacion;

/**
 *
 * @author Jesús
 */
public class DAORecomendacion {    
    
    static DAORecomendacion instancia=null;    
    
    /**
     * Constructor por defecto
     */
    private DAORecomendacion(){}
    
    /**
     * Inserta un Recomendacion nuevo en la base de datos
     * @param r Recomendacion nuevo que se introducira 
     * @throws srccine.modelo.persistencia.excepciones.ErrorInsertarRecomendacion 
     */
    public void insert(Recomendacion r) throws ErrorInsertarRecomendacion {
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Guarda el objeto en la base de datos
            em.persist(r);
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarRecomendacion();
        }
    }
    
    /**
     * Recupera un Recomendacion de la base de datos
     * @param id nombre del Recomendacion a recuperar
     * @return Recomendacion encontrado en la base de datos
     */
    public Recomendacion get(Long id){
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        //Devuelve lo que encuentre en la base de datos
        return em.find(Recomendacion.class, id);
    }
    
    /**
     * Actualiza un Recomendacion ya existente en la base de datos
     * @param r Recomendacion a actualizar
     * @throws srccine.modelo.persistencia.excepciones.ErrorActualizarRecomendacion
     */
    public void update(Recomendacion r) throws ErrorActualizarRecomendacion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.merge(r);
            //Realiza la operacion
            em.getTransaction().commit();
        }catch (Exception ex){
            em.clear();
            throw new ErrorActualizarRecomendacion();
        }
        
    }
    
    /**
     * Borra una Recomendacion de la base de datos
     * @param r Recomendacion que se debe borrar
     * @throws ErrorBorrarRecomendacion error al borrar la Recomendacion
     */
    public void remove(Recomendacion r) throws ErrorBorrarRecomendacion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.remove(r);
            //Realiza la operacion
            em.getTransaction().commit();            
        }catch (Exception ex){
            em.clear();
            throw new ErrorBorrarRecomendacion();
        }
    }
    /**
     * Borra una lista de recomendaciones
     * @param recomendaciones SortedSet<Recomendacion> Recomendaciones que se debe borrar
     * @throws ErrorBorrarRecomendacion error al borrar la Recomendacion
     */
    public void remove(SortedSet<Recomendacion> recomendaciones) throws ErrorBorrarRecomendacion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            Iterator<Recomendacion> iterator = recomendaciones.iterator();
            while (iterator.hasNext()) {
                Recomendacion r = iterator.next();
                //Actualiza el objeto en la base de datos
                em.remove(r);
            }
            
            //Realiza la operacion
            em.getTransaction().commit();            
        }catch (Exception ex){
            em.clear();
            throw new ErrorBorrarRecomendacion();
        }
    }
    
    /**
     * Devuelve la instancia de DAORecomendacion existente en el sistema, sino existe la crea
     * @return DAORecomendacion instancia del DAO
     */
    public static DAORecomendacion instancia(){
        //Comprueba que exista la instancia
        if (instancia==null){
            instancia=new DAORecomendacion();
        }
        
        //La devuelve
        return instancia;
    }

    /**
     * Inserta una lista de Recomendaciones en la base de datos
     * @param _recomendaciones contenedor de Recomendaciones
     * @throws ErrorInsertarRecomendacion Error al inserta la valoraicon
     */
    public synchronized void insert(List _recomendaciones) throws ErrorInsertarRecomendacion {
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        Recomendacion r;
        try{
            Iterator iterator = _recomendaciones.iterator();

            //Comienza la transaccion
            em.getTransaction().begin();
            //Recorre toda la lista de valoraicones
            while (iterator.hasNext()){
                //Guarda el objeto en la base de datos
                r = (Recomendacion) iterator.next();
                em.persist(r);
            }
            //Realiza la operacion
            em.getTransaction().commit();            

        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarRecomendacion();
        }        
    }
    
}
