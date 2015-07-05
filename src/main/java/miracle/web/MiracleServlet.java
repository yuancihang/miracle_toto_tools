package miracle.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import web.base.BaseServlet;
import web.base.ServletUtil;

@WebServlet("/MiracleServlet")
public class MiracleServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String BASE_DIR = "/data/miracle_tmp";
	private static final String YML2XLS_BASE_DIR = BASE_DIR + "/yml2xls";
	private static final String RB2XLS_BASE_DIR = BASE_DIR + "/rb2xls";
	private static final String GENCNRB_BASE_DIR = BASE_DIR + "/gencnrb";
	private static final String LAST_WORDS_BASE_DIR = BASE_DIR + "/last_words";
	private static final String TOTO_DIR = "/data/git/toto";
	
	private void cleanDir(String path) throws IOException{
		Path dir = Paths.get(path);
		
		if(!Files.exists(dir)){
			Files.createDirectories(dir);
		}
		DirectoryStream<Path> ds = Files.newDirectoryStream(dir);
		for (Path entry: ds) {
			if(Files.isDirectory(entry)){
				cleanDir(entry.toFile().getAbsolutePath());
			}else{
				Files.delete(entry);
			}
	    }
	}
	
	public void yml2xls(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		int start = Integer.parseInt(req.getParameter("start"));
		int end = Integer.parseInt(req.getParameter("end"));
		
		cleanDir(YML2XLS_BASE_DIR);
		
		Path story = Paths.get(YML2XLS_BASE_DIR, "story.xls");
		
		GetYmlWords.getYmlWords(TOTO_DIR, start, end, story.toFile().getAbsolutePath());
		
		ServletUtil.download(resp, story.toFile());
	}
	
	public void rb2xls(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		cleanDir(RB2XLS_BASE_DIR);
		
        Map<String, Object> map = ServletUtil.parseUpload(req);
        
        for(Map.Entry<String, Object> entry : map.entrySet()){
        	FileItem fileItem  = (FileItem)entry.getValue();
        	if(!fileItem.getName().equals("")){
        		fileItem.write(Paths.get(RB2XLS_BASE_DIR, fileItem.getName()).toFile());
        	}
        }
		
        File xls = GetYmlWords.getMigrationWords(RB2XLS_BASE_DIR);
		
		ServletUtil.download(resp, xls);
	}
	
	public void gencnrb(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		cleanDir(GENCNRB_BASE_DIR);
		
		Path cnPath = Paths.get(GENCNRB_BASE_DIR, "cn");
		if(!Files.exists(cnPath)){
			Files.createDirectories(cnPath);
		}
		
		Map<String, Object> map = ServletUtil.parseUpload(req);
		
		for(Map.Entry<String, Object> entry : map.entrySet()){
			FileItem fileItem  = (FileItem)entry.getValue();
        	if(!fileItem.getName().equals("")){
        		fileItem.write(Paths.get(GENCNRB_BASE_DIR, fileItem.getName()).toFile());
        	}
		}
		
		File zip = GetYmlWords.generateMigrationCn(GENCNRB_BASE_DIR);
		
		ServletUtil.download(resp, zip);
	}
	
	public void exportLastWords(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		cleanDir(LAST_WORDS_BASE_DIR);
		
		
		
//		ServletUtil.download(resp, zip);
	}
	
}
