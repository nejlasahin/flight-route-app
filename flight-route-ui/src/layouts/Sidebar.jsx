import React from 'react';
import { Link } from 'react-router-dom';

function Sidebar() {
    return (
        <div className="sidebar-container">
            <ul className="nav flex-column mb-auto mt-3">
                <li className="nav-item">
                    <Link to="/locations" className="nav-link text-dark">Locations</Link>
                </li>
                <li>
                    <Link to="/transportations" className="nav-link text-dark">Transportations</Link>
                </li>
                <li>
                    <Link to="/routes" className="nav-link text-dark">Routes</Link>
                </li>
            </ul>
        </div>
    );
}

export default Sidebar;
