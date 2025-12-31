import React, { useState } from 'react';
import ProblemCreate from './ProblemCreate';
import ProblemShow from './ProblemShow';
import '../../css/Problem.css';

function ProblemPage(){
  const [refreshFlag, setRefreshFlag] = useState(false);
  const [editProblem, setEditProblem] = useState(null);
  
  const handleRefresh = () => {
    setRefreshFlag(!refreshFlag);
  };
  
  const handleEditProblem = (problem) => {
    setEditProblem(problem);
  };

  return (
    <div className="problemPage">
      <h2>問題管理</h2>
      <div className="problemContainer">
        <div className="problemCreateSection">
          <h3>新增/編輯問題</h3>
          <ProblemCreate 
            setRefreshFlag={handleRefresh} 
            editProblem={editProblem}
          />
        </div>
        <div className="problemShowSection">
          <h3>問題列表</h3>
          <ProblemShow 
            refreshFlag={refreshFlag} 
            setEditProblem={handleEditProblem}
          />
        </div>
      </div>
    </div>
  );
}

export default ProblemPage;