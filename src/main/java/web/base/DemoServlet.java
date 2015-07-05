package web.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DemoServlet")
public class DemoServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	private List<String> list = new ArrayList<String>();
	
	public DemoServlet(){
		list.add("111");
		list.add("222");
		list.add("333");
	}
	
	public Forward showInfo(HttpServletRequest req, HttpServletResponse resp){
		req.setAttribute("list", list);
		return new Forward("/page/base/main.jsp");
	}
	
	public void updateInfo(HttpServletRequest req, HttpServletResponse resp){
		list.add(req.getParameter("data"));
	}
	
	public void clearInfo(HttpServletRequest req, HttpServletResponse resp){
		list.clear();
	}
	public List<String> jsonInfo(HttpServletRequest req, HttpServletResponse resp){
		return list;
	}
	
	public void upload(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> map = ServletUtil.parseUpload(req);
		
		File f  = (File)map.get("f1");
		System.out.println("=====================");
		System.out.println(f.getAbsolutePath());
	}
	
	public void download(HttpServletRequest req, HttpServletResponse resp){
		ServletUtil.download(resp, new File("d:/tmp/a.txt"));
	}
}
