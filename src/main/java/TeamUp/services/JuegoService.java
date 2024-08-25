package TeamUp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TeamUp.daos.JuegoDAOimpl;
import TeamUp.entities.JuegoEntity;
import TeamUp.entities.UsuarioEntity;
import TeamUp.utils.ResponseData;

@Service
public class JuegoService {

	@Autowired
	private JuegoDAOimpl juegoDAO;
	
	private UserService userService;
	
	public void setUserService(UserService service) { // EVITAR DEPENDENCIAS CIRCULARES
		this.userService = service;
	}
	
	public JuegoEntity findGameById(Long id) throws Exception {
		return juegoDAO.findGameById(id);
	}

	public ResponseData obtenerJuegosUsuario(String username) throws Exception {
		ResponseData res = new ResponseData();
		res.setCode(0);
		
		// 1.- OBTENER LISTA DE JUEGOS DEL USUARIO
		List<JuegoEntity> juegosUsuario = userService.findUserByName(username).getJuegos_ids();	
		
		// 2.- OBTENGO LISTA DE JUEGOS TOTALES
		List<JuegoEntity> listaJuegos = juegoDAO.obtenerTodosLosJuegos();
		
		// 3.- FILTRAR JUEGOS QUE NO ESTAN EN LA LISTA DEL USUARIO
		List<JuegoEntity> juegosRestantes = listaJuegos.stream()
			.filter(juego -> !juegosUsuario.contains(juego))
			.collect(Collectors.toList());
		
		// 4.- TRATAR Y ENVIAR DATOS
		rellenaInformacionJuegos(res.getData(), juegosRestantes);
		return res;
	}

	public ResponseData eliminarJuego(String username, Long idJuego) throws Exception {
		ResponseData res = new ResponseData();
		res.setCode(0);
		
		// 1.- OBTENER USER
		UsuarioEntity user = userService.findUserByName(username);	
		
		// 2.- ELIMINAR JUEGO
		List<JuegoEntity> listaJuegos = user.getJuegos_ids();
		for (JuegoEntity juego : listaJuegos) {
	        if (juego.getId().equals(idJuego)) {
	        	listaJuegos.remove(juego);
	            break;
	        }
	    }
		
		// 3.- ACTUALIZAR USER
		userService.updateUser(user);	
		return res;
	}
	
	private void rellenaInformacionJuegos(Map<String, Object> info, List<JuegoEntity> listaJuegos) {
		List<Map<String, Object>> ListaJuegosFormateados = new ArrayList<>();
		
		for (JuegoEntity juego : listaJuegos) { // ID, NOMBRE, IMG
			Map<String, Object> juegoFormateado = new HashMap<>();
			juegoFormateado.put("id", juego.getId());
			juegoFormateado.put("nombre", juego.getNombre());
			juegoFormateado.put("img", juego.getImg());
			ListaJuegosFormateados.add(juegoFormateado);
		}
		info.put("juegos", ListaJuegosFormateados);
	}
}
