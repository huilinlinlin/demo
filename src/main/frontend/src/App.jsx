import { useEffect, useState } from 'react';
import './App.css'
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from '@progress/kendo-react-buttons';

function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetch('http://localhost:8081/api/hello')
      .then(res => res.text())
      .then(data => setMessage(data));
  }, []);


  return (
    <>
    <div>
      <h1>{message || 'Loading...'}</h1>
    </div>
      <div>
      <Button primary={true}>Hello KendoReact + Vite</Button>
    </div>
    </>
  )}

export default App
