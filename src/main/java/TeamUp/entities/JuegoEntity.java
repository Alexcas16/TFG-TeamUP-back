package TeamUp.entities;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "juegos")
public class JuegoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(unique = true)
	private String nombre;
	
	private String img;
	
	@ManyToMany(mappedBy = "juegos_ids")
	private List<UsuarioEntity> usuarios_ids;
	
	@JoinColumn(name = "foro_id")
	@MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private ForoEntity foro_id;
}