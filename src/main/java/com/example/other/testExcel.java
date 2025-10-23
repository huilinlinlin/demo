/*
 * org.apache.poi
 * */
package test.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;


//import common.date.DateStringUtil;

public class testExcel {
	public static void toExcel() {
		
		File file = new File("C:\\Users\\lynn_liu\\Desktop\\hr_data_sample.ods");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		List<String[]> list=data();
		HSSFWorkbook workbook=new HSSFWorkbook();//建工作簿xls
		HSSFSheet sheet=workbook.createSheet("sheet");//建資料表
		//style
		HSSFCellStyle style=workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		//
		String titleName="TITLE";
		HSSFRow titleRow = sheet.createRow((short) 0);
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(titleName);
		CellRangeAddress cellAddr = new CellRangeAddress(0,0,0,3);
		sheet.addMergedRegion(cellAddr);
		titleCell.setCellStyle(style);
		sheet.setColumnWidth(4,50*256);
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
		HSSFCell timeStampCell = titleRow.createCell(4);
		timeStampCell.setCellValue(timeStamp);
		
		
		HSSFRow annotationRow = sheet.createRow((short) 1);
		CellRangeAddress annotationAddr = new CellRangeAddress(1, 1, 10, 12);
		sheet.addMergedRegion(annotationAddr);
		HSSFCell annotationCell = annotationRow.createCell(10);
		annotationCell.setCellValue("*代表上月出區廠商\n **上區間之前出區廠商\n");
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		annotationRow.setHeight((short)900);
		annotationCell.setCellStyle(style);
		
		Font redFont = workbook.createFont();
		redFont.setColor(HSSFColor.RED.index);
		
		HSSFRow row=sheet.createRow((short) 2);//
		HSSFCell cell=null;

		String[] columnName =list.get(0);
		for(int i=0;i<=4;i++) {
			cell=row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(style);
		}
		for(int i=1;i<list.size();i++) {
			row=sheet.createRow((short) (i + 2));
			for(int j=0;j<list.get(i).length ;j++) {
				cell = row.createCell(j);
				cell.setCellValue(list.get(i)[j]);
			    cell.setCellStyle(style);
			}	
		}
		
		//cellStyle.setWrapText(true);
		/*int cellIndex=0;
		 * row.createCell(cellIndex++).setCellValue(Int);
		 * row.createCell(cellIndex++).setCellValue(String);
		 * row.createCell(cellIndex++).setCellValue(Int);
		 * */
		String fname ="test.xls";
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/Users/lynn_liu/Desktop/"+ fname);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Map<String, Object>> getQueryReports() {
		List<Map<String, Object>> getQueryReports = new ArrayList<Map<String, Object>>();
		Map<String, Object> map= new LinkedHashMap<>();		
		map.put("BASE_NAME", "全部園區");
		getQueryReports.add(map);
		Map<String, Object> map1= new LinkedHashMap<>();		
		map1.put("TYPE", "縣市");
		map1.put("0", "新竹市");
		map1.put("1", "新竹縣");
		map1.put("2", "苗栗縣");
		map1.put("3", "桃園市");
		map1.put("4", "新北市");
		map1.put("5", "台中市");
		map1.put("6", "113");
		map1.put("7", "07");
		getQueryReports.add(map1);
		Map<String, Object> map2= new LinkedHashMap<>();		
		map2.put("TYPE", "戶籍地");
		map2.put("0", "43371");
		map2.put("1", "32450");
		map2.put("2", "21489");
		map2.put("3", "13245");
		map2.put("4", "11208");
		map2.put("5", "8939");
		map2.put("6", "34610");
		map2.put("7", "07");
		getQueryReports.add(map2);
		//{TYPE=戶籍地, 0=43371, 1=32450, 2=21489, 3=13245, 4=11208, 5=8939, 其他=34610, 合計=165312, reportYear=113, reportMonth=07}, {TYPE=戶籍地比率, 0=26.24%, 1=19.63%, 2=13.00%, 3=8.01%, 4=6.78%, 5=5.41%, 其他=20.94%, 合計=100%, reportYear=113, reportMonth=07}, {TYPE=戶籍地增減率, 0=-1.66%, 1=-2.19%, 2=-2.13%, 3=-0.12%, 4=1.87%, 5=5.59%, 其他=1.78%, 合計=-0.40%, reportYear=113, reportMonth=07}, {TYPE=通訊地, 0=53904, 1=36783, 2=21492, 3=12557, 4=9475, 5=7071, 其他=22297, 合計=163579, reportYear=113, reportMonth=07}, {TYPE=通訊地比率, 0=32.95%, 1=22.49%, 2=13.14%, 3=7.68%, 4=5.79%, 5=4.32%, 其他=13.63%, 合計=100%, reportYear=113, reportMonth=07}, {TYPE=通訊地增減率, 0=-1.92%, 1=-1.99%, 2=-2.48%, 3=-0.05%, 4=2.51%, 5=8.42%, 其他=2.07%, 合計=-0.68%, reportYear=113, reportMonth=07}]}]
		Map<String, Object> map3= new LinkedHashMap<>();		
		map3.put("TYPE", "縣市");
		map3.put("0", "新市");
		map3.put("1", "新縣");
		map3.put("2", "苗縣");
		map3.put("3", "桃市");
		map3.put("4", "新市");
		map3.put("5", "台市");
		map3.put("6", "台9市");
		map3.put("7", "113");
		map3.put("8", "07");
		getQueryReports.add(map3);
		return getQueryReports;
	}
	public static List<String[]> data(){
		List<String[]>  list= new ArrayList<>();
		String[] a= {"111","222","333","444","555"};
		String[] b= {"122","233","344","455","566"};
		
		list.add(a);
		list.add(b);
		return list;
	}
	public static void main(String[] args) {
		toExcel();
		//downloadFile(request, response, fname);
//		 List<Map<String, Object>> aa=getQueryReports();
//		 //List<String> city = new ArrayList<>();
//		 for (Map<String, Object> entity : aa) {
//			 String key=entity.keySet().iterator().next();
//			 Object value =entity.get(key);	 
//			 //System.out.println(value);
//					  
//			 for(int i=0;i<entity.size()-3;i++) {
//				 System.out.println(entity.get(Integer.toString(i)));
//				 //city.add((String)entity.get(Integer.toString(i)));
//			 } 
			 
		 }
//		 System.out.println(aa);
		 
//	}
}
