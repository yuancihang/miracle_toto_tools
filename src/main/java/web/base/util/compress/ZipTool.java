package web.base.util.compress;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipTool {
	
	private static final Logger logger = LogManager.getLogger(ZipTool.class);
	
	private static final Integer BUFFER_SIZE = 512;
	
	public static void main(String args[])throws IOException{
//		compressFolder("d:\\test", "d:\\test.zip", "GBK");
//		System.out.println(readTextEntry("d:/test/openmas1.zip", "openmas20100203131843.sql"));
		compressFile("d:/test/openmas20100203131843.sql", "UTF-8");
	}
	
	/**
	 * 压缩文件夹及其子文件夹
	 * @param source String 源文件夹,如: d:/tmp
	 * @param dest String 目标文件,如: e:/tmp.jar
	 * @throws IOException
	 */
	public static void compressFolder(String source, String dest, String encoding)throws IOException{
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(dest));
		zos.setEncoding(encoding);
		zos.setLevel(Deflater.BEST_COMPRESSION);
		compressZipFolder(zos, new File(source),"");
		zos.close();
	}
	
	private static void compressZipFolder(ArchiveOutputStream aos, File f, String base)throws IOException{
		if(f.isFile()){
			compressFile(aos, f, new ZipArchiveEntry(base));
		}
		if(f.isDirectory()){
			compressDirEntry(aos, new ZipArchiveEntry(base + "/"));
			//压缩空文件夹
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
				compressZipFolder(aos, newFile, newBase);
				
			}//for
			
		}//if
	}
	
	//压缩单个文件
	private static void compressFile(ArchiveOutputStream aos, File f, ArchiveEntry archiveEntry)throws IOException{
		aos.putArchiveEntry(archiveEntry);
		
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		
		byte[] data = new byte[BUFFER_SIZE];
		while ((bin.read(data)) != -1) {
			aos.write(data);
		}
		bin.close();
		aos.closeArchiveEntry();
	}
	private static void compressDirEntry(ArchiveOutputStream aos, ArchiveEntry archiveEntry)throws IOException{
		aos.putArchiveEntry(archiveEntry);
		aos.closeArchiveEntry();
	}
	
	public static void compressFile(String sourceFile, String encoding)throws IOException{
		String zipFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".zip";
		compressFile(sourceFile, encoding, zipFile);
	}
	
	//压缩单个文件到ZIP文件中
	public static void compressFile(String sourceFile, String encoding, String zipFile)throws IOException{
		File f = new File(sourceFile);
		String base = f.getName();
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(zipFile));
		zos.setEncoding(encoding);
		zos.setLevel(Deflater.BEST_COMPRESSION);
		zos.putArchiveEntry(new ZipArchiveEntry(base));
		
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		
		byte[] data = new byte[BUFFER_SIZE];
		while ((bin.read(data)) != -1) {
			zos.write(data);
		}
		bin.close();
		zos.closeArchiveEntry();
		zos.close();
	}
	
	public static InputStream readEntry(String zipFileName, String entryName)throws IOException{
		return readEntry(zipFileName, entryName, "UTF-8");
	}
	public static InputStream readEntry(String zipFileName, String entryName, String encoding)throws IOException{
		ZipFile zipFile = new ZipFile(zipFileName, encoding);
		return zipFile.getInputStream(zipFile.getEntry(entryName));
	}
	public static String readTextEntry(String zipFileName, String entryName)throws IOException{
		return readTextEntry(zipFileName, entryName, "UTF-8");
	}
	public static String readTextEntry(String zipFileName, String entryName, String encoding)throws IOException{
		String entryContent = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = readEntry(zipFileName, entryName, encoding);
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = is.read(b, 0, b.length)) != -1) {
			baos.write(b, 0, len);
		}
		is.close();
		byte[] content = baos.toByteArray();
		baos.close();
		if(encoding == null){//按平台默认编码
			entryContent = new String(content);
		}else{
			entryContent = new String(content, encoding);
		}
		return entryContent;
	}
}
