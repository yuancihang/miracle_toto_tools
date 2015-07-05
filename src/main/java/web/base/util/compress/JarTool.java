package web.base.util.compress;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 将java类文件打包
 * @author yuan
 *
 */
public class JarTool {
	
	private static final Integer BUFFER_SIZE = 512;
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(JarTool.class);

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
//		JarTool.compressFolder("d:\\test", "d:\\test.jar");
//		JarUtil.jarFile("d:/key.txt");
		String text = readTextEntry("d:\\test.jar", "key.txt");
		System.out.println(text);
	}
	
	/**
	 * 压缩文件夹及其子文件夹
	 * @param source String 源文件夹,如: d:/tmp
	 * @param dest String 目标文件,如: e:/tmp.jar
	 * @throws IOException
	 */
	public static void compressFolder(String source, String dest)throws IOException{
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(dest));
		jos.setLevel(Deflater.BEST_COMPRESSION);
		compressJarFolder(jos, new File(source),"");
		jos.close();
	}
	
	private static void compressJarFolder(JarOutputStream jos, File f, String base)throws IOException{
		if(f.isFile()){
			compressJarFile(jos, f, base);
		}else if(f.isDirectory()){
			compressDirEntry(jos, f, base);
			
			String[] fileList = f.list();
			for(String file:fileList){
				String newSource = f.getAbsolutePath() + File.separator + file;
				File newFile = new File(newSource);
				String newBase = base + "/" + f.getName()+"/"+newFile.getName();
				if(base.equals("")){
					newBase = newFile.getName();//f.getName()+"/"+newFile.getName();
				}else{
					newBase = base + "/" + newFile.getName();
				}
				
				logger.info("正在压缩文件从 "+newSource+"	到 "+newBase);
				compressJarFolder(jos, newFile, newBase);
				
			}//for
			
		}//if
	}
	
	//压缩单个文件
	private static void compressJarFile(JarOutputStream jos, File f, String base)throws IOException{
		jos.putNextEntry(new ZipEntry(base));
		
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		
		byte[] data = new byte[JarTool.BUFFER_SIZE];
		int len = 0;
		while ((len=bin.read(data)) != -1) {
			jos.write(data, 0, len);
		}
		bin.close();
		jos.closeEntry();
	}
	
	public static void compressFile(String sourceFile)throws IOException{
		String jarFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".jar";
		compressFile(sourceFile, jarFile);
	}
	
	//压缩单个文件到JAR文件中
	public static void compressFile(String sourceFile, String jarFile)throws IOException{
		File f = new File(sourceFile);
		String base = f.getName();
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
		jos.putNextEntry(new ZipEntry(base));
		
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		
		byte[] data = new byte[JarTool.BUFFER_SIZE];
		int len = 0;
		while ((len=bin.read(data)) != -1) {
			jos.write(data, 0, len);
		}
		bin.close();
		jos.closeEntry();
		jos.close();
	}
	
	//压缩空文件夹
	private static void compressDirEntry(JarOutputStream jos, File f, String base)throws IOException{
		jos.putNextEntry(new ZipEntry(base + "/"));
		
		jos.closeEntry();
	}
	
	public static byte[] readEntry(String jarFile, String entryName)throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean existEntry = readEntry(jarFile, entryName, baos);
		if(!existEntry){
			return null;
		}
		return baos.toByteArray();
	}
	
	public static boolean readEntry(String jarFile, String entryName, OutputStream os)throws IOException{
		JarFile jar = new JarFile(new File(jarFile));
		JarEntry entry = jar.getJarEntry(entryName);
		if(entry == null){
			return false;
		}
		InputStream is = new BufferedInputStream(jar.getInputStream(entry));
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = is.read(b, 0, b.length)) != -1) {
			os.write(b, 0, len);
		}
		is.close();
		return true;
	}
	
	public static InputStream getEntryInputStream(String jarFile, String entryName) throws IOException{
		JarFile jar = new JarFile(new File(jarFile));
		JarEntry entry = jar.getJarEntry(entryName);
		if(entry == null){
			return null;
		}
		return jar.getInputStream(entry);
	}
	
	public static InputStream getEntryInputStream(URL url) throws IOException{
		JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
		
		return jarConnection.getInputStream();
	}
	
	public static boolean existsEntry(String jarFile, String entryName)throws IOException{
		JarFile jar = new JarFile(new File(jarFile));
		JarEntry entry = jar.getJarEntry(entryName);
		if(entry == null){
			return false;
		}
		return true;
	}
	
	public static String readTextEntry(String jarFile, String entryName, String encoding)throws IOException{
		String entryContent = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean existEntry = readEntry(jarFile, entryName, baos);
		if(!existEntry){
			return null;
		}
		byte[] content = baos.toByteArray();
		baos.close();
		if(encoding == null){//按平台默认编码
			entryContent = new String(content);
		}else{
			entryContent = new String(content, encoding);
		}
		return entryContent;
	}
	
	public static String readTextEntry(String jarFile, String entryName)throws IOException{
		return readTextEntry(jarFile, entryName, null);
	}

	public static void uncompress(String jarFile)throws IOException{
		File f = new File(jarFile);
		String path = f.getAbsolutePath();
		int index = path.lastIndexOf(".");
		if(index > 0){
			path = path.substring(0, index);
		}
		uncompress(jarFile, path);
	}
	public static void uncompress(String jarFile, String outputDir)throws IOException{
		if(!new File(outputDir).exists()){
			new File(outputDir).mkdirs();
		}
		JarFile jar = new JarFile(new File(jarFile));
		Enumeration<JarEntry> e = jar.entries();
		while(e.hasMoreElements()){
			JarEntry entry = e.nextElement();
			saveEntry(jar, entry, outputDir);
		}

	}
	private static void saveEntry(JarFile jar, JarEntry entry, String outputDir)throws IOException{
		logger.info("解压缩" + entry.getName() + " ... ... ");
		File f = new File(new File(outputDir), entry.getName());
		if(entry.isDirectory()){
			f.mkdirs();
			return ;
		}else{
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		InputStream is = new BufferedInputStream(jar.getInputStream(entry));
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = is.read(b, 0, b.length)) != -1) {
			fos.write(b, 0, len);
		}
		is.close();
		fos.close();
	}
}
