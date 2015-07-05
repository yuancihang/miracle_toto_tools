/**
 * $Id$
 * Copyright(C) 2010-2016 happyelements.com. All rights reserved.
 */
package miracle.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

import web.base.util.compress.ZipTool;

/**
 * 
 * @author <a href="mailto:yongliang.zhao@happyelements.com">yongliang.zhao</a>
 * @version 1.0
 * @since 1.0
 */
public class GetYmlWords {

	static Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {
		{
			add(Character.UnicodeBlock.HIRAGANA);
			add(Character.UnicodeBlock.KATAKANA);
			add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
		}
	};

	public static int isContainJP(String str) { // write your code here

		int jpAmount = 0;

		for (char c : str.toCharArray()) {
			if (japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
				// jpAmount++;
				return 1;
			}
		}
		return jpAmount;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws Exception {
		// 根据日服yml生成excel
//		 getYmlWords("/Users/happyelements/miracle/last_toto/toto", 877, 885, "/Users/happyelements/Downloads/translate/story.xls");
		// jp migration导出excel
//		 getMigrationWords("/Users/happyelements/Downloads/translate/");
		// excel还原cn版本migration
		generateMigrationCn("/Users/happyelements/Downloads/translate/");
	}
	
	private static List<String> getFileString(File file){
		try {
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static void getYmlWords(String jpPath, int start, int end, String output)  {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Sheet1");
		int xlsLine = 0;
		for (int i = start; i <= end; i++) {
			File file = new File(
					jpPath+"/story/yml/"
							+ i + ".yml");
			if (!file.exists()) {
				continue;
			}
			List<String> lineList = getFileString(file);
			
			for (int j = 0; j < lineList.size(); j++) {
				String str = lineList.get(j);
				if (isContainJP(str) > 0) {
					String jp = str;
					if (str.contains(":")) {
						String[] temp = str.split(":");
						jp = temp[2];
						if (temp.length > 3) {
							for (int z = 3; z < temp.length; z++) {
								jp = jp + ":" + temp[z];
							}
						}
					}
					HSSFRow row = sheet.createRow(xlsLine++);
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(i);
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellValue(j + 1);
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellValue(jp);
					System.out.println(i + "\t" + (j + 1) + "\t" + jp);
				}
			}
			
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(new File(output));
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private static void generateMigrationCn() throws Exception {
	// String jpFile =
	// "/Users/xin.chen/Desktop/fy/20140917000001_update_medal_item_0917_1.rb";
	// File file = new File(jpFile);
	// List<String> lineList = FileUtil.getFileString(file);
	// Map<Integer, String> lineMap = CollectionUtil.newHashMap();
	// for (int i = 0; i < lineList.size(); i++) {
	// lineMap.put(i + 1, lineList.get(i));
	// }
	//
	// File in = new File("/Users/xin.chen/Desktop/fy/in");
	// List<String> inList = FileUtil.getFileString(in);
	// for (String inline : inList) {
	// String[] tmp = inline.split("\t");
	// int lineNum = Integer.parseInt(tmp[0]);
	// String jp = tmp[1].trim();
	// String cn = tmp[2].trim();
	// lineMap.put(lineNum, lineMap.get(lineNum).replace(jp, cn));
	// }
	// BufferedWriter bw = new BufferedWriter(new FileWriter((new File(
	// jpFile.replace(".rb", "") + "_cn.rb"))));
	// for (Entry<Integer, String> entry : lineMap.entrySet()) {
	// bw.write(entry.getValue() + "\n");
	// }
	// bw.flush();
	// bw.close();
	// }

	public static File generateMigrationCn(String dirPath) throws Exception {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			throw new Exception("请指定migration所在【文件夹】目录");
		}
		File[] files = dir.listFiles();
		File xls = null;
		Map<String, File> fileMap = new HashMap<String, File>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(".xls")) {
				if (xls != null) {
					throw new Exception("请确认只有翻译xls在此文件夹下");
				}
				xls = files[i];
				System.out.println("[INFO]Found excel:" + xls.getName());
			} else if (files[i].getName().contains(".rb")) {
				fileMap.put(files[i].getName().split("_")[0], files[i]);
			}
		}
		if (xls == null) {
			throw new Exception("请确认翻译后的xls在同文件夹下");
		}

		InputStream input = new FileInputStream(xls);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(input);
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			String sheetName = hssfSheet.getSheetName().trim();
			File file = fileMap.get(sheetName.split("_")[0].trim());
			if (file == null) {
				System.err.println("[WARN]miss file:" + sheetName);
				continue;
			}
			System.out.println("*******************************");
			System.out.println("[INFO]Loading file:" + file.getName());
			List<String> lineList = getFileString(file);
			Map<Integer, String> lineMap = new TreeMap<Integer, String>();
			for (int i = 0; i < lineList.size(); i++) {
				lineMap.put(i + 1, lineList.get(i));
				System.out.println(i + 1 + ":" + lineList.get(i));
			}

			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				HSSFCell cell0 = hssfRow.getCell(0);
				int lineNum = (int) cell0.getNumericCellValue();
				for (int cellNum = 1; cellNum < hssfRow.getLastCellNum(); cellNum += 2) {
					HSSFCell jpCell = hssfRow.getCell(cellNum);
					HSSFCell cnCell = hssfRow.getCell(cellNum + 1);
					String jp = jpCell.getStringCellValue().trim();
					String cn = cnCell.getStringCellValue().trim();
					System.out.println("Read:" + lineNum + "[" + cellNum + ","
							+ (cellNum + 1) + "]" + "\t" + jp + "\t" + cn);
					String ostr = lineMap.get(lineNum);
					String rstr = ostr.replace("'" + jp + "'", "'" + cn + "'");
					lineMap.put(lineNum, rstr);
				}
			}
			File cnDir = new File(file.getParentFile(), "cn");
			File cnFile = new File(cnDir, file.getName().replace(".rb", "")+ "_cn.rb");
			if (cnFile.exists()) {
				cnFile.delete();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(cnFile));
			for (Entry<Integer, String> entry : lineMap.entrySet()) {
				bw.write(entry.getValue() + "\n");
				System.out.println("Write:" + entry.getKey() + "\t"
						+ entry.getValue());
			}
			bw.flush();
			bw.close();
			System.out
					.println("[INFO]Finish generate file:" + cnFile.getName());
		}

		// 压缩
		File sourceFile = new File(dir, "cn");
		File targetFile = new File(dir, "cn.zip");
		ZipTool.compressFolder(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath(), "UTF-8");
		return targetFile;
	}

	public static File getMigrationWords(String dirPath) throws Exception {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			throw new Exception("请指定migration所在【文件夹】目录");
		}
		File[] files = dir.listFiles();
		
		String xlsName = "migration_"
				+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".xls";
		File fileWrite = new File(dirPath, xlsName);
		if (fileWrite.exists()) {
			fileWrite.delete();
		}
		fileWrite.createNewFile();
		HSSFWorkbook workBook = new HSSFWorkbook();
		for (int f = 0; f < files.length; f++) {
			File file = files[f];
			if (!file.getName().contains(".rb")) {
				continue;
			}
			System.out.println("****** " + file.getName() + " *******");
			HSSFSheet sheet = workBook.createSheet(file.getName());
			HSSFCellStyle style = workBook.createCellStyle();
			Font font = workBook.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			style.setFont(font);
			List<String> lineList = getFileString(file);
			int xlsLine = 0;
			for (int i = 0; i < lineList.size(); i++) {
				String str = lineList.get(i);
				if (isContainJP(str) > 0) {
					str = str.trim();
					int start = 0;
					boolean containsJp = false;
					boolean in = false;
					char[] c = str.toCharArray();
					if (!str.contains("'")) {
						continue;
					}
					HSSFRow row = sheet.createRow(xlsLine++);
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(i + 1);
					cell0.setCellStyle(style);
					for (int pos = 0; pos < c.length; pos++) {
						if ('\'' == c[pos]) {
							if (in && containsJp) {
								System.out.println((i + 1) + "\t"
										+ str.substring(start, pos));
								HSSFCell cell1 = row.createCell((int) row
										.getLastCellNum());
								cell1.setCellValue(str.substring(start, pos));
								cell1.setCellStyle(style);
								HSSFCell cell2 = row.createCell((int) row
										.getLastCellNum());
								cell2.setCellValue("");
								cell2.setCellStyle(style);
								start = 0;
								in = false;
								containsJp = false;
							} else {
								in = true;
								start = pos + 1;
								// if (japaneseUnicodeBlocks
								// .contains(Character.UnicodeBlock
								// .of(c[pos + 1]))) {
								// }
							}
						} else {
							if (in) {
								if (japaneseUnicodeBlocks
										.contains(Character.UnicodeBlock
												.of(c[pos]))) {
									containsJp = true;
								}
							}
						}
					}
				}
			}
			FileOutputStream out = new FileOutputStream(fileWrite);
			workBook.write(out);
			out.flush();
			out.close();

		}
		
		return fileWrite;
	}

}
