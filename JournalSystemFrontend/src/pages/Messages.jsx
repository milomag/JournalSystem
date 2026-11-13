
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function Messages({ user }){
    const { userId } = useParams();

    const [messages, setMessages] = useState([]);
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(true);
    const [sending, setSending] = useState(false);
    const [error, setError] = useState("");

    const token = localStorage.getItem("token");
    async function fetchMessages(){
        try{
            setError("");

            const response = await fetch(`/api/messages/${userId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if(!response.ok){
                throw new Error(`Could not load messages (${response.status})`);
            }

            const data = await response.json();
            setMessages(data);

        }catch(error){
            setError(error.message);
        }finally{
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchMessages();
    }, [userId]);

    async function handleSend(event){
        event.preventDefault();

        if(!content.trim() || sending){
            return;
        }

        try{
            setSending(true);
            setError("");

            const response = await fetch(`/api/messages/${userId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ content })
            });

            if(!response.ok){
                throw new Error(`Could not send message (${response.status})`);
            }

            const savedMessage = await response.json();

            setMessages(prev => [...prev, savedMessage]);
            setContent("");

        }catch(error){
            setError(error.message);
        }finally{
            setSending(false);
        }
    }

    if(loading){
        return <p>Loading messages...</p>;
    }

    return(
        <div className="messages-container">
            <h2>Conversation</h2>

            <button onClick={fetchMessages}>
                Refresh
            </button>

            {error && <p>{error}</p>}

            <div className="messages-list">
                {messages.length === 0 ? (
                    <p>No messages yet. Start the conversation!</p>
                ) : (
                    messages.map(message => {
                        const ownMessage = message.senderId === user.id;

                        return(
                            <div
                                key={message.id}
                                className={ownMessage ? "message own-message" : "message"}
                            >
                                <p>{message.content}</p>

                                <small>
                                    {ownMessage ? "You" : "Other user"}
                                    {" - "}
                                    {new Date(message.timestamp).toLocaleString()}
                                </small>
                            </div>
                        );
                    })
                )}
            </div>

            <form onSubmit={handleSend} className="message-form">
                <input
                    type="text"
                    value={content}
                    onChange={e => setContent(e.target.value)}
                    placeholder="Write a message..."
                    required
                />

                <button type="submit" disabled={sending || !content.trim()}>
                    {sending ? "Sending..." : "Send"}
                </button>
            </form>
        </div>
    );
}

export default Messages;
