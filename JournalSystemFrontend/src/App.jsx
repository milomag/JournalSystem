import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Patients from "./pages/Patients";

function App(){
    return(
        <BrowserRouter>
            <Navbar />

            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/patients" element={<Patients />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;