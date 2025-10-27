// package test.ods;

// import java.io.File;
// import java.io.IOException;
// import java.text.DecimalFormat;
// import java.text.SimpleDateFormat;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// import org.apache.commons.lang.StringUtils;
// import org.jopendocument.dom.ODValueType;
// import org.jopendocument.dom.spreadsheet.Sheet;
// import org.jopendocument.dom.spreadsheet.SpreadSheet;

// public class TestOds {

// 	public static void main(String[] args) throws IOException {
// //		String s1="_";
// //		String s2="AA_";
// //		String s3="_AA";
// //		System.out.println(s1.startsWith("_"));
// //		System.out.println(s2.endsWith("_"));
// //		System.out.println(s3.indexOf("_"));
		
		
// 		//File file = new File("C:\\Users\\lynn_liu\\Desktop\\Test\\test2.ods");
// 		File file = new File("C:\\Users\\lynn_liu\\Desktop\\批次離職檔案範本.ods");
// 		if (!file.exists()) {
// 			System.out.println("no file");
// 		}
// 		Sheet sheet;
// 		sheet = SpreadSheet.createFromFile(file).getSheet(0);
// 		Object value1 = null == sheet.getCellAt(0, 0) ? "null":sheet.getCellAt(0, 0);
// 		//System.out.println(sheet.getRowCount());
// 		int rowCount =0;
// 		for(int i=1 ; i<sheet.getRowCount(); i++) {
// 			String s = getCellValue(sheet.getCellAt(0, i).toString());
// 			//System.out.println(i +" "+s);
// 			if(!s.equals("")) {
// 				rowCount++;
// 			}else {
// 				break;
// 			}
// 		}
// 		//System.out.println(rowCount);
// 		loop1:
// 		for(int i = 1; i <= rowCount; i++) {
// 			loop2:
// //                for(int j = 0; j <= 17; j++) {
// //                	String value = getCellValue(sheet.getCellAt(j, i).toString());
// //                	System.out.println(value);
// //                	if (value.equals("")) {
// //                        continue loop1;
// //                    }
// //                }
// 				for(int j = 0; j <= 1; j++) {
// 	            	String value = getCellValue(sheet.getCellAt(j, i).toString());
// 	            	System.out.println(value);
// 	            	if (value.equals("")) {
// 	                    continue loop1;
// 	                }
// 	            }
			
// 		}
		
// 		//System.out.println(getCellValue(sheet.getCellAt(0, 1).toString()));
// 		//System.out.println(getCellValue(sheet.getCellAt(17,0).toString()).length()>0);
		         
// 	}
//     public static String getCellValue(String cellString) {
//     	int end = -1 == cellString.indexOf("</text:p>") ? 0:cellString.indexOf("</text:p>");
//     	int start = 0 == end ? 0:cellString.substring(0,end).lastIndexOf(">")+1;    	
//     	if(start>0 && end>0 && end>start) {
//     		return cellString.substring(start,end);
//     	}else {
//     		return "";
//     	}
//     }
// }
