import React, { useEffect, useState } from 'react';
import '../../css/Msg.css'
function MsgShow({refreshTrigger}) {
  const [msgs, setMsgs] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  //show
  useEffect(() => {
    fetch("http://localhost:8081/msg/index")
    .then(res => res.json())
    .then(data =>setMsgs(data))
    .catch(err => console.log("Fetch error:", err));
  },[refreshTrigger])
  //select
 const filteredMsgs = msgs.filter(msg =>
  msg.msgUserid.toLowerCase().includes(searchTerm.toLowerCase()) ||
  msg.msgContent.toLowerCase().includes(searchTerm.toLowerCase())
 );
 const doEdit = () =>{
   alert('do');
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
          <td>帳號</td>
          <td>內容</td>
          <td>時間</td>
        </tr>
      </thead>
      <tbody>
      {filteredMsgs.map((msg) => (
        <tr key = {msg.msgId}>
          <td>{msg.msgUserid}</td>
          <td><div onClick={doEdit}>{msg.msgContent}</div></td>
          <td>{msg.msgDate.split('.')[0]}</td>
        </tr>
      ))}
      </tbody>
    </table>
    </>
  )
}

export default MsgShow;
