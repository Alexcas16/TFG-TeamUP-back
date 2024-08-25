package TeamUp.daos;

import java.util.List;

import org.springframework.stereotype.Repository;

import TeamUp.entities.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PostDAOimpl {

	@PersistenceContext
	EntityManager em;

	public List<PostEntity> obtenerPostPorForo(Long id_foro) throws Exception {
		try {
			String hql = "SELECT p FROM PostEntity p WHERE p.foro_id.id = :id_foro ORDER BY p.foro_id DESC";
	        return em.createQuery(hql, PostEntity.class)
		        .setParameter("id_foro", id_foro)
		        .getResultList();
	        
	    } catch (Exception e) {
	        throw new Exception(e);
	    }
	}
	
	public void persistPost(PostEntity post) {
		try {
            em.persist(post);
        } catch (Exception e) {
            throw new RuntimeException("Error al persistir el post", e);
        }
	}
	
	public PostEntity findPostById(Long id) throws Exception {
		try {
            return em.find(PostEntity.class, id);
        } catch (Exception e) {
            throw new Exception("Error al obtener el post por ID. ID: " + id , e);
        }
	}
	
}