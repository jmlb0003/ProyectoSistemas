package srccine.modelo.persistencia;

import java.util.Iterator;
import java.util.List;
import srccine.modelo.persistencia.excepciones.ErrorActualizarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorInsertarValoracion;
import srccine.modelo.persistencia.excepciones.ErrorBorrarRecomendacion;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import srccine.modelo.Valoracion;

/**
 *
 * @author Jesús
 */
public class DAOValoracion {    
    
    static DAOValoracion instancia=null;    
    
    /**
     * Constructor por defecto
     */
    private DAOValoracion(){}
    
    /**
     * Inserta un Valoracion nuevo en la base de datos
     * @param v Valoracion nuevo que se introducira
     * @throws ErrorInsertarValoracion Error deb_ido a que el Valoracion ya existe 
     */
    public void insert(Valoracion v) throws ErrorInsertarValoracion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Guarda el objeto en la base de datos
            em.persist(v);
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarValoracion();
        }
    }
    
    /**
     * Recupera un Valoracion de la base de datos
     * @param id nombre del Valoracion a recuperar
     * @return Valoracion encontrado en la base de datos
     */
    public Valoracion get(String id){
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        //Devuelve lo que encuentre en la base de datos
        return em.find(Valoracion.class, id);
    }
    
    /**
     * Actualiza un Valoracion ya existente en la base de datos
     * @param v Valoracion a actualizar
     * @throws ErrorActualizarValoracion error al actualizar el usuario en la bbdd
     */
    public void update(Valoracion v) throws ErrorActualizarValoracion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.merge(v);
            //Realiza la operacion
            em.getTransaction().commit();
        }catch (Exception ex){
            em.clear(); ex.printStackTrace();
            throw new ErrorActualizarValoracion();
        }
        
    }
    
    /**
     * Borra una valoracion de la base de datos
     * @param v Valoracion que se debe borrar
     * @throws ErrorBorrarRecomendacion error al borrar la valoracion
     */
    
    public void remove(Valoracion v) throws ErrorBorrarRecomendacion{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.remove(v);
            //Realiza la operacion
            em.getTransaction().commit();            
        }catch (Exception ex){
            em.clear();
            throw new ErrorBorrarRecomendacion();
        }
    }
    
    /**
     * Devuelve la instancia de DAOValoracion existente en el sistema, sino existe la crea
     * @return DAOValoracion instancia del DAO
     */
    public static DAOValoracion instancia(){
        //Comprueba que exista la instancia
        if (instancia==null){
            instancia=new DAOValoracion();
        }
        
        //La devuelve
        return instancia;
    }

    /**
     * Inserta una lista de valoraciones en la base de datos
     * @param _valoraciones contenedor de valoraciones
     * @throws ErrorInsertarValoracion Error al inserta la valoraicon
     */
    public void insert(List _valoraciones) throws ErrorInsertarValoracion {
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        Valoracion v;
        int n = _valoraciones.size();
        try{
            Iterator iterator = _valoraciones.iterator();

                //Comienza la transaccion
                em.getTransaction().begin();
                //Recorre toda la lista de valoraicones
                while (iterator.hasNext()){
                    //Guarda el objeto en la base de datos
                    v = (Valoracion) iterator.next();
                    em.persist(v);
                }
                //Realiza la operacion
                em.getTransaction().commit();            
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarValoracion();
        }        
    }

    /**
     * Busca en la bbdd la valoracion realizada por un usuario sobre una pelicula determinada
     * @param idUsuario Id del usuario que realizo la valoracion
     * @param idPelicula Id de la pelicula que se valoro
     * @return Valoracion encontrada en la bbdd
     */
    public Valoracion get(String idUsuario, Long idPelicula) {
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
                
        //Busca el partido creando un query
        Query consulta = em.createQuery("select v from Valoracion v WHERE v._idPelicula="+idPelicula+" and v._idUsuario="+idUsuario);
        
        if (consulta.getResultList().isEmpty()){
            return null;
        }
        
        return (Valoracion) consulta.getResultList().get(0);
    }
    
}
