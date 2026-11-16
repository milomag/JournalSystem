import { useState, useEffect } from "react";
import {Link} from "react-router-dom";
import AddCondition from "../components/AddCondition.jsx";
import ListConditions from "../components/ListConditions.jsx";
import AddEncounter from "../components/AddEncounter.jsx";
import ListEncounters from "../components/ListEncounters.jsx";

function Patients({ user }){
    const [patientId, setPatientId] = useState("");
    const [patient, setPatient] = useState(null);
    const [error, setError] = useState("");
    const [showConditions, setShowConditions] = useState(false);
    const [showAddCondition, setShowAddCondition] = useState(false);
    const [showEncounters, setShowEncounters] = useState(false);
    const [showAddEncounters, setShowAddEncounters] = useState(false);

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

            setShowConditions(false);
            setShowAddCondition(false);
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

    useEffect(() => {
        setShowConditions(false);
        setShowAddCondition(false);
    }, [patientId]);

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

    async function showCon() {
        setShowConditions(true);
        setShowEncounters(false);
        setShowAddEncounters(false);
    }

    async function showAddCon() {
        setShowAddCondition(true);
    }
    async function hideAddCon() {
        setShowAddCondition(false);
    }

    async function showEnc() {
        setShowEncounters(true);
        setShowConditions(false);
        setShowAddCondition(false);
    }

    async function showAddEnc() {
        setShowAddEncounters(true);
    }
    async function hideAddEnc() {
        setShowAddEncounters(false);
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

                    {!showConditions ? (<button onClick={showCon}>Show Conditions</button>):null}
                    {!showEncounters ? (<button onClick={showEnc}>Show Encounters</button>):null}

                    {showConditions ? (
                        <div>
                            {!isPatient ? (<button onClick={showAddCon}>Add condition</button>):null}
                            {showAddCondition ? (
                                <div>
                                    <AddCondition
                                        id={patient.id}
                                        userRole={user.role}
                                    />
                                    <button onClick={hideAddCon}>close</button>
                                </div>
                            ): null}
                            <ListConditions
                                searchId={patient.id}
                                role={user.role}
                            />
                        </div>
                    ):null}

                    {showEncounters ? (
                        <div>
                            {!isPatient ? (<button onClick={showAddEnc}>Add encounters</button>):null}
                            {showAddEncounters ? (
                                <div>
                                    <AddEncounter
                                        id={patient.id}
                                        userRole={user.role}
                                    />
                                    <button onClick={hideAddEnc}>close</button>
                                </div>
                            ): null}
                            <ListEncounters
                                searchId={patient.id}
                                role={user.role}
                            />
                        </div>
                    ):null}
                </div>
            )}
        </div>
    );
}

export default Patients;