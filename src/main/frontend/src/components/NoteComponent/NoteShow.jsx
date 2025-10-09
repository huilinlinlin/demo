import React, { useEffect, useState } from 'react';
import '../../css/Note.css'
function NoteShow({refreshFlag,setEditNote}) {
  const [notes, setNotes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
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
 }
 const doDownload = (noteId) =>{
   setEditNote(noteId);
 }
  return (
    <>
    <div>
      搜尋:
      <input type='text' value={searchTerm} 
        onChange={ e => setSearchTerm(e.target.value)}
        placeholder="輸入關鍵字" />
    </div>
    <hr/>
    <table border={1}>
      <thead>
        <tr>
          <td width="100pt">項目名稱</td>
          <td width="400pt">內容</td>
          <td width="400pt">附件</td>
          <td width="160pt">時間</td>
        </tr>
      </thead>
      <tbody>
      {filteredNotes.map((note) => (
        <tr key = {note.noteId}>
          <td>{note.noteItem}</td>
          <td><div onClick = { () => doEdit(note)}>{note.noteContent}</div></td>
          <td><div onclick = { () => doDownload(note.noteId)} >{note.noteFile}</div></td>
          <td>{note.noteDate == null ? "":note.noteDate.split('.')[0]}</td>
        </tr>
      ))}
      </tbody>
    </table>
    </>
  )
}

export default NoteShow;
