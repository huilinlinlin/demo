import React, {useState, useEffect} from "react";
import { useRef } from 'react';
import '../../css/Problem.css'

function ProblemCreate({setRefreshFlag, editProblem}){
  //新增
  const [formData,setFormData] = useState({
      problemItem:'',
      problemContent:'',
      problemFile:'',
      problemDate: ''
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
    const files = fileInputRef.current.files;
    //1.檔案數2個
    if(files.length > 2){
      alert("最多兩個檔案");
      fileInputRef.current.value = '';
      return;
    }
    //2.檔案大小限制1MB
    const maxSize = 1 * 1024 * 1024;
    for (let i = 0; i < files.length; i++) {
      if (files[i].size > maxSize) {
        alert(`${files[i].name} 太大，不能超過 1MB`);
        fileInputRef.current.value = '';
        break;
      }
    }
  }  
  const handleSubmit = (e) => {
    //防止表單送出後頁面重新整理
    e.preventDefault();
    //setData
    const date = getDate();
    const newFormData = new FormData();
    const files = fileInputRef.current.files;
    newFormData.append("problemItem",formData.problemItem);
    newFormData.append("problemContent",formData.problemContent);
    newFormData.append("problemDate",date);
    Array.from(files).forEach(file => {
      newFormData.append("problemFile",file);
    }); 

    // 防止空資料送出
    if (!formData.problemItem || !formData.problemContent) {
        alert("請輸入類型與內容");
        return;
    } 
    //送出資料 
    const isNew = (problemId === undefined || problemId === '');
    fetch("http://localhost:8081/problem"+ (isNew ? '': '/'+problemId),{
      method: isNew ? "POST": "PUT",
      body: newFormData,
      }).then(res => {
          if (!res.ok) throw new Error("新增問題失敗");
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
  const [problemId, setProblemId] = React.useState('');
  useEffect(() => {
    if (editProblem) {
      setProblemId(editProblem.problemId || undefined)
      setFormData({
        problemItem: editProblem.problemItem || '',
        problemContent: editProblem.problemContent || '',
        problemFile: editProblem.problemFile || ''
      });
    }
  }, [editProblem]);
  //刪除
  const doDelete = () =>{
    let result = confirm("確定要刪除嗎?");
    if (result && problemId !== ''){
      fetch("http://localhost:8081/problem"+ '/'+ problemId,{
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
      setProblemId('');
      setFormData({ problemItem: '', problemContent: '' , problemDate: '' ,problemFile: ''});
    }else{
      setFormData({ problemItem: editProblem?.problemItem || '', problemContent: '' ,problemFile: ''});
    }
    
  } 
    return (
        <>
        <form encType="multipart/form-data">
        <input
          type="hidden"
          name="problemId"
          value={problemId}
        readOnly />  
        <input
          type="text"
          name="problemItem"
          placeholder="類型"
          value={formData.problemItem}
          onChange={handleInputChange}
          className="inputCreate"
        />
        <br/>
        <input
          type="text"
          name="problemContent"
          placeholder="內容"
          value={formData.problemContent}
          onChange={handleInputChange}
          className="inputCreate"
        />
        <label><input type="file" name="file" accept=".jpg,.jpeg,.png,.gif,.txt,.sql" multiple 
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
export default ProblemCreate;
