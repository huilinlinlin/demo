import React, {useState, useEffect,useRef} from "react";
import '../../css/Note.css'

function NoteCreate({setRefreshFlag,editNote}){
  //新增
  const [formData,setFormData] = useState({
      noteItem:'',
      noteContent:'',
      noteFileImg:'',
      noteFileText:'',
      noteDate: ''
  });
  const handleInputChange = (e) => {  
      const {name,value} = e.target;
      setFormData(prev => ({
          ...prev,
          [name]: value
      }));
  }
  const fileInputRef = useRef(null);
  const handleFileChange = () => {
    const files = fileInputRef.current.files[0];
    const maxSize = 1 * 1024 * 1024;
    //2.檔案大小限制1MB
      if (files.size > maxSize) {
        alert(`${files.name} 太大，不能超過 1MB`);
        fileInputRef.current.value = '';
      }
    //3️.允許的檔案類型（MIME type）
      const allowedExtensions = ['jpg', 'jpeg', 'png', 'txt', 'sql'];
      const file = files;
      const ext = file.name.split('.').pop().toLowerCase();

      if (!allowedExtensions.includes(ext)) {
        alert(`${file.name} 檔案類型不支援`);
        fileInputRef.current.value = '';
      }
  }  
  const handleSubmit = (e) => {
    //防止表單送出後頁面重新整理
    e.preventDefault();
    //setData
    const date = getDate();
    const newFormData = new FormData();
    const files = fileInputRef.current.files;
    newFormData.append("noteItem",formData.noteItem);
    newFormData.append("noteContent",formData.noteContent);
    newFormData.append("noteDate",date);
    newFormData.append("noteFileText",formData.noteFileText);
    Array.from(files).forEach(file => {
      newFormData.append("noteFileImg",file);
    }); 

    // 防止空資料送出
    if (!formData.noteItem || !formData.noteContent) {
        alert("請輸入帳號與留言內容"+!formData.noteItem+!formData.noteContent);
        return;
    } 
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
        noteFileImg: editNote.noteFileImg || ''
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
      setFormData({ noteItem: '', noteContent: '' , noteDate: '' ,noteFileImg: ''});
    }else{
      setFormData({ noteItem: editNote.noteItem, noteContent: '' ,noteFileImg: ''});
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
          className="inputCreate"
        />
        <br/>
        <input
          type="text"
          name="noteContent"
          placeholder="簡述筆記內容"
          value={formData.noteContent}
          onChange={handleInputChange}
          className="inputCreate"
        />
        <label><input type="file" name="file" accept=".jpg,.jpeg,.png,.gif" 
        onChange={handleFileChange} ref={fileInputRef}/></label>
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