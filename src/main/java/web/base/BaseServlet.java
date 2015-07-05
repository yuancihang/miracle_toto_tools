package web.base;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Map<String, MethodHandle> methodHandleCache = new ConcurrentHashMap<String, MethodHandle>();
	
	public void init()throws ServletException{
		Lookup lookup = MethodHandles.lookup();
		
		Method[] methods = this.getClass().getDeclaredMethods();
		
		for(Method method : methods){
			Class<?>[] paramTypes = method.getParameterTypes();
			if(paramTypes == null){
				continue;
			}
			if ((paramTypes.length == 2) && (paramTypes[0].isAssignableFrom(HttpServletRequest.class))
					&& (paramTypes[1].isAssignableFrom(HttpServletResponse.class))) {
				
				try {
					MethodHandle mh = lookup.findVirtual(this.getClass(), method.getName(),
							MethodType.methodType(method.getReturnType(), HttpServletRequest.class, HttpServletResponse.class));
					methodHandleCache.put(method.getName(), mh);
				} catch (Exception e) {
					WebLogger.getCommonLog().error(e.getMessage(), e);
				}
			}
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		String operate = req.getParameter("operate");
		
		if(methodHandleCache.containsKey(operate)){
			try {
				Object result = methodHandleCache.get(operate).invoke(this, req, resp);
				if(result == null){
					writeSuccess(resp);
				}else if(result instanceof Forward){
					Forward forward = (Forward)result;
					req.getRequestDispatcher(forward.getForward()).forward(req, resp);
				}else{
					writeJSON(resp, JSON.toJSONString(result));
				}
			} catch (Throwable e) {
				WebLogger.getCommonLog().error(e.getMessage(), e);
				writeFail(resp);
			}
		}else{
			WebLogger.getCommonLog().error("illegal operate : " + operate);
		}
	}
	
	private void writeSuccess(HttpServletResponse resp){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		
		writeJSON(resp, jsonObject.toJSONString());
	}
	
	private void writeFail(HttpServletResponse resp){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);
		
		writeJSON(resp, jsonObject.toJSONString());
	}
	
	private void writeJSON(HttpServletResponse resp, String json) {
		resp.setContentType("application/json;charset=utf-8");
		
		try {
			resp.getWriter().write(json);
		} catch (IOException e) {
			WebLogger.getCommonLog().error(e.getMessage(), e);
		}
	}
}
