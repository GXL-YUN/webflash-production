// App.js
import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';
import React, { useRef, useEffect } from 'react';
import AppLayout from '../Layout/index';
import TableGlobbing from '../../../components/TableGlobbing/index';
import ProjectDashboard from  '../ProjectDashboard';
import TemTable from  '../list/TemTable';

//路由地址
const Index = () => {
    return ([
            <Route key="project-table" path="/ProjectDashboard/:id" element={<ProjectDashboard/>} />,
            <Route key="project-table" path="/project/list" element={ <AppLayout><TableGlobbing /></AppLayout>} />,
            <Route key="project-table" path="/project/demandList" element={ <AppLayout><TemTable /></AppLayout>} />
            ]
    );
};
export default Index;



