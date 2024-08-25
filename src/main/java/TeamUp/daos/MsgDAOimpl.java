package TeamUp.daos;

import java.util.List;

import org.springframework.stereotype.Repository;

import TeamUp.entities.MsgEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MsgDAOimpl {

	@PersistenceContext
	EntityManager em;

	public List<MsgEntity> obtenerMensajesSalaPorId(Long id) throws Exception {
        try {
            String hql = "SELECT m FROM MsgEntity m WHERE m.chat_id.id = :id ORDER BY m.time_stamp DESC";
            return em.createQuery(hql, MsgEntity.class)
                    .setParameter("id", id)
                    .setMaxResults(50)
                    .getResultList();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

	public void persistMsg(MsgEntity msg) {
		try {
			em.persist(msg);
		} catch (Exception e) {
			throw new RuntimeException("Error al persistir el mensaje", e);
		}
	}	
}
