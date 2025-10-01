
function MsgEdit({action}) {
    if (action.type === 'D'){
        alert('確定要刪除嗎?');
    }
     
    return (
    <>
   {action.msgid}{action.type}
    </>
    )

}
export default MsgEdit;