package TeamUp.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseData {
	
	private Map<String, Object> data;
	
	private Integer code;
	
	public ResponseData() {
		code = null;
		data = new HashMap<String, Object>();
	}
	
}
