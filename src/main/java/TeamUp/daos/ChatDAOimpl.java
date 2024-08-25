package TeamUp.daos;

import org.springframework.stereotype.Repository;

import TeamUp.entities.ChatEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class ChatDAOimpl {

	@PersistenceContext
	EntityManager em;
	
	public void persistChat(ChatEntity chat) {
		try {
            em.persist(chat);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el chat", e);
        }
	}
	
	public void updateChat(ChatEntity chat) {
		try {
            em.merge(chat);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el chat", e);
        }
	}
	
	public ChatEntity findChatByPostId(Long idPost) {
		try {
			String hql = "SELECT c FROM ChatEntity c WHERE c.post_id.id = :idPost";
			return em.createQuery(hql, ChatEntity.class)
	                .setParameter("idPost", idPost)
	                .getSingleResult();

        } catch (NoResultException e) {
            return null;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el chat por idPost", e);
        }
	}

	public ChatEntity findChatBytId(Long idChat) {
		try {
			String hql = "SELECT c FROM ChatEntity c WHERE c.id = :idChat";
			return em.createQuery(hql, ChatEntity.class)
	                .setParameter("idChat", idChat)
	                .getSingleResult();

        } catch (NoResultException e) {
            return null;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el chat por id", e);
        }
	}
	
}
