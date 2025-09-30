import React, { useEffect, useState } from 'react';
function MsgShow() {
  const [msgs, setMsgs] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  //show
  useEffect(() => {
    fetch("http://localhost:8081/msg/index")
    .then(res => res.json())
    .then(data =>setMsgs(data))
    .catch(err => console.log("Fetch error:", err));
  },[])
  //select
 const filteredMsgs = msgs.filter(msg =>
  msg.msgUserid.toLowerCase().includes(searchTerm.toLowerCase()) ||
  msg.msgContent.toLowerCase().includes(searchTerm.toLowerCase())
 );

  return (
    <>
    <h2>訊息欄</h2>
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
          <td>序號</td>
          <td>帳號</td>
          <td>時間</td>
          <td>內容</td>
        </tr>
      </thead>
      <tbody>
      {filteredMsgs.map((msg,index) => (
        <tr key = {index}>
          <td>{msg.msgId}</td>
          <td>{msg.msgUserid}</td>
          <td>{msg.msgDate.split('.')[0]}</td>
          <td>{msg.msgContent}</td>
        </tr>
      ))}
      </tbody>
    </table>
    </>
  )
}

export default MsgShow;