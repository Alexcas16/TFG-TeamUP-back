package TeamUp.daos;

import java.util.List;

import org.springframework.stereotype.Repository;

import TeamUp.entities.JuegoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class JuegoDAOimpl {

	@PersistenceContext
	EntityManager em;

	public List<JuegoEntity> obtenerTodosLosJuegos() {
		try {
			return em.createQuery("SELECT j FROM JuegoEntity j", JuegoEntity.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JuegoEntity findGameById(Long juego_id) throws Exception {
		try {
			String hql = "FROM JuegoEntity j WHERE j.id = :juego_id";
	        return em.createQuery(hql, JuegoEntity.class)
	                 .setParameter("juego_id", juego_id)
	                 .getSingleResult();
	        
		} catch (NoResultException e) {
	        return null;
	        
	    } catch (Exception e) {
	        throw new Exception(e);
	    }
	}
	
}
