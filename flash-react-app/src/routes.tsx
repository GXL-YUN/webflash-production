// App.js
import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';

import React, { useRef, useEffect } from 'react';
import AppLayout from './components/Layout/index';
import Home from './pages/Home';
import Index from './pages/Index';
import Table from "../src/components/Table/index";
import MkReview from "../src/components/MkReview/index";
import MkFromDate from "../src/components/MkFromDate/page/ThreeLayerPage";
import DemandList from '../src/components/DemandManagement/DemandList';
import DemandDetail from '../src/components/DemandManagement/DemandDetail';
import TableGlobbing from '../src/components/TableGlobbing/index';
//路由日志
import ComponentScannerView from './route/scripts/ComponentScannerView';
import KnowledgeList from '../src/operation/Knowledge/pages/KnowledgeList/index';
import NoteCreate from '../src/operation/Knowledge/pages/NoteCreate/index';
import NoteDetail from '../src/operation/Knowledge/pages/NoteDetail/index';


//多层

import TreeApp from '../src/components/Tree/index2';

// 页面组件
const Dashboard = () => <div><Home/></div>;
//路由地址
const App = () => {
    // @ts-ignore
    return (
                <Routes>

                    <Route path="/route/View" element={
                        <AppLayout>
                            <ComponentScannerView/>
                        </AppLayout>
                    } />

                    {/* 其他路由 */}

                    <Route path="/" element={<Home />} />
                    <Route path="/data/adout" element={<AppLayout><Index /></AppLayout>} />
                    <Route path="/table/RoomTable" element={<AppLayout><TableGlobbing/></AppLayout>} />
                    <Route path="*" element={<NotFound />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/table" element={<Table />} />
                    <Route path="/mkReview" element={<MkReview />} />
                    <Route path="/mkFromDate" element={<MkFromDate />} />
                    <Route path="/demand/list" element={<DemandList />} />
                    <Route path="/demand/detail/:id" element={<DemandDetail />} />
                    <Route path="/" element={<Navigate to="/demand/list" replace />} />

                    <Route path="/Knowledge/list" element={<AppLayout><KnowledgeList/></AppLayout>} />
                    <Route path="/Knowledge/add" element={<NoteCreate />} />
                    <Route path="/Knowledge/view/1" element={<NoteDetail />} />

                    <Route path="/TreeApp/all" element={<AppLayout><TreeApp/></AppLayout>} />

                </Routes>
    );
};
const NotFound = () => (
    <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404</h1>
        <p>页面未找到</p>
    </div>
);
export default App;