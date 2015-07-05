package web.base.util.compress;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPTool {

	public static byte[] compress(byte[] source) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(source);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzipos = new GZIPOutputStream(baos);
		int data = -1;
		while((data = bais.read()) != -1){
			gzipos.write(data);
		}
		gzipos.finish();
		gzipos.flush();
		gzipos.close();
		
		return baos.toByteArray();
	}
	
	public static void compressFile(String source, String dest)throws IOException{
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
		
		FileOutputStream fos = new FileOutputStream(dest);
		GZIPOutputStream gzipos = new GZIPOutputStream(fos);
		int data = -1;
		while((data = bis.read()) != -1){
			gzipos.write(data);
		}
		gzipos.finish();
		gzipos.flush();
		gzipos.close();
		fos.close();
	}
	public static void compressFile(String source)throws IOException{
		File sourceFile = new File(source);
		String dest = "";
		int index = sourceFile.getName().lastIndexOf(".");
		if(index != -1){
			File destFile = new File(sourceFile.getParentFile(), sourceFile.getName().substring(0, index) + ".gz");
			dest = destFile.getAbsolutePath();
		}else{
			dest = source + ".gz";
		}
		compressFile(source, dest);
	}
	
	public static byte[] uncompress(byte[] source) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(source);
		GZIPInputStream gzipis = new GZIPInputStream(bais);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int data = -1;
		while((data = gzipis.read()) != -1){
			baos.write(data);
		}
		gzipis.close();
		
		return baos.toByteArray();
	}
	
	public static void main(String args[])throws Exception{
		compressFile("d:/address.txt");
//		byte[] rawData = FileUtil.readBinaryFile("d:/ip.bat");
//		byte[] data = compress(rawData);
//		double r = 1d - ((double)data.length/(double)rawData.length);
//		System.out.println("压缩了：" + r*100 + "%");
		
		
	}
	
}
