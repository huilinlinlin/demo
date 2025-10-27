// package test.file;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;

// public class testFile {
// 	public static void testFile() {
		
// 	}
// 	public static void main(String[] args) throws IOException {
// 		File file = new File("C:/temp/test");
// 		boolean status = file.exists();
// 		System.out.println(status);

// 		if (status) {
// 			System.out.println("File Exist.");
// 		} else {
// 			file.mkdirs();
// 			System.out.println("Directory Created");
// 		}
// 		File file2 = new File(file, "hello.txt");
// 		boolean status2 = file2.exists();
// 		if (status2) {
// 			System.out.println("File Exist.");
// 			System.out.println(file2.getName());
// 			System.out.println(file2.getPath());
// 			System.out.println(file2.getParent());
// 			System.out.println(file2.length());
		    
			
// 		} else {
// 			file2.createNewFile();
// 			System.out.println("Directory Created");
// 		}
// 		FileInputStream fis1=new FileInputStream("C:/temp/test/hello.txt");
// 		//FileOutputStream fos1=new FileOutputStream("C:/temp/test/Dest.txt");
// 		FileOutputStream fos1=new FileOutputStream("C:/temp/test/Dest.txt",true);
// 		int data;
// 		while((data=fis1.read())!=-1) {
// 			System.out.println((char)data);
// 			fos1.write(data);
// 		}
// 		fos1.close();
// 		fis1.close();

	
// 	}
// }
