import { useState, useEffect } from "react";

function Patients({ user }){
    const [patientId, setPatientId] = useState("");
    const [patient, setPatient] = useState(null);
    const [error, setError] = useState("");

    const isPatient = user.role === "PATIENT";
    const isStaff = user.role === "STAFF";
    const isDoctor = user.role === "DOCTOR";

    async function fetchPatient(url){
        try{
            const token = localStorage.getItem("token");

            const response = await fetch(url, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if(!response.ok){
                setPatient(null);
                setError(response.status === 404
                    ? "Patient not found."
                    : "Could not retrieve patient information.");
                return;
            }

            const data = await response.json();
            setPatient(data);
            setError("");
        }
        catch(error){
            setPatient(null);
            setError("Could not connect to the server.");
        }
    }

    useEffect(()=>{
        if(!isPatient) return;

        let ignore = false;

        async function loadPatient(){
            try{
                const token = localStorage.getItem("token");

                const response = await fetch("/api/patients/me", {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });

                if(!response.ok){
                    if(ignore) return;

                    setPatient(null);
                    setError("Could not retrieve patient information.");
                    return;
                }

                const data = await response.json();

                if(ignore) return;

                setPatient(data);
                setError("");
            }
            catch(error){
                if(ignore) return;

                setPatient(null);
                setError("Could not connect to the server.");
            }
        }

        loadPatient();

        return ()=>{
            ignore = true;
        };
    }, [isPatient]);

    async function searchPatient(){
        if(!patientId){
            setError("Please enter a patient ID.");
            setPatient(null);
            return;
        }

        const url = isStaff
            ? `/api/patients/staff/${patientId}`
            : `/api/patients/${patientId}`;

        fetchPatient(url);
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