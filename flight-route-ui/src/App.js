import React from 'react';
import {Route, Routes} from 'react-router-dom';
import Footer from './layouts/Footer';
import './App.css';
import Error from "./components/Error";
import Home from "./components/Home";
import Locations from "./components/Locations";
import Transportations from "./components/Transportations";
import RouteCalculation from "./components/RouteCalculation";
import Sidebar from "./layouts/Sidebar";
import Header from "./layouts/Header";

function App() {
    return (
        <div className="d-flex">
            <Sidebar/>
            <div className="content-area">
                <Header/>
                <div className="main-content p-4" style={{marginTop: '80px'}}>
                    <Routes>
                        <Route path="/" element={<Home/>}/>
                        <Route path="/locations" element={<Locations/>}/>
                        <Route path="/transportations" element={<Transportations/>}/>
                        <Route path="/routes" element={<RouteCalculation/>}/>
                        <Route path="*" element={<Error/>}/>
                    </Routes>
                </div>
            </div>
            <Footer/>
        </div>
    );
}

export default App;
