import { useEffect, useState } from "react";

function App(){
    const [patient, setPatient] = useState(null);

    useEffect(()=>{
        fetch("/api/patients/1")
            .then(response=>response.json())
            .then(data=>setPatient(data));
    }, []);

    return <pre>{JSON.stringify(patient, null, 2)}</pre>;
}

export default App;