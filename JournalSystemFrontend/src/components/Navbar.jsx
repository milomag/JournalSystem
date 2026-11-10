import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

function Navbar({ user, setUser }){
    const navigate = useNavigate();

    function handleLogout(){
        localStorage.removeItem("user");
        setUser(null);
        navigate("/");
    }

    return(
        <nav className="navbar">
            <h2 className="navbar-title">JournalSystem</h2>

            <div className="navbar-links">
                <Link to="/">Home</Link>
                <Link to="/patients">Patient Info</Link>

                {user ? (
                    <button onClick={handleLogout}>Logout</button>
                ) : (
                    <Link to="/auth">Login / Register</Link>
                )}
            </div>
        </nav>
    );
}

export default Navbar;