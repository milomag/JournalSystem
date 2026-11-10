import { useState, useEffect } from "react";

function Patients({ user }){
    const [patientId, setPatientId] = useState("");
    const [patient, setPatient] = useState(null);
    const [error, setError] = useState("");

    const isPatient = user.role === "PATIENT";
    const isStaff = user.role === "STAFF";
    const isDoctor = user.role === "DOCTOR";

    async function fetchPatient(url){
        setPatient(null);
        setError("");

        try{
            const response = await fetch(url);

            if(!response.ok){
                setError(response.status === 404
                    ? "Patient not found."
                    : "Could not retrieve patient information.");
                return;
            }

            const data = await response.json();
            setPatient(data);
        }
        catch(error){
            setError("Could not connect to the server.");
        }
    }

    useEffect(()=>{
        if(isPatient){
            fetchPatient(`/api/patients/user/${user.id}`);
        }
    }, [isPatient, user.id]);

    async function searchPatient(){
        if(!patientId){
            setError("Please enter a patient ID.");
            setPatient(null);
            return;
        }

        fetchPatient(`/api/patients/${patientId}`);
    }

    return(
        <div>
            <h1>{isPatient ? "My Patient Information" : "Find Patient"}</h1>

            {(isStaff || isDoctor) && (
                <div>
                    <input
                        type="number"
                        placeholder="Enter patient ID"
                        value={patientId}
                        onChange={event=>setPatientId(event.target.value)}
                    />

                    <button onClick={searchPatient}>Search</button>
                </div>
            )}

            {error && <p>{error}</p>}

            {patient && (
                <div>
                    <h2>{patient.fullName}</h2>

                    {isDoctor || isPatient ? (
                        <>
                            <p>ID: {patient.id}</p>
                            <p>Email: {patient.email}</p>
                            <p>Phone: {patient.phoneNumber}</p>
                            <p>Date of birth: {patient.dateOfBirth}</p>
                        </>
                    ) : null}
                </div>
            )}
        </div>
    );
}

export default Patients;