import './App.css'
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from '@progress/kendo-react-buttons';
import NotePage from './components/NoteComponent/NotePage';

function App() {
  return (
    <>
    <span className={`title`}>筆記本</span>
    <span className={`title`}>測試工具</span>
    <NotePage/>
    </>
  )}

export default App
