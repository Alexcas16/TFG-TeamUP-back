package TeamUp.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TeamUp.daos.ChatDAOimpl;
import TeamUp.entities.ChatEntity;
import TeamUp.entities.MsgEntity;
import TeamUp.entities.PostEntity;
import TeamUp.entities.UsuarioEntity;
import TeamUp.enums.EnumTipoChat;
import TeamUp.utils.ResponseData;
import jakarta.annotation.PostConstruct;

@Service
public class ChatService {

	@Autowired
	private ChatDAOimpl chatDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private MsgService msgService;
	
	// VARIABLES GLOBALES
	private static final Integer EXITO = 0;
	private static final Integer YA_EXISTE = 1;
	private static final Integer COMPLETO = 2;
	private static final Integer CHAT_VACIO = 3;

	@PostConstruct
    public void init() { // EVITAR DEPENDENCIAS CIRCULARES
		msgService.setChatService(this);
    }
	
	public ResponseData createChat(String username, Long idLong) throws Exception {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER USUARIO
		UsuarioEntity user = userService.findUserByName(username);
		
		// 2.- COMPROBAR SI YA TIENE EL CHAT
		List<ChatEntity> listaChats = user.getChats_ids();
		for (ChatEntity chat : listaChats) {
			if (chat.getPost_id().getId().equals(idLong)) {
				res.setCode(YA_EXISTE); // 2.1.- CHAT YA EXISTE
				return res;
			}
		}
		
		// 3.- COMPROBAR SI CHAT YA EXISTE
		PostEntity post = postService.findPostById(idLong);
		UsuarioEntity creador = post.getUsuario_id();
		Long idPost = post.getId();
		
		ChatEntity chat = findChatByPostId(idPost);
		if(chat != null) { // 4.- SI EXISTE, AÑADIR USER SI CABE
			if(chat.getUsuario_id().size() < 2) { // LOS CHATS SON DE 2 USUARIOS
				// 5.- UPDATE CHAT
				chat.getUsuario_id().add(user);
				chatDAO.updateChat(chat);
				
				// 6.- UPDATE USER
				user.getChats_ids().add(chat);
				userService.updateUser(user);
				
				// 7.- WELCOME MSG
				msgService.createWelcomeMsg(user, chat);
				
				// FINALIZAR
				res.getData().put("id", chat.getId());
				res.getData().put("img", chat.getImg());
				res.getData().put("name", chat.getName());
				res.setCode(EXITO);
			} else {
				res.setCode(COMPLETO);
				return res;
			}
		} else {// 4.- SI NO EXISTE, CREARLO
			// 4.- PREPARAR PARTICIPANTES
			List<UsuarioEntity> participantes = new ArrayList<UsuarioEntity>();
			participantes.add(user);
			participantes.add(creador);
			
			// 5.- CREAR CHAT
			ChatEntity newChat = new ChatEntity(
					creador.getUsuario() + "_chat", 
					"/images/chats/chat_pic_default.png", // DEFAULT
					participantes, 
					EnumTipoChat.CHAT, 
					post
			);
			
			// 6.- PERSISTIR CHAT
			chatDAO.persistChat(newChat);
			
			// 7.- AÑADIR CHAT A LISTA CHATS DE USUARIOS
			user.getChats_ids().add(newChat);
			creador.getChats_ids().add(newChat);
			
			// 8.- ACTUALIZAR USERS
			userService.updateUser(user);
			userService.updateUser(creador);
			
			// 9.- WELCOME MSG
			msgService.createWelcomeMsg(user, newChat);
			
			// 10.- FINALIZAR
			res.getData().put("id", newChat.getId());
			res.getData().put("img", newChat.getImg());
			res.getData().put("name", newChat.getName());
			
			res.setCode(EXITO);
		}
		return res;
	}

	public ResponseData unirseGrupo(String username, Long idLong) throws Exception {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER USUARIO
		UsuarioEntity user = userService.findUserByName(username);
		
		// 2.- COMPROBAR SI YA TIENE EL CHAT
		List<ChatEntity> listaChats = user.getChats_ids();
		for (ChatEntity chat : listaChats) {
			if (chat.getPost_id().getId().equals(idLong)) {
				res.setCode(YA_EXISTE); // 2.1.- CHAT YA EXISTE
				return res;
			}
		}
		
		// 3.- COMPROBAR SI CHAT YA EXISTE
		PostEntity post = postService.findPostById(idLong);
		UsuarioEntity creador = post.getUsuario_id();
		Long idPost = post.getId();
		Integer numUsers = post.getNum_users();
		
		ChatEntity chat = findChatByPostId(idPost);
		if(chat != null) { // 4.- SI EXISTE, AÑADIR USER SI CABE
			if(chat.getUsuario_id().size() <= numUsers.intValue()) {
				
				// 5.- UPDATE CHAT
				chat.getUsuario_id().add(user);
				chatDAO.updateChat(chat);
				
				// 6.- UPDATE USER
				user.getChats_ids().add(chat);
				userService.updateUser(user);

				// 7.- WELCOME MSG
				msgService.createWelcomeMsg(user, chat);
				
				// FINALIZAR
				res.getData().put("id", chat.getId());
				res.getData().put("img", chat.getImg());
				res.getData().put("name", chat.getName());
				res.setCode(EXITO);
				
			} else { // COMPLETO
				res.setCode(COMPLETO);
				return res;
			}
		} else { // 4.- SI NO EXISTE, CREARLO
			// 5.- PREPARAR PARTICIPANTES
			List<UsuarioEntity> participantes = new ArrayList<UsuarioEntity>();
			participantes.add(user);
			participantes.add(creador);
			
			ChatEntity newChat = new ChatEntity(
					creador.getUsuario() + "_grupo", 
					"/images/chats/group_pic_default.png", // DEFAULT
					participantes, 
					EnumTipoChat.GROUP, 
					post
			);
			
			// 6.- PERSISTIR CHAT
			chatDAO.persistChat(newChat);
			
			// 7.- AÑADIR CHAT A LISTA CHATS DE USUARIOS
			user.getChats_ids().add(newChat);
			creador.getChats_ids().add(newChat);
			
			// 8.- ACTUALIZAR USERS
			userService.updateUser(user);
			userService.updateUser(creador);
			
			// 9.- WELCOME MSG
			msgService.createWelcomeMsg(user, newChat);
			
			// FINALIZAR
			res.getData().put("id", newChat.getId());
			res.getData().put("img", newChat.getImg());
			res.getData().put("name", newChat.getName());
			
			res.setCode(EXITO);
		}
		return res;
	}

	public ChatEntity findChatByPostId(Long idPost) {
		return chatDAO.findChatByPostId(idPost);
	}

	public ResponseData obtenerMensajesSala(Long idLong) throws Exception {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER MENSAJES DEL CHAT (50 ÚLTIMOS)
		List<MsgEntity> listaMensajes = msgService.obtenerMensajesSala(idLong);
		
		// 2.- COMPROBAR QUE NO SEA VACÍO
		if(listaMensajes.isEmpty()) {
			res.setCode(CHAT_VACIO);
			return res;
		}
		
		// 3.- PARSEAR Y ENVIAR INFO
		parsearListaMensajes(listaMensajes, res.getData());
		res.setCode(EXITO);
		return res;
	}
	
	public ResponseData enviarMensaje(Long chatIdLong, Long userIdLong, String text) throws Exception {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER CHAT Y USER
		ChatEntity chat = chatDAO.findChatBytId(chatIdLong);
		UsuarioEntity user = userService.findUserById(userIdLong);
		
		// 2.- CREAR MENSAJE
		MsgEntity msg = new MsgEntity(
				text,
				Timestamp.from(Instant.now()),
				chat,
				user,
				null // POST NULL EN ESTE CASO
		);
		msgService.persistMsg(msg);
		
		// 3.- ACTUALIZAR EL CHAT
		chat.getMsgs_ids().add(msg);
		chatDAO.updateChat(chat);
		
		// 4.- PARSEAR DATOS Y ENVIAR
		parsearMensajeEnviado(msg, res.getData());
		res.setCode(0);
		return res;
	}
	
	public ResponseData salirChat(Long chatIdLong, String userName) throws Exception {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER CHAT Y USER
		ChatEntity chat = chatDAO.findChatBytId(chatIdLong);
		
		// 2.- BUSCAR Y ELIMINAR USER
		List<UsuarioEntity> listaUsuarios = chat.getUsuario_id();
		
		for (UsuarioEntity user : listaUsuarios) {
			if (user.getUsuario().equals(userName)) {
				try {
					// 3.- ELIMINAR RELACION Y ACTUALIZAR
					user.getChats_ids().remove(chat);
					userService.updateUser(user);
					
					listaUsuarios.remove(user);
					chat.setUsuario_id(listaUsuarios);
					chatDAO.updateChat(chat);
					
					// 4.- BYE MSG
					msgService.createByeMsg(user, chat);

					res.setCode(0); // EXITO
					return res;
					
				} catch (Exception e) {
					throw new RuntimeException("Error al eliminar usuario de la lista del chat");
				}
			}
		}
		// GESTION DE ERRORES
		throw new RuntimeException("Usuario no encontrado. Caso no posible");
	}
	
	public void updateChat(ChatEntity chat) {
		chatDAO.updateChat(chat);
	}
	
	public ResponseData mostrarParticipantes(Long chatIdLong) {
		ResponseData res = new ResponseData();
		
		// 1.- OBTENER CHAT
		ChatEntity chat = chatDAO.findChatBytId(chatIdLong);
		
		// 2.- OBTENER PARTICIPANTES
		if (chat != null) {
			List<UsuarioEntity> listaUsuarios = chat.getUsuario_id();
			
			if (!listaUsuarios.isEmpty()) {
				List<String> listaNombresUsuarios = new ArrayList<String>();
				
				for (UsuarioEntity user : listaUsuarios) {
					listaNombresUsuarios.add(user.getUsuario());
				}
				
				res.setCode(EXITO); // 0
				res.getData().put("usuarios", listaNombresUsuarios);
				
			} else {
				res.setCode(CHAT_VACIO); // 3
			}
		} else {
			res.setCode(99); // ERROR NO CONTROLADO, NO EXISTE EL CHAT
		}
		return res;
	}
	
	private void parsearListaMensajes(List<MsgEntity> listaMensajes, Map<String, Object> data) {
		List<Map<String, Object>> ListaMensajesFormateados = new ArrayList<>();
		
		for (MsgEntity msg : listaMensajes) { // ID, TIME, USERNAME, USERPIC
			Map<String, Object> msgFormateado = new HashMap<>();
			msgFormateado.put("id", msg.getId());
			msgFormateado.put("time", msg.getTime_stamp());
			msgFormateado.put("username", msg.getUsuario_id().getUsuario());
			msgFormateado.put("img", msg.getUsuario_id().getImg());
			msgFormateado.put("text", msg.getTexto());
			ListaMensajesFormateados.add(msgFormateado);
		}
		data.put("messages", ListaMensajesFormateados);
	}
	
	private void parsearMensajeEnviado(MsgEntity msg, Map<String, Object> map) {
		Map<String, Object> msgParseado = new HashMap<String, Object>();
		
		msgParseado.put("id", msg.getId());
		msgParseado.put("time", msg.getTime_stamp());
		msgParseado.put("username", msg.getUsuario_id().getUsuario());
		msgParseado.put("img", msg.getUsuario_id().getImg());
		msgParseado.put("text", msg.getTexto());
		
		map.put("message", msgParseado);
	}
}
