
import MsgShow from './MsgShow'
import MsgCreate from './MsgCreate'
import { useState } from 'react';
import '../../css/Msg.css'
function MsgPage() {
  const [refreshFlag, setRefreshFlag] = useState(false);  
   // 切換 refreshFlag 來觸發 MsgShow 重新取得資料
   const handleRefesh = () => {
     setRefreshFlag(prev => !prev);
   }
  return (
    <>
    <h2>留言板</h2>
    <MsgCreate onMessageSent = {handleRefesh}/>
    <MsgShow refreshTrigger = {refreshFlag}/>
    </>
  )}

export default MsgPage;