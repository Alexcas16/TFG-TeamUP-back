package TeamUp.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TeamUp.daos.PostDAOimpl;
import TeamUp.entities.MsgEntity;
import TeamUp.entities.PostEntity;
import TeamUp.entities.UsuarioEntity;
import TeamUp.enums.EnumTipoChat;
import TeamUp.utils.ResponseData;

@Service
public class PostService {
	
	@Autowired
	private PostDAOimpl postDAO;
	
	@Autowired
	private JuegoService juegoService;
	
	@Autowired
	private UserService userService;
	
	public ResponseData sendPost(String userName, Long id, String texto, Integer type, Integer numParticipantes) throws Exception {
		ResponseData res = new ResponseData();
		res.setCode(0);
				
		// 1.- RECUPERAR USER
		UsuarioEntity user = userService.findUserByName(userName);
		
		// 2.- CREAR POST
		PostEntity post = new PostEntity(
				user,
				juegoService.findGameById(id).getForo_id(),
				null, // MSG VACÍO POR AHORA
				type == 0 ? EnumTipoChat.GROUP : (type == 1 ? EnumTipoChat.CHAT : EnumTipoChat.POST),
				numParticipantes
		); // 0 -> POST | 1 -> CHAT | >1 -> GROUP
		
		// 3.- CREAR MSG
		MsgEntity msg = new MsgEntity(
				texto,
				Timestamp.from(Instant.now()),
				null, // CHAT A NULL EN ESTOS CASOS
				user,
				post
		);
		
		// 4.- ASIGNAR MSG A POST Y PERSISTIR
		post.setMsg_id(msg);
		postDAO.persistPost(post);
		
		// 5.- SETEAR INFO NECESARIA
		res.getData().put("img", user.getImg());
		res.getData().put("type", post.getTipo().getValue());
		res.getData().put("post_id", post.getId());
		
		return res;
	}

	public ResponseData obtainForumPosts(Long id) throws Exception {
		ResponseData res = new ResponseData();
		res.setCode(0);
		
		// 1.- OBTENER FORO
		Long id_foro =  juegoService.findGameById(id).getForo_id().getId();
		
		// 2.- OBTENER LOS POSTS
		// TODO - LIMITAR NUMERO
		List<PostEntity> listaPost = postDAO.obtenerPostPorForo(id_foro);
		
		// 2.5.- SI ES VACIO, EVITO OPERACIONES Y TERMINO
		if (listaPost == null || listaPost.isEmpty()) {
			res.setCode(1);
			return res;
		}
		
		// 3.- TRATAR Y ENVIAR DATOS
		rellenaInformacionPostst(res.getData(), listaPost);
		return res;
	}
	
	public PostEntity findPostById(Long id) throws Exception {
		return postDAO.findPostById(id);
	}

	private void rellenaInformacionPostst(Map<String, Object> info, List<PostEntity> listaPost) {
		List<Map<String, Object>> ListaPostsFormateados = new ArrayList<>();
		
		for (PostEntity post : listaPost) {
			Map<String, Object> postFormateado = new HashMap<>();
			postFormateado.put("id", post.getId());
			postFormateado.put("img", post.getUsuario_id().getImg());
			postFormateado.put("username", post.getUsuario_id().getUsuario());
			postFormateado.put("texto", post.getMsg_id().getTexto());
			postFormateado.put("tipo", post.getTipo().getValue());
			ListaPostsFormateados.add(postFormateado);
		}
		info.put("posts", ListaPostsFormateados);
	}
	
}
