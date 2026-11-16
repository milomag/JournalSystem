import { useState } from "react";

function AddEncounter(patient) {
    const [userId, setUserId] = useState(patient.id);

    const [observations, setObservations] = useState([]);
    const [observation, setObservation] = useState("");
    const [practitionerEmail, setPractitionerEmail] = useState("");
    const [location, setLocation] = useState("");
    const [time, setTime] = useState("");
    const [sending, setSending] = useState(false);
    const [message, setMessage] = useState("");

    const token = localStorage.getItem("token");

    const isStaff = patient.userRole === "STAFF";

    async function handleSubmit(event) {
        event.preventDefault();

        if(practitionerEmail.trim()==="" || time.trim()==="" || sending) {
            return;
        }

        const body = {practitionerEmail, time, location, observations}

        try {
            setSending(true);
            setMessage("");

            let url = `/api/encounter/${userId}`;
            if (isStaff) url = `/api/encounter/staff/${userId}`

            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error(`Could not submit encounter (${response.status})`)
            }

            setMessage("Successfully added encounter");
            setPractitionerEmail("");
            setTime("");
            setLocation("");
            setObservation("");

        } catch (error) {
            setMessage(error.message);
        } finally {
            setSending(false);
        }
    }

    async function addObservation(event) {
        event.preventDefault();
        if (observation.trim() === "") return;
        observations.push(observation);
        setObservation("");
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="practitioner email"
                    value={practitionerEmail}
                    onChange={event =>setPractitionerEmail(event.target.value)}
                />
                <input
                    type="text"
                    placeholder="Location"
                    value={location}
                    onChange={event => setLocation(event.target.value)}
                />
                <input
                    type="datetime-local"
                    value={time}
                    onChange={event => setTime(event.target.value)}
                />
                <button type="submit">Submit</button>
            </form>
            {!(observations.length === 0) ? (
                <div>
                    {observations.map(obs => {
                        return (
                            <div>
                                <p>Observation: {obs}</p>
                            </div>
                        );
                    })}
                </div>
            ): null}
            <form onSubmit={addObservation}>
                <input
                    type="text"
                    placeholder="observation"
                    value={observation}
                    onChange={event => setObservation(event.target.value)}
                />
                <button type="submit">Add</button>
            </form>
            <p>{message}</p>
        </div>
    )
}

export default AddEncounter;