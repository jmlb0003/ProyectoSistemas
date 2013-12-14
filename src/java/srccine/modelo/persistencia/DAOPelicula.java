package srccine.modelo.persistencia;

import java.util.HashMap;
import srccine.modelo.persistencia.excepciones.ErrorActualizarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorBorrarPelicula;
import srccine.modelo.persistencia.excepciones.ErrorInsertarPelicula;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import srccine.modelo.Pelicula;

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

    /**
     * Busca el numero de peliculas que hay en la BBDD
     * @return Numero de peliculas que hay en el sistema
     */
    public long getNumPeliculas() {
         //Obtiene la instancia del EntityManager del gestor de persistenciaa
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
        
        //Busca el partido creando un query
        Query num = em.createQuery("select COUNT(*) from Pelicula");
        return (Long) num.getSingleResult();
    }
    
    /**
     * Obtiene todas las peliculas de la BBDD
     * @return Devuelve un mapa con todas las peliculas de la BBDD
     */
    public Map<Long,Pelicula> get() {
        EntityManager em=GestorPersistencia.instancia().getEntityManager();

        Map<Long,Pelicula> map = new HashMap(); 
                
        Query consulta = em.createQuery("SELECT p FROM Pelicula p ");
        List peliculas = consulta.getResultList();
        for (Iterator it = peliculas.iterator(); it.hasNext();) {
            Pelicula p = (Pelicula) it.next();
            map.put(p.obtieneID(), p);
        }
        return map;
    }    
    
    /**
     * Busca las peliculas por titulo
     * @param titulo Subcadena que debe contener el titulo de las peliculas
     * @return Lista con las peliculas coincidentes con el titulo pasado por argumento
     */
    public List get(String titulo) {
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
                
        Query consulta = em.createQuery("SELECT p FROM Pelicula p Where p._titulo LIKE '%:consulta%';").
                setParameter("consulta", titulo);
        
        return consulta.getResultList();
    }    
    
    /**
     * Busca las peliculas con mayor media de la BBDD
     * @return Lista con las peliculas con mejor media de la BBDD
     */
    public List getMejorValoradas() {
        EntityManager em=GestorPersistencia.instancia().getEntityManager();
                
        Query consulta = em.createQuery("SELECT p FROM Pelicula p ORDER BY p._media DESC LIMIT :recomendaciones").
                setParameter("recomendaciones", srccine.modelo.Modelo.NUM_RECOMENDACIONES);
        
        return consulta.getResultList();
    }
    
}
