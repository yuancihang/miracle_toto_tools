package web.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ServletUtil {

	public static void download(HttpServletResponse response, File file) {
		/* 如果文件存在 */
		if (file.exists()) {
			try {
				String filename = URLEncoder.encode(file.getName(), "UTF-8");
				response.reset();
				response.setContentType("application/x-msdownload");
				response.addHeader("Content-Disposition",
						"attachment; filename=\"" + filename + "\"");
				int fileLength = (int) file.length();
				response.setContentLength(fileLength);
				/* 如果文件长度大于0 */
				if (fileLength != 0) {
					InputStream inStream = null;
					ServletOutputStream servletOS = null;
					try{
						/* 创建输入流 */
						inStream = new FileInputStream(file);
						byte[] buf = new byte[4096];
						/* 创建输出流 */
						servletOS = response.getOutputStream();
						int readLength;
						while (((readLength = inStream.read(buf)) != -1)) {
							servletOS.write(buf, 0, readLength);
						}
						
						servletOS.flush();
					}finally{
						if(inStream!=null) inStream.close();
						if(servletOS!=null) servletOS.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Map<String, Object> parseUpload(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			
			if(isMultipart){
				// Create a factory for disk-based file items
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// Configure a repository (to ensure a secure temp location is used)
				ServletContext servletContext = request.getServletContext();
				File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
				factory.setRepository(repository);

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// Set overall request size constraint
				upload.setSizeMax(1024*1024);

				// Parse the request
				List<FileItem> items = upload.parseRequest(request);
				
				// Process the uploaded items
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
				    FileItem item = iter.next();

				    if (item.isFormField()) {
				    	String name = item.getFieldName();
				        String value = item.getString();
				        map.put(name, value);
				    } else {
				    	map.put(item.getFieldName(), item);
				    }
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
}
