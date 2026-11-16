
import { useEffect, useState } from "react";

function ListConditions({ searchId, role }) {
    const patientId = searchId;

    const isPatient = role === "PATIENT";

    const [conditions, setConditions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function fetchConditions(url) {
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
                throw new Error(`Could not load conditions (${response.status})`);
            }

            const data = await response.json();
            setConditions(data ?? []);

        } catch(error) {
            setConditions([]);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    async function refreshConditions(){
        if(!isPatient && !patientId){
            setError("Undefined patient ID.");
            setConditions([]);
            return;
        }

        const url = isPatient
            ? "/api/condition/me"
            : `/api/condition/${patientId}`;

        await fetchConditions(url);
    }

    useEffect(() => {
        async function loadConditions(){
            if(!isPatient && !patientId){
                setError("Undefined patient ID.");
                setConditions([]);
                return;
            }

            const url = isPatient
                ? "/api/condition/me"
                : `/api/condition/${patientId}`;

            await fetchConditions(url);
        }

        loadConditions();
    }, [patientId, role]);

    return (
        <div>
            <h2>Conditions</h2>

            <button onClick={refreshConditions} disabled={loading}>
                {loading ? "Loading..." : "Refresh"}
            </button>

            {error && <p>{error}</p>}

            {loading ? (
                <p>Loading conditions...</p>
            ) : conditions.length === 0 ? (
                <p>No conditions to show</p>
            ) : (
                <div>
                    {conditions.map(condition => (
                        <div key={condition.id}>
                            <p>Diagnosis: {condition.diagnosis}</p>
                            <small>Details: {condition.details}</small>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default ListConditions;
