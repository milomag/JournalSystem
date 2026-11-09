
import { useState } from "react";

function Patients(){
    const [patientId, setPatientId] = useState("");
    const [patient, setPatient] = useState(null);
    const [error, setError] = useState("");

    async function searchPatient(){
        setPatient(null);
        setError("");

        if(!patientId){
            setError("Please enter a patient ID.");
            return;
        }

        try{
            const response = await fetch(`/api/patients/${patientId}`);

            if(!response.ok){
                setError("Patient not found.");
                return;
            }

            const data = await response.json();
            setPatient(data);
        }
        catch(error){
            setError("Could not connect to the server.");
        }
    }

    return(
        <div>
            <h1>Find Patient</h1>

            <input
                type="number"
                placeholder="Enter patient ID"
                value={patientId}
                onChange={event=>setPatientId(event.target.value)}
            />

            <button onClick={searchPatient}>Search</button>

            {error && <p>{error}</p>}

            {patient && (
                <div>
                    <h2>{patient.fullName}</h2>
                    <p>ID: {patient.id}</p>
                    <p>Email: {patient.email}</p>
                    <p>Phone: {patient.phoneNumber}</p>
                    <p>Date of birth: {patient.dateOfBirth}</p>
                </div>
            )}
        </div>
    );
}

export default Patients;
