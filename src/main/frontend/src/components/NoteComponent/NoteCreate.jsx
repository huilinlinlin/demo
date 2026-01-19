import React, {useState, useEffect,useRef} from "react";
import { AiFillPicture } from "react-icons/ai";
import { PiClipboardTextFill } from "react-icons/pi";
import '../../css/Note.css'

function NoteCreate({setRefreshFlag,editNote}){
  //新增
  const [formData,setFormData] = useState({
      noteItem:'',
      noteContent:'',
      noteDate: ''
  });
  const handleInputChange = (e) => {  
      const {name,value} = e.target;
      setFormData(prev => ({
          ...prev,
          [name]: value
      }));
  }
  //檔案限制
  const textFileInputRef = useRef(null);
  const imgFileInputRef = useRef(null);
  const handleFileChange = (e) => {
    const { name, files } = e.target;
    if (!files || files.length === 0) return;
    const file = files[0];
    const maxSize = 1 * 1024 * 1024;
    //2.檔案大小限制1MB
      if (file.size > maxSize) {
        alert(`${file.name} 太大，不能超過 1MB`);
        e.target.value = '';
        return;
      }
    //3️.允許的檔案類型（MIME type）
      const isText = name === 'noteTextFile';
      const allowedExtensions = isText
      ? ['txt', 'sql']
      : ['jpg', 'jpeg', 'png', 'gif'];
      const ext = file.name.split('.').pop().toLowerCase();
      if (!allowedExtensions.includes(ext)) {
        alert(`${file.name} 檔案類型不支援`);
        e.target.value = '';
        return;
      }
  }  
  const handleSubmit = (e) => {
    //防止表單送出後頁面重新整理
    e.preventDefault();
    //setData
    const date = getDate();
    const newFormData = new FormData();
    const textFiles = textFileInputRef.current.files;
    const imgFiles = imgFileInputRef.current.files;
    newFormData.append("noteItem",formData.noteItem);
    newFormData.append("noteContent",formData.noteContent);
    newFormData.append("noteDate",date);
    Array.from(textFiles).forEach(file => {
      newFormData.append("noteTextFile",file);
    });
    Array.from(imgFiles).forEach(file => {
      newFormData.append("noteImgFile",file);
    });  
console.log(formData)
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
        noteImgFile: editNote.noteImgFile || '',
        noteTextFile: editNote.noteTextFile || ''
      });
    }
  }, [editNote]);
  //刪除
  const doDelete = () =>{
    let result = window.confirm("確定要刪除嗎?");
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
      setFormData({ noteItem: '', noteContent: '' , noteDate: '' ,noteImgFile: '',noteTextFile: ''});
    }else{
      setFormData({ noteItem: editNote.noteItem, noteContent: '' ,noteImgFile: '',noteTextFile: ''});     
    }
    if (textFileInputRef.current) textFileInputRef.current.value = '';
    if (imgFileInputRef.current) imgFileInputRef.current.value = '';
  } 
    return (
        <>
        <form onSubmit={handleSubmit} encType="multipart/form-data">
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
        <label><PiClipboardTextFill size={24}/><input type="file" name="noteTextFile" accept=".txt,.sql" 
        onChange={handleFileChange } ref={textFileInputRef} hidden/></label>
        <label><AiFillPicture size={24}/><input type="file" name="noteImgFile" accept=".jpg,.jpeg,.png,.gif" 
        onChange={handleFileChange} ref={imgFileInputRef} hidden/></label>
        <br/>
        <input type="submit" value="SAVE"/>
        <input type="button" value="NEW" onClick={() => doclear('R') }/>
        <input type="button" value="CLEAR" onClick={ () => doclear('C')}/>
        <input type="button" value="DELETE" onClick={doDelete}/>
       </form>
       </>
    );
}
export default NoteCreate;