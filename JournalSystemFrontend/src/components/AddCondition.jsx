import { useState } from "react";

function AddCondition({ id }) {
    const userId = id;

    const [diagnosis, setDiagnosis] = useState("");
    const [details, setDetails] = useState("");
    const [sending, setSending] = useState(false);
    const [message, setMessage] = useState("");

    const token = localStorage.getItem("token");

    async function handleSubmit(event) {
        event.preventDefault();

        if(diagnosis.trim() === "" || sending) {
            return;
        }

        const body = { diagnosis, details };

        try {
            setSending(true);
            setMessage("");

            const response = await fetch(`/api/condition/${userId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error(`Could not submit condition (${response.status})`);
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
                    onChange={event => setDiagnosis(event.target.value)}
                    required
                />

                <input
                    type="text"
                    placeholder="Details"
                    value={details}
                    onChange={event => setDetails(event.target.value)}
                />

                <button type="submit" disabled={sending}>
                    {sending ? "Submitting..." : "Submit"}
                </button>
            </form>

            <p>{message}</p>
        </div>
    );
}

export default AddCondition;
