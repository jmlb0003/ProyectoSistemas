package modelo.persistencia;

import java.util.Iterator;
import java.util.Map;
import modelo.persistencia.excepciones.ErrorBorrarUsuario;
import modelo.persistencia.excepciones.ErrorInsertarUsuario;
import modelo.persistencia.excepciones.ErrorActualizarUsuario;
import javax.persistence.EntityManager;
import modelo.Usuario;

/**
 * Clase que sigue el patron DAO para persistir los objetos de la clase Usuario
 * @author Jesús
 */
public class DAOUsuario {
    private static DAOUsuario instancia=null;
    
    /**
     * Constructor por defecto
     */
    private DAOUsuario(){}
    
    /**
     * Inserta un usuario nuevo en la base de datos
     * @param u Usuario nuevo que se introducira
     * @throws ErrorInsertarUsuario Error debido a que el deporte ya existe 
     */
    public void insert(Usuario u) throws ErrorInsertarUsuario{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Guarda el objeto en la base de datos
            em.persist(u);
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarUsuario();
        }
    }
    
    /**
     * Recupera un usuario de la base de datos
     * @param id nombre del usuario a recuperar
     * @return Usuario encontrado en la base de datos
     */
    public Usuario get(String id){
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        //Devuelve lo que encuentre en la base de datos
        return em.find(Usuario.class, id);
    }
    
    /**
     * Actualiza un usuario ya existente en la base de datos
     * @param u Usuario a actualizar
     * @throws ErrorActualizarUsuario error al actualizar el usuario en la bbdd
     */
    public void update(Usuario u) throws ErrorActualizarUsuario{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.merge(u);
            //Realiza la operacion
            em.getTransaction().commit();
        }catch (Exception ex){
            em.clear();
            throw new ErrorActualizarUsuario();
        }
        
    }
    
    /**
     * Borra un usuario de la base de datos
     * @param u Usuario que se debe borrar
     * @throws ErrorBorrarUsuario error al borrar el usuario
     */
    public void remove(Usuario u) throws ErrorBorrarUsuario{
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            //Actualiza el objeto en la base de datos
            em.remove(u);
            //Realiza la operacion
            em.getTransaction().commit();            
        }catch (Exception ex){
            em.clear();
            throw new ErrorBorrarUsuario();
        }
    }
    
    /**
     * Devuelve la instancia de DAOUsuario existente en el sistema, sino existe la crea
     * @return DAOUsuario instancia del DAO
     */
    public static DAOUsuario instancia(){
        //Comprueba que exista la instancia
        if (instancia==null){
            instancia=new DAOUsuario();
        }
        
        //La devuelve
        return instancia;
    }

    public void insert(Map usuarios) throws ErrorInsertarUsuario {
        //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        try{
            //Comienza la transaccion
            em.getTransaction().begin();
            Iterator<Map.Entry> iterator = usuarios.entrySet().iterator();
            while (iterator.hasNext()){
                //Guarda el objeto en la base de datos
                Usuario u = (Usuario) iterator.next().getValue();
                em.persist(u);
            }
            //Realiza la operacion
            em.getTransaction().commit();
            
        }catch(Exception e){
            //Si hay un error comprueba si esta activa y en ese caso hace un rollback
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.clear();
            throw new ErrorInsertarUsuario();
        }    
    }
}
