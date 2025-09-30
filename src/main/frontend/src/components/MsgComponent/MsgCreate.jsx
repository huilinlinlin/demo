import React, {useEffect,useState} from "react";
function MsgCreate(){
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
        // 清空表單
        setFormData({ msgUserid: '', msgContent: '' , msgDate: ''});
      }).catch(err => console.log("Post error:", err));
      function getDate(){
        const d = new Date();
        const pad = (n) => n.toString().padStart(2, '0');
        const formattedDate = `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
        alert(formattedDate);
        return formattedDate;
      }
    }

    return (
        <>
        <form onSubmit={handleSubmit} style={{ marginTop: '20px', marginBottom: '20px' }}>
        <input
          type="text"
          name="msgUserid"
          placeholder="帳號"
          value={formData.msgUserid}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="msgContent"
          placeholder="留言內容"
          value={formData.msgContent}
          onChange={handleInputChange}
        />
        <button type="submit">送出留言</button>
      </form>
        </>
    );
}
export default MsgCreate;