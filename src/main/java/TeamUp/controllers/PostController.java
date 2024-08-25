package TeamUp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TeamUp.enums.EnumTipoChat;
import TeamUp.services.PostService;
import TeamUp.utils.ResponseData;

@RestController
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@PostMapping("/sendPost")
    public ResponseData sendPost(@RequestBody Map<String, Object> data) throws Exception {				
		String username = (String) data.get("user");
		
        Integer id = (Integer) data.get("game");
        Long idLong = id.longValue();
        
        String text = (String) data.get("text");
        
        Integer numParticipantes = (Integer) data.get("numParticipantes");
        
        Integer type = (numParticipantes == 0) ? EnumTipoChat.POST.getValue() : (numParticipantes >= 2) ? EnumTipoChat.GROUP.getValue() : EnumTipoChat.CHAT.getValue() ;
        
        return postService.sendPost(username, idLong, text, type, numParticipantes);
    }
	
	@PostMapping("/obtenerPostsForo")
    public ResponseData obtenerPostsForo(@RequestBody Map<String, Object> data) throws Exception {				
		Integer id = (Integer) data.get("idJuego");
		Long idLong = id.longValue();
		
        return postService.obtainForumPosts(idLong);
    }
}
