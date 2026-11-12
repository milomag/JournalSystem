
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Contacts({ user }){
    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        async function fetchContacts(){
            try{
                const token = localStorage.getItem("token");

                const response = await fetch("/api/messages/contacts", {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                if(!response.ok){
                    throw new Error("Failed to fetch contacts");
                }

                const data = await response.json();
                setContacts(data);

            }catch(error){
                setError(error.message);
            }finally{
                setLoading(false);
            }
        }

        fetchContacts();
    }, [user.token]);

    if(loading){
        return <p>Loading contacts...</p>;
    }

    if(error){
        return <p>{error}</p>;
    }

    return(
        <div className="contacts-container">
            <h2>Contacts</h2>

            {contacts.length === 0 ? (
                <p>No contacts available.</p>
            ) : (
                <div className="contacts-list">
                    {contacts.map(contact => (
                        <div key={contact.id} className="contact-item">
                            <h3>{contact.email}</h3>
                            <p>{contact.role}</p>

                            <button
                                onClick={() => navigate(`/messages/${contact.id}`)}
                            >
                                Message
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Contacts;
