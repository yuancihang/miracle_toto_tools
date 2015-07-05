package web.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebLogger {

	public static final String COMMON_LOGGER_NAME = "common";
	
	public static Logger getCommonLog(){
		return LogManager.getLogger(COMMON_LOGGER_NAME);
	}
	
}
