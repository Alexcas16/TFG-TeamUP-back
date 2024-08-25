package TeamUp.daos;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import TeamUp.entities.JuegoEntity;
import TeamUp.entities.UsuarioEntity;
import TeamUp.utils.TeamUPexception;
import TeamUp.utils.TeamUPexception.CodigoError;
import ch.qos.logback.classic.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UsuarioDAOimpl {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private JuegoDAOimpl juegoDAO;
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(UsuarioDAOimpl.class);
	
	@Transactional(rollbackOn = TeamUPexception.class)
	public void guardarUsuario(UsuarioEntity usuario) throws Exception {
		try {
			em.persist(usuario);
		} catch (PersistenceException e2) {
			logger.error("Error al intentar persistir el usuario: " + usuario.getUsuario(), e2);
	
			Throwable cause = e2.getCause(); // TODO - CHAPUZA
			if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
				String message = e2.getMessage();
	
				if (message.contains("usuarios.usuario")) { // NOMBRE DUPLICADO
					throw new TeamUPexception("Nombre de usuario ya existe", CodigoError.USERNAME_YA_EXISTE);
				} else if (message.contains("usuarios.email")) { // CORREO DUPLICADO
					throw new TeamUPexception("Email ya existe", CodigoError.EMAIL_YA_EXISTE);
				}
				} else {
					throw new PersistenceException("Error al intentar persistir el usuario", e2); 
				}
			throw e2;  
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public UsuarioEntity obtenerUsuarioPorNombre(String nombreUsuario) throws Exception {				
		try {
			String hql = "FROM UsuarioEntity u WHERE u.usuario = :nombreUsuario";
			return em.createQuery(hql, UsuarioEntity.class)
				.setParameter("nombreUsuario", nombreUsuario)
				.getSingleResult();
	
		} catch (NoResultException e) {
			logger.info("User no encontrado");
			throw new TeamUPexception("User no encontrado", CodigoError.USER_NO_EXISTE);
	
		} catch (Exception e) {
			logger.error("Excepcion no controlada", e);
			throw new Exception(e);
		}
	}
	
	public UsuarioEntity obtenerUsuarioPorEmail(String email) throws Exception {
		try {
			String hql = "FROM UsuarioEntity u WHERE u.email = :email";
			return em.createQuery(hql, UsuarioEntity.class)
				.setParameter("email", email)
				.getSingleResult();
		
		} catch (NoResultException e) {
			return null;
		
		} catch (Exception e) {
			logger.error("Excepcion no controlada", e);
			throw new Exception(e);
		}
	}
	
	@Transactional(rollbackOn = TeamUPexception.class)
	public void addGameById(UsuarioEntity newUser, Long gameId) throws TeamUPexception {
		try {
			// ENCONTRAR JUEGO
			JuegoEntity juego = juegoDAO.findGameById(gameId);
			
			if (juego == null) {
				throw new TeamUPexception("No se ha podido encontrar este juego", CodigoError.JUEGO_NO_EXISTE); // code 5
			}
			// JUEGOS DEL USUARIO
			List<JuegoEntity> listaJuegos = newUser.getJuegos_ids();
			
			// YA TIENE EL JUEGO?
			if (!listaJuegos.contains(juego)) {
				listaJuegos.add(juego);
				// ACTUALIZAR LISTA Y USUARIO
				newUser.setJuegos_ids(listaJuegos);
				em.merge(newUser);        
			} else {
				throw new TeamUPexception("El usuario ya tiene este juego en su lista", CodigoError.JUEGO_YA_EXISTE); // code 
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al añadir el juego al usuario", e);
		}
	}
	
	public void updateUser(UsuarioEntity user) {
		try {
			em.merge(user);
		} catch (Exception e) {
			throw new RuntimeException("Error al actualizar el usuario", e);
		}
	}
	
	public UsuarioEntity obtenerUsuarioPorId(Long id) throws Exception {
		try {
			String hql = "FROM UsuarioEntity u WHERE u.id = :id";
			return em.createQuery(hql, UsuarioEntity.class)
				.setParameter("id", id)
				.getSingleResult();
		
		} catch (NoResultException e) {
			logger.info("User no encontrado");
			throw new TeamUPexception("User no encontrado", CodigoError.USER_NO_EXISTE);
		
		} catch (Exception e) {
			logger.error("Excepcion no controlada", e);
			throw new Exception(e);
		}
	}
}
