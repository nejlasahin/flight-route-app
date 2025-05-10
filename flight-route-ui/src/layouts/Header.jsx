import React from 'react';
import { Link } from 'react-router-dom';

function Header() {
    return (
        <div className="header-container">
            <h2>
                <Link to="/" className="text-white" style={{ textDecoration: 'none' }}>
                    Flight Route App
                </Link>
            </h2>
        </div>
    );
}

export default Header;
