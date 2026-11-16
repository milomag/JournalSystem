import { useState } from "react";

function AddCondition(patient) {
    const [userId, setUserId] = useState(patient.id);

    const [diagnosis, setDiagnosis] = useState("");
    const [details, setDetails] = useState("");
    const [sending, setSending] = useState(false);
    const [message, setMessage] = useState("");

    const token = localStorage.getItem("token");

    const isStaff = patient.userRole === "STAFF";

    async function handleSubmit(event) {
        event.preventDefault();

        if(diagnosis.trim()==="" || sending) {
            return;
        }

        const body = {diagnosis, details}

        try {
            setSending(true);
            setMessage("");

            let url = `/api/condition/${userId}`
            if (isStaff) url = `/api/condition/staff/${userId}`

            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error(`Could not submit condition (${response.status})`)
            }

            setMessage("Successfully added condition");
            setDetails("");
            setDiagnosis("");

        } catch (error) {
            setMessage(error.message);
        } finally {
            setSending(false);
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Diagnosis"
                    value={diagnosis}
                    onChange={event =>setDiagnosis(event.target.value)}
                />
                <input
                    type="text"
                    placeholder="Details"
                    value={details}
                    onChange={event => setDetails(event.target.value)}
                />
                <button type="submit">Submit</button>
            </form>
            <p>{message}</p>
        </div>
    )
}

export default AddCondition;