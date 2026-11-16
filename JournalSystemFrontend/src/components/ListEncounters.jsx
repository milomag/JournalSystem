import { useEffect, useState } from "react";

function ListEncounters(user) {

    const [patientId, setPatientId] = useState(user.searchId);
    const [encounters, setEncounters] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const isPatient = user.role === "PATIENT";
    const isStaff = user.role === "STAFF";

    async function fetchEncounters(url) {
        setLoading(true);
        setError("");

        try {
            const token = localStorage.getItem("token");

            const response = await fetch(url, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if(!response.ok){
                throw new Error(`Could not load encounters (${response.status})`);
            }

            const data = await response.json();
            if (data == null){
                setEncounters([]);
            } else {
                setEncounters(data);
            }

        }catch(error){
            setEncounters([]);
            setError(error.message);
        }finally{
            setLoading(false);
        }

    }

    async function searchEncounters(){
        if (!patientId) {
            setError("Undefined patient ID.");
            setEncounters([]);
            return;
        }
        let url;
        if (isPatient) {
            url = `/api/encounter/me`;
        } else if (isStaff){
            url = `/api/encounter/staff/${patientId}`
        } else {
            url = `/api/encounter/${patientId}`
        }

        await fetchEncounters(url);
    }

    useEffect(() => {
        let ignore = false;

        if (!ignore)searchEncounters();
        return ()=>{
            ignore = true;
        };
    },[patientId]);

    return (
        <div>
            <h2>Encounters</h2>
            <button onClick={searchEncounters}>
                Refresh
            </button>

            {error && <p>{error}</p>}

            <div>
                {encounters.length === 0 ? (
                    <p>No encounters to show</p>
                ) : (
                    <div>
                        {encounters.map(encounter => {
                            return (
                                <div>
                                    <p>{encounter.practitionerEmail} : {encounter.time} : {encounter.location}</p>
                                    {encounter.observations.map(observation => {
                                        return (
                                            <small>{observation}<br/></small>
                                        )
                                    })}
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    )
}

export default ListEncounters;