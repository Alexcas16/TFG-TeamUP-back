package TeamUp.entities;

import TeamUp.enums.EnumTipoChat;
import TeamUp.enums.EnumTipoChatConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "posts")
public class PostEntity {

	@Id
	@Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private UsuarioEntity usuario_id;
	
	@ManyToOne
	@JoinColumn(name = "foro_id")
	private ForoEntity foro_id;
	
	@OneToOne(
			mappedBy = "post_id",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY
	)
	private MsgEntity msg_id;
	
	@OneToOne(
            mappedBy = "post_id",
            fetch = FetchType.LAZY
    )
    private ChatEntity chatHijoId;
	
	@Convert(converter = EnumTipoChatConverter.class)
	private EnumTipoChat tipo;
	
	@Column(name="num_users")
	private Integer num_users;
	
	public PostEntity() {}
    
    public PostEntity(UsuarioEntity usuario_id, ForoEntity foro_id, MsgEntity msg_id, EnumTipoChat tipo, Integer num_users) {
    	this.usuario_id = usuario_id;
    	this.foro_id = foro_id;
    	this.msg_id = msg_id;
    	this.tipo = tipo;
    	this.num_users = num_users;
	}
}
