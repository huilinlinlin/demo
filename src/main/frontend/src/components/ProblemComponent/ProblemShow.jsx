import React, { useEffect, useState } from 'react';
import axios from "axios";
import '../../css/Problem.css'
function ProblemShow({refreshFlag,setEditProblem}) {
  const [problems, setProblems] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [detailText, setDetailText] = useState('');
  const [detailImage, setDetailImage] = useState('');
  //show
  useEffect(() => {
    fetch("http://localhost:8081/problem/index")
    .then(res => res.json())
    .then(data =>setProblems(data))
    .catch(err => console.log("Fetch error:", err));
  },[refreshFlag])
  //篩選
 const filteredProblems = problems.filter(problem =>
  problem.problemItem.toLowerCase().includes(searchTerm.toLowerCase()) ||
  problem.problemContent.toLowerCase().includes(searchTerm.toLowerCase())
 );
 
 const doEdit = (problem) => {
    setEditProblem(problem);
    axios.get("http://localhost:8081/problem/readFile/"+ problem.problemId)
    .then(res => {
      console.log(res)
          const textFile = res.data.textFile;
          const imageFile = res.data.imageFile;
          
          // 處理文字檔
          if (textFile?.content) {          
            const binaryString = atob(textFile.content);// 1. atob 轉成 Latin1 字串        
            const byteArray = Uint8Array.from(binaryString, c => c.charCodeAt(0)); // 2. 轉成 byte array          
            const decodedText = new TextDecoder("utf-8").decode(byteArray);// 3. 使用 TextDecoder 解碼成 UTF-8
            setDetailText(decodedText);
          }else {
            setDetailText('');
          }

          // 處理圖片
          if (imageFile?.content) {
            const img = `data:${imageFile.type};base64,${imageFile.content}`;
            setDetailImage(img);
          }else {
            setDetailImage('');
          }
        }).catch(err => console.error("讀取附件失敗:", err));
};
 const doDownload = (fileName) =>{
   // 組合下載 URL
    const downloadUrl = 'http://localhost:8081/problem/download.do?fileName=' + encodeURIComponent(fileName);

    // 建立一個隱藏的 <a> 標籤並觸發 click
    const a = document.createElement('a');
    a.href = downloadUrl;
    a.download = '';  // 這會提示瀏覽器進行下載（需要後端有正確 Content-Disposition）
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }
  return (
    <>
    
    <div className={`showSection`} >
  <div className={`listSection`} >
        <div >
          搜尋：<input className="inputSelect" type='text' value={searchTerm} 
            onChange={ e => setSearchTerm(e.target.value)}
            placeholder="輸入關鍵字" />
        </div>
        <table border={1}>
          <thead>
            <tr>
              <td width="100pt">ID</td>
              <td width="100pt">類型</td>
              <td width="500pt">內容</td>
              <td width="100pt">附件</td>
            </tr>
          </thead>
          <tbody>
          {filteredProblems.map((problem) => (
            <tr key = {problem.problemId}>
              <td>{problem.problemId}</td>
              <td>{problem.problemItem}</td>
              <td><div onClick = { () => doEdit(problem)}>{problem.problemContent}</div></td>
              <td>
                 { problem.problemFile && ( problem.problemFile.split(',').map((file,index) => (
                  <div key={index} onClick={() => doDownload(file)}>{file}
                  </div>)
                 ))}                
              </td>
            </tr>
          ))}
          </tbody>
        </table>
      </div>
      <div className={`detailSection`} >
      <pre>{detailText}</pre>
      {detailImage && <img src={detailImage} alt="preview" style={{ width: "300px" }} />}
      </div>
    </div>
    
    </>
  )
}

export default ProblemShow;