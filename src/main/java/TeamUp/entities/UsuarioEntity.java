package TeamUp.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "usuarios")
public class UsuarioEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true)
    private String usuario;

	private String email;
	
    private String password_hash;
    
    private String img;
    
    @ManyToMany
    @JoinTable(
        name = "usuario_juego",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "juego_id")
    )
    @OrderBy("id ASC")
    private List<JuegoEntity> juegos_ids;
    
    @ManyToMany
    @JoinTable(
        name = "usuario_chat",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<ChatEntity> chats_ids;
    
    @OneToMany(
    		mappedBy="usuario_id",
    		cascade = CascadeType.ALL,
    		orphanRemoval = true
    )
    private List<PostEntity> posts;
    
    @OneToMany(
    		mappedBy="usuario_id",
    		cascade = CascadeType.ALL,
    		orphanRemoval = true
    )
    private List<MsgEntity> msgs;
    
    public UsuarioEntity() {
    	this.juegos_ids = new ArrayList<JuegoEntity>();
    	this.chats_ids = new ArrayList<ChatEntity>();
    	this.posts = new ArrayList<PostEntity>();
    	this.msgs = new ArrayList<MsgEntity>();
    }
    
    public UsuarioEntity(String usuario, String email, String pass) {
    	this.usuario = usuario;
    	this.email = email;
    	this.password_hash = pass;
    	this.img = "/images/users/profile_pic_default.png"; // UNICA FOTO HASTA LA FECHA
    	this.juegos_ids = new ArrayList<JuegoEntity>();
    	this.chats_ids = new ArrayList<ChatEntity>();
    	this.posts = new ArrayList<PostEntity>();
    	this.msgs = new ArrayList<MsgEntity>();
	}
}
