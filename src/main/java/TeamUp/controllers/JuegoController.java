package TeamUp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TeamUp.services.JuegoService;
import TeamUp.utils.ResponseData;

@RestController
@RequestMapping("/juegos")
public class JuegoController {

	@Autowired
	private JuegoService juegoService;
	
	@PostMapping("/obtenerJuegosUser")
    public ResponseData obtenerJuegosUser(@RequestBody Map<String, String> request) throws Exception {				
		String username = request.get("username");
        
        return juegoService.obtenerJuegosUsuario(username);
    }
	
	@PostMapping("/eliminarJuego")
    public ResponseData eliminarJuego(@RequestBody Map<String, Object> request) throws Exception {				
		String username = (String) request.get("username");
		
		Integer id = (Integer) request.get("idJuego");
		Long idJuego = id.longValue();
		
        return juegoService.eliminarJuego(username, idJuego);
    }
}
