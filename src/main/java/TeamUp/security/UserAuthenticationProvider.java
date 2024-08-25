package TeamUp.security;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import TeamUp.daos.UsuarioDAOimpl;
import TeamUp.entities.UsuarioEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;
	
	@Autowired
	private UsuarioDAOimpl usuarioDAO;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String login) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600000); // 1 HORA
		
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create()
			.withSubject(login)
			.withIssuedAt(now)
			.withExpiresAt(validity)
			.sign(algorithm);
	}
	
	public Authentication validateToken(String token) throws Exception {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT decoded = verifier.verify(token);
			UsuarioEntity user = usuarioDAO.obtenerUsuarioPorNombre(decoded.getSubject());
			
			return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
		
		} catch (Exception e) {
			throw new AccessDeniedException("Token expirado");
		}
	}
}
