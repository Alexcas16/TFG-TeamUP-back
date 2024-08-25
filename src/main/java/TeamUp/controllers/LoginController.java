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
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/tryLogin")
    public ResponseData tryLogin(@RequestBody Map<String, String> loginRequest) {				
		String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        return userService.validateLogin(username, password);
    }
	
	@PostMapping("/register")
    public ResponseData registrarUsuario(@RequestBody Map<String, String> registryRequest) {				
		String username = registryRequest.get("username");
		String email = registryRequest.get("email");
        String password = registryRequest.get("password");
        
		return userService.registryUser(username, email, password);
    }
}
