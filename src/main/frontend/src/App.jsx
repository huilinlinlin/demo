import MsgShow from './components/MsgComponent/MsgShow'
import MsgCreate from './components/MsgComponent/MsgCreate'
import './App.css'
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from '@progress/kendo-react-buttons';

function App() {
  return (
    <>
    <MsgCreate/>
    <MsgShow/>
    </>
  )}

export default App
