package web.base.util.compress;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipEntryOutputStream extends OutputStream {

	private ZipArchiveOutputStream zos;
	
	public ZipEntryOutputStream(String zipFile, String entryName, String encoding)throws IOException{
		this(new FileOutputStream(zipFile), entryName, encoding);
	}
	public ZipEntryOutputStream(OutputStream os, String entryName, String encoding)throws IOException{
		zos = new ZipArchiveOutputStream(os);
		zos.setEncoding(encoding);
		zos.setLevel(Deflater.BEST_COMPRESSION);
		zos.putArchiveEntry(new ZipArchiveEntry(entryName));
	}
	
	public ZipEntryOutputStream(ZipArchiveOutputStream zos, String entryName)throws IOException{
		this.zos = zos;
		zos.putArchiveEntry(new ZipArchiveEntry(entryName));
	}
	
	public ZipEntryOutputStream(ZipOutputStream zos, String entryName)throws IOException{
		this.zos = zos.getZipArchiveOutputStream();
		this.zos.putArchiveEntry(new ZipArchiveEntry(entryName));
	}
	
	public void write(int b) throws IOException {
		zos.write(b);
	}
	
	public void flush()throws IOException{
		zos.flush();
	}
	
	public void close()throws IOException{
		zos.closeArchiveEntry();
		zos.close();
	}
	
	public void closeEntry()throws IOException{
		zos.closeArchiveEntry();
	}

}
