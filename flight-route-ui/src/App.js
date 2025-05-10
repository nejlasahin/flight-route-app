import React from 'react';
import Footer from './layouts/Footer';
import Nav from './layouts/Nav';
import Error from "./components/Error";
import Home from "./components/Home";
import {Route, Routes} from 'react-router-dom';
import Locations from "./components/Locations";
import Transportations from "./components/Transportations";
import RouteCalculation from "./components/RouteCalculation";

function App() {
    return (
        <>
            <Nav/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/locations" element={<Locations/>}/>
                <Route path="/transportations" element={<Transportations/>}/>
                <Route path="/routes" element={<RouteCalculation/>}/>
                <Route path="*" element={<Error/>}/>
            </Routes>
            <Footer/>
        </>
    );
}

export default App;