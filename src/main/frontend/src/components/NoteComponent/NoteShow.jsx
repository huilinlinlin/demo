import React, { useEffect, useState } from 'react';
import '../../css/Note.css'
function NoteShow({refreshFlag,setEditNote}) {
  const [notes, setNotes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [detail, setDetail] = useState([]);
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
console.log(note.noteId)
   fetch("http://localhost:8081/note/readFile/"+ note.noteId,{
      method: "GET",
      headers: {
        "Content-Type": "text/plain"
      }
      }).then(res => {
          if (!res.ok) throw new Error("讀取失敗");
          return res.text();
      }).then(text  => {
        setDetail(text);
      }).catch(err => console.log("Post error:", err));
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
    <div >
      搜尋:
      <input type='text' value={searchTerm} 
        onChange={ e => setSearchTerm(e.target.value)}
        placeholder="輸入關鍵字" />
    </div>
    <hr/>
    <div className={`showSection`} >
  <div className={`listSection`} >
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
           <iframe src="http://localhost:8081/note/readFile/10" width="600" height="400"></iframe>
      </div>
    </div>
    
    </>
  )
}

export default NoteShow;
