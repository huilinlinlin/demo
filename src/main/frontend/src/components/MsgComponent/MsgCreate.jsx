import MsgEdit from './MsgEdit'
import React, {useState} from "react";
function MsgCreate({onMessageSent}){
    const [formData,setFormData] = useState({
        msgUserid:'',
        msgContent:'',
        msgDate: ''
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
        const updatedData = {
        ...formData,
           msgDate: date
        };
        console.log(formData);
        // 防止空資料送出
        if (!formData.msgUserid || !formData.msgContent) {
            alert("請輸入帳號與留言內容");
            return;
        }
        fetch("http://localhost:8081/msg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedData)
        }).then(res => {
            if (!res.ok) throw new Error("新增留言失敗");
            return res.json();
        }).then(data => {
        // 新增成功清空表單
        console.log(data);
        setFormData({ msgUserid: '', msgContent: '' , msgDate: ''});
        if(onMessageSent) onMessageSent();// 通知父元件
      }).catch(err => console.log("Post error:", err));
      
    }
      const getDate = () => {
        const d = new Date();
        const pad = (n) => n.toString().padStart(2, '0');
        const formattedDate = `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
        return formattedDate;
      }
   const [edit, setEdit] = useState({msgid:'',type:''});
//  const doEdit = (num,action) => {
//     setEdit({msgid:num,type:action});
//  }   
    return (
        <>
        <form onSubmit={handleSubmit} >
        <input
          type="text"
          name="msgUserid"
          placeholder="帳號"
          value={formData.msgUserid}
          onChange={handleInputChange}
        />
        <br/>
        <textarea
          name="msgContent"
          placeholder="留言內容"
          value={formData.msgContent}
          onChange={handleInputChange}
        />
        <br/>
        <button type="submit">送出留言</button>
      
      {/* <input type='button' value={'修改'} onClick={ () => doEdit(msg.msgId,'E')}/>
            <input type='button' value={'刪除'} onClick={ () => doEdit(msg.msgId,'D')}/> */}
            <MsgEdit action={edit}/>
       </form> 
       </>
    );
}
export default MsgCreate;