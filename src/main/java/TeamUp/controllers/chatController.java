package TeamUp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TeamUp.services.ChatService;
import TeamUp.utils.ResponseData;

@RestController
@RequestMapping("chat")
public class chatController {

	@Autowired
	private ChatService chatService;
	
	@PostMapping("/crearChat")
    public ResponseData crearChat(@RequestBody Map<String, Object> data) throws Exception {				
		String username = (String) data.get("username");
		Integer idPost = (Integer) data.get("postId");
		Long idLong = idPost.longValue();
		
		return chatService.createChat(username, idLong);
	}
	
	@PostMapping("/unirseGrupo")
    public ResponseData unirseGrupo(@RequestBody Map<String, Object> data) throws Exception {				
		String username = (String) data.get("username");
		Integer idPost = (Integer) data.get("postId");
		Long idLong = idPost.longValue();
		
		return chatService.unirseGrupo(username, idLong);
	}
	
	@PostMapping("/obtenerMensajesSala")
    public ResponseData obtenerMensajesSala(@RequestBody Map<String, Object> data) throws Exception {				
		Integer chatId = (Integer) data.get("chatId");
		Long idLong = chatId.longValue();
		
		return chatService.obtenerMensajesSala(idLong);
	}
	
	@PostMapping("/enviarMensaje")
    public ResponseData enviarMensaje(@RequestBody Map<String, Object> data) throws Exception {		
		Integer chatId = (Integer) data.get("chatId");
		Integer userId = (Integer) data.get("userId");
		String msg = (String) data.get("msg");
		Long chatIdLong = chatId.longValue();
		Long userIdLong = userId.longValue();
				
		return chatService.enviarMensaje(chatIdLong, userIdLong, msg);
	}
	
	@PostMapping("/salirChat")
    public ResponseData salirChat(@RequestBody Map<String, Object> data) throws Exception {		
		Integer chatId = (Integer) data.get("chatId");
		String userName = (String) data.get("userName");
		Long chatIdLong = chatId.longValue();
				
		return chatService.salirChat(chatIdLong, userName);
	}
	
	@PostMapping("/mostrarParticipantes")
    public ResponseData mostrarParticipantes(@RequestBody Map<String, Object> data) throws Exception {		
		Integer chatId = (Integer) data.get("chatId");
		Long chatIdLong = chatId.longValue();
				
		return chatService.mostrarParticipantes(chatIdLong);
	}
	
}
