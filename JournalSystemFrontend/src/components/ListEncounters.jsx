
import { useEffect, useState } from "react";

function ListEncounters({ searchId, role }) {
    const patientId = searchId;

    const isPatient = role === "PATIENT";

    const [encounters, setEncounters] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

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

            if(!response.ok) {
                throw new Error(`Could not load encounters (${response.status})`);
            }

            const data = await response.json();
            setEncounters(data ?? []);

        } catch(error) {
            setEncounters([]);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    async function refreshEncounters(){
        if(!isPatient && !patientId){
            setError("Undefined patient ID.");
            setEncounters([]);
            return;
        }

        const url = isPatient
            ? "/api/encounter/me"
            : `/api/encounter/${patientId}`;

        await fetchEncounters(url);
    }

    useEffect(() => {
        async function loadEncounters(){
            if(!isPatient && !patientId){
                setError("Undefined patient ID.");
                setEncounters([]);
                return;
            }

            const url = isPatient
                ? "/api/encounter/me"
                : `/api/encounter/${patientId}`;

            await fetchEncounters(url);
        }

        loadEncounters();
    }, [patientId, role]);

    return (
        <div>
            <h2>Encounters</h2>

            <button onClick={refreshEncounters} disabled={loading}>
                {loading ? "Loading..." : "Refresh"}
            </button>

            {error && <p>{error}</p>}

            {loading ? (
                <p>Loading encounters...</p>
            ) : encounters.length === 0 ? (
                <p>No encounters to show</p>
            ) : (
                <div>
                    {encounters.map(encounter => (
                        <div key={encounter.id}>
                            <p>
                                {encounter.practitionerEmail} : {encounter.time} : {encounter.location}
                            </p>

                            {encounter.observations?.map((observation, index) => (
                                <small key={index}>
                                    {observation}
                                    <br />
                                </small>
                            ))}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default ListEncounters;
