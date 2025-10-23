import React, { useEffect, useState } from 'react';
import axios from "axios";
import '../../css/Note.css'
function NoteShow({refreshFlag,setEditNote}) {
  const [notes, setNotes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [detail, setDetail] = useState([]);
  const [isImage, setIsImage] = useState(false);
  //show
  useEffect(() => {
    fetch("http://localhost:8081/note/index")
    .then(res => res.json())
    .then(data =>setNotes(data))
    .catch(err => console.log("Fetch error:", err));
  },[refreshFlag])
  //select
 const filteredNotes = notes.filter(note =>
  note.noteItem.toLowerCase().includes(searchTerm.toLowerCase()) ||
  note.noteContent.toLowerCase().includes(searchTerm.toLowerCase())
 );
 const doEdit = (note) =>{
   setEditNote(note);
   axios.get("http://localhost:8081/note/readFile/"+ note.noteId,{
      responseType: 'blob',//二進位檔案資料，不要自動把它當成文字或 JSON
      headers: {
        "Content-Type": "text/plain"
      }
      }).then(response => {
        const contentType = response.headers['content-type'];
        
        if(contentType.includes('text')){
          const reader = new FileReader();
          reader.onload = () => {
            const text = reader.result;
            setDetail(text);
            setIsImage(false);
          }
          reader.readAsText(response.data);
        }else if(contentType.includes('image')){
            const url = URL.createObjectURL(response.data);
            setDetail(url);
            setIsImage(true);
        }else{
          setDetail("未知檔案");
          setIsImage(false);
        }
      }).catch(err => console.log("axios error:", err));
 }
 const doDownload = (noteId) =>{
   // 組合下載 URL
    const downloadUrl = 'http://localhost:8081/note/download.do?id=' + encodeURIComponent(noteId);

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
          搜尋：<input className="inputSelect"　type='text' value={searchTerm} 
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
              <td>{note.noteFile && (
                  <div onClick={() => doDownload(note.noteId)}>
                    下載
                  </div>)}
              </td>
              {/* <td>{note.noteDate == null ? "":note.noteDate.split('.')[0]}</td> */}
            </tr>
          ))}
          </tbody>
        </table>
      </div>
      <div className={`detailSection`} >
         {isImage ? (
          <img src={detail} alt="note" style={{ maxWidth: '80%' }} />
         ):(
          <pre>{detail}</pre>
         )}
      </div>
    </div>
    
    </>
  )
}

export default NoteShow;
