package TeamUp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TeamUp.services.UserService;
import TeamUp.utils.ResponseData;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/addGame")
    public ResponseData addGame(@RequestBody Map<String, Object> data) throws Exception {				
		String username = (String) data.get("user");
		
		Integer idJuego = (Integer) data.get("game_id");
		Long idLong = idJuego.longValue();
		
		return userService.addGame(username, idLong);
	}
}
