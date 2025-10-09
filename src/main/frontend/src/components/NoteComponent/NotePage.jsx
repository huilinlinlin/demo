
import NoteShow from './NoteShow'
import NoteCreate from './NoteCreate'
import { useState } from 'react';
import '../../css/Note.css'
function NotePage() {
  // 新增資料：切換 refreshFlag 來觸發 NoteShow 重新取得資料
  const [refreshFlag, setRefreshFlag] = useState(false);  
  //修改資料：傳遞要修改的資料
  const [editNote, setEditNote] = useState(''); 
  //資料顯示
  const handleRefesh = () => {
    setRefreshFlag(prev => !prev);
  }
  return (
    <>
    <h2>筆記本</h2>
    <NoteCreate setRefreshFlag = {handleRefesh} editNote = {editNote}/>
    <NoteShow refreshFlag = {refreshFlag} setEditNote = {setEditNote}/>
    </>
  )}

export default NotePage;