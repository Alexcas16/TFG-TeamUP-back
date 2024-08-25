package TeamUp.entities;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "msgs")
public class MsgEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String texto;
	
	private Timestamp time_stamp;
	
	@ManyToOne
	@JoinColumn(name = "chat_id")
	private ChatEntity chat_id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private UsuarioEntity usuario_id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity post_id;
	
	public MsgEntity() {}
	
	public MsgEntity(String texto, Timestamp time_stamp, ChatEntity chat_id, UsuarioEntity usuario_id, PostEntity post_id) {
		this.texto = texto;
		this.time_stamp = time_stamp;
		this.chat_id = chat_id;
		this.usuario_id = usuario_id;
		this.post_id = post_id;
	}
}
