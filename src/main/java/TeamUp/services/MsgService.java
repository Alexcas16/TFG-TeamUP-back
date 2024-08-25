package TeamUp.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TeamUp.daos.MsgDAOimpl;
import TeamUp.entities.ChatEntity;
import TeamUp.entities.MsgEntity;
import TeamUp.entities.UsuarioEntity;

@Service
public class MsgService {
	
	@Autowired
	private MsgDAOimpl msgDAO;
	

	private ChatService chatService;
	
	public void setChatService(ChatService chatService) { // EVITAR DEPENDENCIAS CIRCULARES
		this.chatService = chatService;
	}
	
	public List<MsgEntity> obtenerMensajesSala(Long id) throws Exception {
		return msgDAO.obtenerMensajesSalaPorId(id);
	}
	
	public void persistMsg(MsgEntity msg) {
		msgDAO.persistMsg(msg);
	}
	
	public void createWelcomeMsg(UsuarioEntity user, ChatEntity chat) {
		String welcome = "** " + user.getUsuario() + " se unió al grupo **";
		
		// 1.- CREAR MENSAJE
		MsgEntity msg = new MsgEntity(
				welcome,
				Timestamp.from(Instant.now()),
				chat,
				user,
				null // POST NULL EN ESTE CASO
		);
		this.persistMsg(msg);
		
		// 3.- ACTUALIZAR EL CHAT
		chat.getMsgs_ids().add(msg);
		chatService.updateChat(chat);
	}
	
	public void createByeMsg(UsuarioEntity user, ChatEntity chat) {
		String welcome = "** " + user.getUsuario() + " abandonó el grupo **";
		
		// 1.- CREAR MENSAJE
		MsgEntity msg = new MsgEntity(
				welcome,
				Timestamp.from(Instant.now()),
				chat,
				user,
				null // POST NULL EN ESTE CASO
		);
		this.persistMsg(msg);
		
		// 3.- ACTUALIZAR EL CHAT
		chat.getMsgs_ids().add(msg);
		chatService.updateChat(chat);
	}
}
