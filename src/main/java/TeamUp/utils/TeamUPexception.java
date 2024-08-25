package TeamUp.utils;

public class TeamUPexception extends Exception {
	
    private static final long serialVersionUID = 1L;
	private CodigoError codigoError;
	
    public TeamUPexception(String mensaje, CodigoError codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public CodigoError getCodigoError() {
        return codigoError;
    }
    
    public enum CodigoError {
    	// REGISTRO
    	USERNAME_YA_EXISTE(1),
    	EMAIL_YA_EXISTE(2),
    	
    	// LOGIN
    	USER_NO_EXISTE(3),
    	
    	// JUEGOS
    	JUEGO_YA_EXISTE(4),
    	JUEGO_NO_EXISTE(5),

    	// TOKEN
    	TOKEN_EXPIRADO(6);
    	
        private int codigo;

        CodigoError(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }
    }
}




