import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar(){
    return(
        <nav className="navbar">
            <h2 className="navbar-title">JournalSystem</h2>

            <div className="navbar-links">
                <Link to="/">Home</Link>
                <Link to="/patients">Patient Info</Link>
                <Link to="/auth">Login / Register</Link>
            </div>
        </nav>
    );
}

export default Navbar;