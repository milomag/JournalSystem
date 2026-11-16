import { useEffect, useState } from "react";

function ListConditions(user) {

    const [patientId, setPatientId] = useState(user.searchId);
    const [conditions, setConditions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const isPatient = user.role === "PATIENT";
    const isStaff = user.role === "STAFF";

    async function fetchConditions(url) {
        setLoading(true);

        try {
            const token = localStorage.getItem("token");

            const response = await fetch(url, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if(!response.ok){
                throw new Error(`Could not load conditions (${response.status})`);
            }

            const data = await response.json();
            if (data == null){
                setConditions([]);
            } else {
                setConditions(data);
            }

        }catch(error){
            setConditions([]);
            setError(error.message);
        }finally{
            setLoading(false);
        }

    }

    async function searchConditions(){
        if (!patientId) {
            setError("Undefined patient ID.");
            setConditions([]);
            return;
        }

        let url;
        if (isPatient) {
            url = `/api/condition/me`;
        } else if (isStaff){
            url = `/api/condition/staff/${patientId}`
        } else {
            url = `/api/condition/${patientId}`
        }

        await fetchConditions(url);
    }

    useEffect(() => {
        let ignore = false;

        if (!ignore)searchConditions();
        return ()=>{
            ignore = true;
        };
    },[patientId]);

    return (
        <div>
            <h2>Conditions</h2>
            <button onClick={searchConditions}>
                Refresh
            </button>

            {error && <p>{error}</p>}

            <div>
                {conditions.length === 0 ? (
                    <p>No conditions to show</p>
                ) : (
                    <div>
                        {conditions.map(condition => {
                            return (
                                    <div>
                                        <p>Diagnosis: {condition.diagnosis}</p>
                                        <small>Details: {condition.details}</small>
                                    </div>
                                );
                        })}
                    </div>
                )}
            </div>
        </div>
    )
}

export default ListConditions;