package TeamUp.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
	
	public static String hashPassword(String password) {
		String salt = BCrypt.gensalt();
	
	    String hashedPassword = BCrypt.hashpw(password, salt);
	    return hashedPassword;
	}

	public static boolean checkPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
}
