import {Link} from 'react-router-dom';

function Nav() {

    return (
        <>
            <div>
                <header
                    className="container d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
                    <Link to="/"
                          className="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none fw-bold fs-3">
                        Flight Route App
                    </Link>


                    <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0 fs-5">
                        <li><Link to="/locations" className="nav-link px-2 link-dark">Locations</Link></li>
                        <li><Link to="/transportations" className="nav-link px-2 link-dark">Transportations</Link></li>
                        <li><Link to="/routes" className="nav-link px-2 link-dark">Routes</Link></li>
                    </ul>

                </header>
            </div>
        </>
    )
}

export default Nav;