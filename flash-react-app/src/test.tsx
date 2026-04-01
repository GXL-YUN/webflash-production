// App.js
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import React from 'react';

import AppLayout from './components/Layout/index';
import Index from './pages/Index';
import Table from "../src/components/Table/index";
import MkReview from "../src/components/MkReview/index";
import MkFromDate from "../src/components/MkFromDate/page/ThreeLayerPage";
import DemandList from '../src/components/DemandManagement/DemandList';
import DemandDetail from '../src/components/DemandManagement/DemandDetail';
import TableGlobbing from '../src/components/TableGlobbing/index';
// 路由日志
import ComponentScannerView from './route/scripts/ComponentScannerView';
import KnowledgeList from '../src/operation/Knowledge/pages/KnowledgeList/index';
import NoteCreate from '../src/operation/Knowledge/pages/NoteCreate/index';
import NoteDetail from '../src/operation/Knowledge/pages/NoteDetail/index';
// 多层
import TreeApp from '../src/components/Tree/index2';
import MkTable from './operation/huanchuangmk/TemTable';

// 路由配置函数
const Test = () => {
    return [


        <Route key="route-view" path="/route/View" element={
            <AppLayout>
                <ComponentScannerView />
            </AppLayout>
        } />,
        <Route key="data-about" path="/data/adout" element={ <AppLayout><Index /></AppLayout>} />,
        <Route key="room-table" path="/table/RoomTable" element={ <AppLayout><TableGlobbing /></AppLayout>} />,
        <Route key="table" path="/table" element={ <AppLayout><Table /></AppLayout>} />,
        <Route key="mk-review" path="/mkReview" element={ <AppLayout><MkReview /></AppLayout>} />,
        <Route key="mk-from-date" path="/mkFromDate" element={ <AppLayout><MkFromDate /></AppLayout>} />,
        <Route key="demand-list" path="/demand/list" element={ <AppLayout><DemandList /></AppLayout>} />,
        <Route key="demand-detail" path="/demand/detail/:id" element={ <AppLayout><DemandDetail /></AppLayout>} />,
        <Route key="knowledge-list" path="/Knowledge/list" element={ <AppLayout><KnowledgeList /></AppLayout>} />,
        <Route key="knowledge-add" path="/Knowledge/add" element={ <AppLayout><NoteCreate /></AppLayout>} />,
        <Route key="knowledge-view" path="/Knowledge/view/1" element={ <AppLayout><NoteDetail /></AppLayout>} />,
        <Route key="tree-app" path="/TreeApp/all" element={ <AppLayout><TreeApp /></AppLayout>} />,
        <Route key="test" path="/test" element={ <AppLayout><MkTable /></AppLayout>} />,

    ];
};

export default Test;