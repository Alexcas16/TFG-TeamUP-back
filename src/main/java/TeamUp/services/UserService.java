package TeamUp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TeamUp.daos.UsuarioDAOimpl;
import TeamUp.entities.ChatEntity;
import TeamUp.entities.JuegoEntity;
import TeamUp.entities.UsuarioEntity;
import TeamUp.security.PasswordManager;
import TeamUp.security.UserAuthenticationProvider;
import TeamUp.utils.ResponseData;
import TeamUp.utils.TeamUPexception;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {
	
	@Autowired
	private UsuarioDAOimpl usuarioDAO;
	
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;
	
	@Autowired
	private JuegoService juegoService;
	
	@PostConstruct
    public void init() { // EVITAR DEPENDENCIAS CIRCULARES
		juegoService.setUserService(this);
    }
			
	// VARIABLES PARA LOGIN
	private static final Integer CONSTASENYA_INCORRECTA = 4;
	
	public ResponseData validateLogin(String userName, String pass) {		
		ResponseData res = new ResponseData();
		UsuarioEntity user = null;
		
		try { // 1. OBTENER USUARIO
			user = usuarioDAO.obtenerUsuarioPorNombre(userName);
			
			if(!PasswordManager.checkPassword(pass, user.getPassword_hash())) { // 3. ENTRA SI PASS INCORRECTA
				res.setCode(CONSTASENYA_INCORRECTA); // CODE 2
				
			} else { // 2. ASIGNAR TOKEN
				String token = userAuthenticationProvider.createToken(user.getUsuario());
	        		     
				// 3. MANDAR TOKEN
				res.getData().put("token", token);
				
				// 4. MANDAR RESTO DE CAMPOS NECESARIOS
				rellenarInfoUsuario(res.getData(), user); 
				res.setCode(0);
			}	
			return res;
			
		} catch (TeamUPexception e) { // NO EXISTE
			e.printStackTrace();
			res.setCode(e.getCodigoError().getCodigo());
			return res;
			
		} catch (Exception e) { // ERROR NO CONTROLADO
			e.printStackTrace();
			res.setCode(99);
			return res;
		}
	}
	
	public ResponseData registryUser(String userName, String email, String pass) {
		ResponseData res = new ResponseData();
		
		// 1. APLICAR ENCRIPTADO A PASS
		String hashedPass = PasswordManager.hashPassword(pass);
	
		// 2. CREAR NUEVO USER
		UsuarioEntity newUser = new UsuarioEntity(userName, email, hashedPass);
		try {
			// 3.- PERSISTIR USER Y AÑADIR JUEGO 'LUPA'
			usuarioDAO.addGameById(newUser, (long) 999);		
			// 4. CREAR TOKEN
			String token = userAuthenticationProvider.createToken(newUser.getUsuario());
			
			res.setCode(0);
			res.getData().put("token", token);
			
		} catch (TeamUPexception e) { //USER O EMAIL YA EXISTEN
			e.printStackTrace();
			res.setCode(e.getCodigoError().getCodigo());
		
		} catch (Exception e2) { // ERROR NO CONTROLADO
			e2.printStackTrace();
			res.setCode(99);
		}
		return res;
	}
	
	public UsuarioEntity findUserByName(String username) throws Exception {
		return usuarioDAO.obtenerUsuarioPorNombre(username);
	}
	
	public UsuarioEntity findUserById(Long id) throws Exception {
		return usuarioDAO.obtenerUsuarioPorId(id);
	}
	
	public ResponseData addGame(String username, Long idLong) throws Exception {
		ResponseData res = new ResponseData();
		res.setCode(0); // Éxito por defecto
		
		// 1.- Obtener user
		UsuarioEntity user = findUserByName(username);
		
		// 2.- Obtener juego
		JuegoEntity juego = juegoService.findGameById(idLong);
		
		// 3.- Actualizar lista de juego y persistir
		user.getJuegos_ids().add(juego);
		usuarioDAO.updateUser(user);
		
		// 4.- Preparar info para front
		Map<String, Object> juegoFormateado = new HashMap<String, Object>();
		juegoFormateado.put("id", juego.getId());
		juegoFormateado.put("img", juego.getImg());
		juegoFormateado.put("altText", juego.getNombre());
		
		res.getData().put("juego", juegoFormateado);
		
		return res;
	}
	
	public void updateUser(UsuarioEntity user) {
		usuarioDAO.updateUser(user);
	}
	
	/**
	 * @param info
	 * @param user
	 * Funcion auxiliar para que quede mas limpio.
	 * @return info
	 */
	private void rellenarInfoUsuario(Map<String,Object> info, UsuarioEntity user) {
		// 4.1- Juegos
		List<Map<String, Object>> juegos = new ArrayList<>();
		for (JuegoEntity juego : user.getJuegos_ids()) {
		    Map<String, Object> juegoTuple = new HashMap<>();
		    juegoTuple.put("id", juego.getId());
		    juegoTuple.put("img", juego.getImg());
		    juegos.add(juegoTuple);
		}
		info.put("juegos", juegos);
		
		// 4.2- Chats
		List<Map<String, Object>> chats = new ArrayList<>();
		for (ChatEntity chat : user.getChats_ids()) {
		    Map<String, Object> chatTriple = new HashMap<>();
		    chatTriple.put("id", chat.getId());
		    chatTriple.put("img", chat.getImg());
		    chatTriple.put("name", chat.getName());
		    chats.add(chatTriple);
		}
		info.put("chats", chats);
		
		// 4.3- Usuario
		info.put("userName", user.getUsuario());
		
		// 4.4- Img
		info.put("img", user.getImg());
		
		// 4.5- userId
		info.put("userId", user.getId());
	}
}
