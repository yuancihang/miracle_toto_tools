package web.base.util.compress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipOutputStream extends OutputStream {

	private ZipArchiveOutputStream zos;
	
	public ZipOutputStream(String zipFile, String encoding)throws IOException{
		this(new FileOutputStream(zipFile), encoding);
	}
	
	public ZipOutputStream(OutputStream os, String encoding){
		zos = new ZipArchiveOutputStream(os);
		zos.setEncoding(encoding);
		zos.setLevel(Deflater.BEST_COMPRESSION);
	}
	
	public void write(int b) throws IOException {
		zos.write(b);
	}
	
	public void flush()throws IOException{
		zos.flush();
	}
	
	public void close()throws IOException{
		zos.close();
	}

	public ZipArchiveOutputStream getZipArchiveOutputStream(){
		return zos;
	}
}
