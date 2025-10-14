import React, {useState, useEffect} from "react";
import '../../css/Note.css'

function NoteCreate({setRefreshFlag,editNote}){
  //新增
  const [formData,setFormData] = useState({
      noteItem:'',
      noteContent:'',
      noteFile:'',
      noteDate: ''
  });
  const handleInputChange = (e) => {  
      const {name,value} = e.target;
      setFormData(prev => ({
          ...prev,
          [name]: value
      }));
  }
  const handleSubmit = (e) => {
    //防止表單送出後頁面重新整理
    e.preventDefault();
    //setData
    const date = getDate();
    const fileInput = document.querySelector('input[type="file"][name="file"]');
    const file = fileInput.files[0];
    const newFormData = new FormData();
    newFormData.append("noteItem",formData.noteItem);
    newFormData.append("noteContent",formData.noteContent);
    if (file) {
      newFormData.append("noteFile", file);
    }
    newFormData.append("noteDate",date);

    // 防止空資料送出
    if (!formData.noteItem || !formData.noteContent) {
        alert("請輸入帳號與留言內容"+!formData.noteItem+!formData.noteContent);
        return;
    }
 console.log("newFormData"+newFormData);   
    //送出資料 
    const isNew = (noteId === undefined || noteId === '');
    fetch("http://localhost:8081/note"+ (isNew ? '': '/'+noteId),{
      method: isNew ? "POST": "PUT",
      body: newFormData,
      }).then(res => {
          if (!res.ok) throw new Error("新增留言失敗");
          return res.json();
      }).then(data => {
      // 新增成功清空表單
      console.log(data);
      doclear('R');
      if(setRefreshFlag) setRefreshFlag();// 通知父元件
      }).catch(err => console.log("Post error:", err));  
  }
  const getDate = () => {
    const d = new Date();
    const pad = (n) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  }
  //編輯
  const [noteId, setNoteid] = React.useState('');
  useEffect(() => {
    if (editNote) {
      setNoteid(editNote.noteId || undefined)
      setFormData({
        noteItem: editNote.noteItem || '',
        noteContent: editNote.noteContent || '',
        noteFile: editNote.noteFile || ''
      });
    }
  }, [editNote]);
  //刪除
  const doDelete = () =>{
    let result = confirm("確定要刪除嗎?");
    if (result && noteId !== ''){
      fetch("http://localhost:8081/note"+ '/'+ noteId,{
      method: "DELETE",
      headers: {
        "Content-Type": "application/json"
      }
      }).then(res => {
          if (!res.ok) throw new Error("刪除失敗");
          return res;
      }).then(data => {
      console.log(data);
      doclear('R');
      if(setRefreshFlag) setRefreshFlag();// 通知父元件
      }).catch(err => console.log("Post error:", err));  
    }
  }
  const doclear = (type) => {
    if(type === 'R'){
      setNoteid('');
      setFormData({ noteItem: '', noteContent: '' , noteDate: '' ,noteFile: ''});
    }else{
      setFormData({ noteItem: editNote.noteItem, noteContent: '' ,noteFile: ''});
    }
    
  } 
    return (
        <>
        <form encType="multipart/form-data">
        <input
          type="hidden"
          name="noteId"
          value={noteId}
        readOnly />  
        <input
          type="text"
          name="noteItem"
          placeholder="系統名稱"
          value={formData.noteItem}
          onChange={handleInputChange}
        />
        <br/>
        <input
          type="text"
          name="noteContent"
          placeholder="簡述筆記內容"
          value={formData.noteContent}
          onChange={handleInputChange}
        />
        <input type="file" name="file"/>
        <br/>
        <input type="button" value="SAVE" onClick={handleSubmit}/>
        <input type="button" value="NEW" onClick={() => doclear('R') }/>
        <input type="button" value="CLEAR" onClick={ () => doclear('C')}/>
        <input type="button" value="DELETE" onClick={doDelete}/>
       </form>
       </>
    );
}
export default NoteCreate;