# demo 增加 ProblemComponent 元件
##
1.java程式:C:\workspace\demo\src\main\java\com\example\controller\ProblemController.java
2.react程式碼位置:C:\workspace\demo\src\main\frontend\src\components\ProblemComponent\
3.關於 PROBLEM 資料表欄位
## PROBLEM 資料表欄位
| 欄位名稱 | 欄位中文名稱 | 空值(Y/N) | 欄位型別 | 備註 |
| -------- | ------------ | --------- | -------- | ---- |
| PROBLEM_ID | ID | N | int | 主鍵 |
| PROBLEM_CONTENT | 內容 | Y | varchar(255) | |
| PROBLEM_DATE | 建立時間 | Y | datetime2(6) | |
| PROBLEM_FILE | 附件 | Y | varchar(255) | |
| PROBLEM_ITEM | 類型 | Y | varchar(255) | |
##需求說明
1.於ProblemComponent用React
建立ProblemPage.jsx的主頁,放兩個元件ProblemCreate.jsx和ProblemShow.jsx
2.ProblemCreate.jsx可以新增資料,功能參考C:\workspace\demo\src\main\frontend\src\components\NoteComponent\NoteCreate.jsx
3.ProblemShow.jsx可以顯示資料,功能參考C:\workspace\demo\src\main\frontend\src\components\NoteComponent\NoteShow.jsx
4.ProblemShow.jsx顯示的資料點選後可以編輯和存檔