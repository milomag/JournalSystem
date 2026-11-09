import { useState } from "react";

function Auth(){
    const [register, setRegister] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("PATIENT");
    const [message, setMessage] = useState("");
    const [fullName, setFullName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");

    async function handleSubmit(event){
        event.preventDefault();
        setMessage("");

        const url = register
            ? "/api/users/register"
            : "/api/users/login";

        const body = register
            ? { email, password, role, fullName, dateOfBirth }
            : { email, password };

        try{
            const response = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });

            const data = await response.json();

            if(!response.ok){
                setMessage(Object.values(data).join(", "));
                return;
            }

            localStorage.setItem("user", JSON.stringify(data));
            setMessage(register ? "Registration successful!" : "Login successful!");
        }
        catch(error){
            setMessage("Could not connect to the server.");
        }
    }

    return(
        <div>
            <h1>{register ? "Register" : "Login"}</h1>

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={e=>setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={e=>setPassword(e.target.value)}
                    required
                />

                {register && role === "PATIENT" && (
                    <>
                        <input
                            type="text"
                            placeholder="Full name"
                            value={fullName}
                            onChange={e=>setFullName(e.target.value)}
                            required
                        />

                        <input
                            type="date"
                            value={dateOfBirth}
                            onChange={e=>setDateOfBirth(e.target.value)}
                            required
                        />
                    </>
                )}

                {register && (
                    <select value={role} onChange={e=>setRole(e.target.value)}>
                        <option value="PATIENT">Patient</option>
                        <option value="DOCTOR">Doctor</option>
                        <option value="STAFF">Staff</option>
                    </select>
                )}

                <button type="submit">
                    {register ? "Register" : "Login"}
                </button>
            </form>

            <p>{message}</p>

            <button onClick={()=>{
                setRegister(!register);
                setMessage("");
            }}>
                {register ? "Back to Login" : "Create Account"}
            </button>
        </div>
    );
}

export default Auth;