<%!
private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
private static final ThreadLocal<JspWriter> outThreadLocal = new ThreadLocal<JspWriter>();

public void my_init(HttpServletRequest request, JspWriter out){
	requestThreadLocal.set(request);
	outThreadLocal.set(out);
}
public void log(String message){
	System.out.println(message);
}
public void p(Object obj){
	System.out.println(obj);
}
public void echo(String message)throws Exception{
	outThreadLocal.get().println(message);
}
public String params(String name){
	return requestThreadLocal.get().getParameter(name);
}
public void pp()throws Exception{
	HttpServletRequest request = requestThreadLocal.get();
	java.util.Enumeration<?> e = request.getParameterNames();
	while(e.hasMoreElements()){
		String name = (String)e.nextElement();
		echo(name + " : " + request.getParameter(name) + "<br>");
	}
}
public String path(String p){
	return requestThreadLocal.get().getContextPath() + "/" + p;
}
public String base_path(){
	HttpServletRequest request = requestThreadLocal.get();
	return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
}
%>

<%
my_init(request, out);
%>