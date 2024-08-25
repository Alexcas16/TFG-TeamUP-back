package TeamUp.entities;

import java.util.ArrayList;
import java.util.List;

import TeamUp.enums.EnumTipoChat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "chats")
public class ChatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String img;
	
	@ManyToMany(mappedBy = "chats_ids")
	private List<UsuarioEntity> usuario_id;
	
	@OneToMany(
			mappedBy="chat_id",
    		cascade = CascadeType.ALL,
    		orphanRemoval = true
    )
	private List<MsgEntity> msgs_ids;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post_id;
	
	private EnumTipoChat tipo;
	
	public ChatEntity() {
		this.msgs_ids = new ArrayList<MsgEntity>();
	}
	
	public ChatEntity(String name, String img, List<UsuarioEntity> users_ids, EnumTipoChat tipo, PostEntity post_id) {
		this.name = name;
		this.img = img;
		this.usuario_id = users_ids;
		this.msgs_ids = new ArrayList<MsgEntity>();
		this.tipo = tipo;
		this.post_id = post_id;
	}
}
