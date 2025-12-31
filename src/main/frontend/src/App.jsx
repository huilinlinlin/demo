import './App.css'
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from '@progress/kendo-react-buttons';
import { useState } from 'react';
import NotePage from './components/NoteComponent/NotePage';
import ProblemPage from './components/ProblemComponent/ProblemPage';
function App() {
  const [currentPage, setCurrentPage] = useState('home');

  const handleNotebookClick = () => {
    setCurrentPage('notebook');
  };

  const handleBackToHome = () => {
    setCurrentPage('home');
  };

  const handleDbToolClick = () => {
    setCurrentPage('problem');
  };

  return (
    <>
      {currentPage === 'home' && (
        <>
          <span 
            className={`title clickable`} 
            onClick={handleNotebookClick}
            style={{ cursor: 'pointer', color: '#007bff', textDecoration: 'underline' }}
          >
            筆記本
          </span>
          <span className={`title clickable`}
            onClick={handleDbToolClick}
            style={{ cursor: 'pointer', color: '#007bff', textDecoration: 'underline' }}
          >
            測試工具
          </span>
        </>
      )}
      
      {currentPage === 'notebook' && (
        <>
          <div style={{ marginBottom: '10px' }}>
            <button 
              onClick={handleBackToHome}
              style={{ 
                padding: '5px 10px', 
                backgroundColor: '#007bff', 
                color: 'white', 
                border: 'none', 
                borderRadius: '4px',
                cursor: 'pointer'
              }}
            >
              ← 返回首頁
            </button>
          </div>
          <NotePage/>
        </>
      )}

      { currentPage === 'problem'&& (
        <>
          <div style={{ marginBottom: '10px' }}>
            <button 
              onClick={handleBackToHome}
              style={{ 
                padding: '5px 10px', 
                backgroundColor: '#007bff', 
                color: 'white', 
                border: 'none', 
                borderRadius: '4px',
                cursor: 'pointer'
              }}
            >
              ← 返回首頁
            </button>
          </div>
          <ProblemPage/>
        </>
      )}
    </>
  )
}

export default App
