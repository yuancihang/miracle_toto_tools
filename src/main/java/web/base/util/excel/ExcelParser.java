package web.base.util.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser implements Iterator<List<String>> {
	
	public static final String XML_EXCEL_SUFFIX = "xlsx";
	
	private InputStream is = null;
	private Iterator<?> rowIterator = null;
	private Workbook book = null;

	public ExcelParser(String excelFile) throws IOException{
		this(excelFile, 0);
	}
	
	public ExcelParser(String excelFile, int sheetIndex) throws IOException{
		is = new FileInputStream(excelFile);
		if(excelFile.endsWith(XML_EXCEL_SUFFIX)){
			book = new XSSFWorkbook(is);
		}else{
			book = new HSSFWorkbook(is);
		}
		Sheet sheet = book.getSheetAt(sheetIndex);
		rowIterator = sheet.rowIterator();

	}
	
	public boolean hasNext(){
		return rowIterator.hasNext();
	}
	
	public List<String> next(){
		Row row = (Row)rowIterator.next();
		List<String> erow = new ArrayList<String>();
		Iterator<?> cellIterator = row.cellIterator();
		while(cellIterator.hasNext()){
			Cell cell = (Cell)cellIterator.next();
			erow.add(this.getCellValue(cell));
		}
		return erow;
	}
	
	public void remove(){
		throw new UnsupportedOperationException("本EXCEL解析器是只读的."); 
	}
	
	private String getCellValue(Cell cell){
		String value = null;
		//简单的查检列类型
		switch(cell.getCellType())
		{
			case Cell.CELL_TYPE_STRING://字符串
				value = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC://数字
				long dd = (long)cell.getNumericCellValue();
				value = dd+"";
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;	
			case Cell.CELL_TYPE_FORMULA:
				value = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BOOLEAN://boolean型值
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				value = String.valueOf(cell.getErrorCellValue());
				break;
			default:
				break;
		}
		return value;
	}
	
	
	public void close(){
		if(book != null){
			try {
				book.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}//close
	
}
