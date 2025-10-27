// package test.sql;

// import java.io.BufferedReader;
// import java.io.Console;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import common.Encryptor;
// import common.EncryptorProvide;

// public class InsertData {

// 	public static void main(String[] args) throws IOException {
// 		InsertData insertData = new InsertData();
// //必填欄位區1.
// 		String filename = "01";// txt檔名
// 		String dbName = "NSC_B12_A.dbo.VW_COMPANY_TURNOVER";// 完整資料表名稱
	
// 		List<String> data = insertData.inputData(filename); // 輸入資料
// 		insertData.outputData(data, filename, dbName);// 輸出資料

// 	}
// //必填欄位區2.條件
// 	public Map<String,String[]> getCondition() {
// 		Map<String,String[]> conditionMap = new HashMap<String, String[]>();	
// 		String[] zeroCol= { "" };//列出需要補0的欄位 PARK_ID BASE_ID
// 		String[] identity = { "" };// 扣除識別欄位ROW_ID
// 		String[] encryptCol = { "UNAME","EMAIL","AA" };//加密欄位F_ID	EMAIL	USER_NAME
// 		String[] encryptKey = { "D067FEBF453EEEAE7547E78ABF8CFCCF5C72F51A855DCE7C9BB9F69BB64173B4" };//密鑰
// 		String[] timeCol = { "CREATE_DATE","UPLOAD_MONTH","RENT_STARDATE","RENT_ENDDATE" };// 扣除識別欄位ROW_ID
// 		conditionMap.put("zeroCol", zeroCol);
// 		conditionMap.put("identity", identity);
// 		conditionMap.put("encryptCol", encryptCol);
// 		conditionMap.put("encryptKey", encryptKey);
// 		conditionMap.put("timeCol", timeCol);
// 		return conditionMap;
// 	}
// 	// 輸出資料
// 	public void outputData(List<String> data, String filename, String dbName) throws IOException {
// 		List<String> data_list = data;
// 		String str = "INSERT " + dbName + " ";
// 		for (int i = 0; i < data_list.size(); i++) {
// 			str = str + "(" + data_list.get(i).toString() + ")";
// 			if (i == 0) {
// 				str = str + " VALUES \n";
// 			} else {
// 				str = str + "\n";
// 				if (i < (data_list.size() - 1)) {
// 					str = str + ",";
// 				}
// 			}
// 			System.out.println(str);

// 		}
// 		String userDirectoryPath = System.getProperty("user.dir") + "\\" + "insert" + "\\";
// 		FileWriter fw = new FileWriter(userDirectoryPath + "output_" + filename + ".sql");
// 		fw.write(str);
// 		fw.flush();
// 		fw.close();
// 	}

// 	// 輸入資料
// 	public List<String> inputData(String filename) throws IOException {
// 		String userDirectoryPath = System.getProperty("user.dir") + "\\" + "insert" + "\\";
// 		FileReader fr = new FileReader(userDirectoryPath + filename + ".txt");
// 		BufferedReader br = new BufferedReader(fr);
// 		List<String> list = new ArrayList<String>();
// 		String input_col = null;
// 		String input_data = null;
// 		int count = 0;// 辨識第一行為欄位
// 		while (br.ready()) {		
// 			String str = br.readLine().replace("	", ",");
// 			if (count == 0) {
// 				input_col = str;
// 				list.add(input_col);
// 			} else {
// 				input_data = dataProcess(str, input_col);// 處理資料
// 				list.add(input_data);
// 			}
// 			count++;			
// 		}
// 		br.close();
// 		fr.close();		
// 		return list;
// 	}

// 	public String dataProcess(String data, String input_col) {
// 		Encryptor encryptor = new Encryptor();
// 		String result = "";
// 		String[] dataArr = data.split(",");
// 		String[] col = input_col.split(",");
// 		Map<String,String[]> conditionMap = getCondition();
		
// 		for (int i = 0; i < col.length; i++) {			
// 			//列出需要補0的欄位 PARK_ID BASE_ID
// 			for (String col_zero : conditionMap.get("zeroCol")) {
// 				if (col[i].equals(col_zero) && dataArr[i].length() < 2) {
// 					dataArr[i] = "0" + dataArr[i];
// 				}
// 			}	
// 			//加密欄位out
// 			for(String col_enc : conditionMap.get("encryptCol")) {	
// 				if (col[i].equals(col_enc)) {
// 					dataArr[i] = encryptor.decrypt(dataArr[i],conditionMap.get("encryptKey")[0]);
// 				}
// 			}
// 			//時間欄位
// 			for(String col_time : conditionMap.get("timeCol")) {	
// 				if (col[i].equals(col_time)) {
// 				System.out.println(dataArr[i]);	
// 				if(!dataArr[i].equals("NU")) {
// 					long timeStr = Long.parseLong(dataArr[i]);
// 				System.out.println(timeStr);	
// 					Date date = new Date(timeStr);
// 					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// 					dataArr[i] = sdf.format(date);
// 				}
					
// 				}
// 			}
// 			// 扣除識別欄位
// 			for (String iden : conditionMap.get("identity")) {
// 				if (!iden.equals(col[i])) {
// 					if (!dataArr[i].equals("null")) {
// 						dataArr[i] = "'" + dataArr[i] + "'";
// 					}
// 					if (i != (dataArr.length - 1)) {
// 						dataArr[i] = dataArr[i] + ",";
// 					}
// 				}
// 			}
// 			//System.out.println(dataArr[i]);
// 			result = result+dataArr[i];
// 			}
// 		return result;
// 	}
// }
