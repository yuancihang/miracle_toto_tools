package web.base.util;

import java.io.IOException;

import org.msgpack.MessagePack;

/**
 * 
 *<pre>
 *  用法： 
 *  @Message // Annotation
	public class MyMessage {
		public String name;
	    public double version;
	    public List<String> list = new ArrayList<String>();
	    public Map<String, String> map = new HashMap<String, String>();
	    public List<Message2> list2 = new ArrayList<Message2>();
	    public Map<String, Message2> map2 = new HashMap<String, Message2>();
	    public Message2 message;
	    @Optional
	    public int age = 0;
	}
	@Message
	public class Message2 {
		public Message2(){
		}
		public Message2(String name){
			this.name = name;
		}
		public String name;
	}
 * </pre>
 * @author Administrator
 *
 */
public class MessagePackUtil {

	private static final MessagePack msgpack = new MessagePack();
	
	static{
		System.setProperty("msgpack.dynamic-codegen.enabled", "false");
	}
	
	public static byte[] serial(Object obj) throws IOException{
		return msgpack.write(obj);
	}
	
	public static <T> T unserial(byte[] data, Class<T> clazz) throws IOException{
		return msgpack.read(data, clazz);
	}
}
