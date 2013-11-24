package modelo.persistencia;

import modelo.persistencia.excepciones.ErrorConexionBD;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GestorPersistencia {
    private EntityManagerFactory emf;
    private EntityManager em;
    
    private static GestorPersistencia instancia = null;
    
    private GestorPersistencia() throws ErrorConexionBD {
        try {
            emf = Persistence.createEntityManagerFactory("SGBDCinePU");
            em = emf.createEntityManager();
        }catch  (Exception e){
            throw new ErrorConexionBD();
        }        
    }
    
    public EntityManager getEntityManager() {
        return em;
    }
        
    public static void crearConexion() throws ErrorConexionBD {
        if (instancia == null) {
                instancia = new GestorPersistencia();
        }
    }
    
    public static GestorPersistencia instancia() {
        return instancia;
    }
    
    public static void desconectar() {
        if (instancia != null) {
            instancia.em.getTransaction().begin();
            instancia.em.createNativeQuery("shutdown").executeUpdate();
            instancia.em.getTransaction().commit();
            instancia.em.close();
            instancia.emf.close();
            instancia = null;
        }
    }
}