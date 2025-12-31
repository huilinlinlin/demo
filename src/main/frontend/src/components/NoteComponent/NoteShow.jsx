import React, { useEffect, useState , useRef} from 'react';

import axios from "axios";
import '../../css/Note.css'
function NoteShow({refreshFlag,setEditNote}) {
  const [notes, setNotes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [detailText, setDetailText] = useState('');
  const [detailImage, setDetailImage] = useState('');
  const textareaRef = useRef(null);
  //show
  useEffect(() => {
    fetch("http://localhost:8081/note/index")
    .then(res => res.json())
    .then(data =>setNotes(data))
    .catch(err => console.log("Fetch error:", err));
  },[refreshFlag])
  //textarea欄位隨內容調整大小
  useEffect(() => {
  if (textareaRef.current) {
    textareaRef.current.style.height = "auto"; // 先重置
    textareaRef.current.style.height = textareaRef.current.scrollHeight + "px";
  }
}, [detailText]);
  //篩選
 const filteredNotes = notes.filter(note =>
  note.noteItem.toLowerCase().includes(searchTerm.toLowerCase()) ||
  note.noteContent.toLowerCase().includes(searchTerm.toLowerCase())
 );
 
 const doEdit = (note) => {
    setEditNote(note);
    axios.get("http://localhost:8081/note/readFile/"+ note.noteId)
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
    const downloadUrl = 'http://localhost:8081/note/download.do?fileName=' + encodeURIComponent(fileName);

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
                <td width="100pt">項目名稱</td>
                <td width="500pt">內容</td>
                <td width="100pt">附件</td>
              </tr>
            </thead>
            <tbody>
              {filteredNotes.map((note) => (
                <tr key = {note.noteId}>
                  <td>{note.noteItem}</td>
                  <td><div onClick = { () => doEdit(note)}>{note.noteContent}</div></td>
                  <td>
                    { note.noteFile && ( note.noteFile.split(',').map((file,index) => (
                      <div key={index} onClick={() => doDownload(file)}>{file}
                      </div>)
                    ))}                
                  </td>
                  {/* <td>{note.noteDate == null ? "":note.noteDate.split('.')[0]}</td> */}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className={`detailSection`} >
          <pre><textarea value={detailText} ref={textareaRef}  onChange={(e) => setDetailText(e.target.value)}
                          style={{
                            width: "100%",
                            resize: "none", // 禁止手動拉大小（可選）
                            overflow: "hidden",
                          }}/></pre>
          {detailImage && <img src={detailImage} alt="preview" style={{ width: "300px" }} />}
        </div>
      </div>
    </>
  )
}

export default NoteShow;
