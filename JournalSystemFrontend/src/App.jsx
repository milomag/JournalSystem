
import { useState } from "react";
import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Patients from "./pages/Patients";
import Auth from "./pages/Auth";
import Contacts from "./pages/Contacts";
import Messages from "./pages/Messages";

function App(){
    const [user, setUser] = useState(
        JSON.parse(localStorage.getItem("user"))
    );

    return(
        <BrowserRouter>
            <Navbar user={user} setUser={setUser} />

            <Routes>
                <Route path="/" element={<Home />} />

                <Route
                    path="/patients"
                    element={user ? <Patients user={user} /> : <Navigate to="/auth" replace />}
                />

                <Route
                    path="/contacts"
                    element={user ? <Contacts user={user} /> : <Navigate to="/auth" replace />}
                />

                <Route
                    path="/messages/:userId"
                    element={user ? <Messages user={user} /> : <Navigate to="/auth" replace />}
                />

                <Route path="/auth" element={<Auth setUser={setUser} />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
