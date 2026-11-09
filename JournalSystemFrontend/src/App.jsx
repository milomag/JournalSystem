
import {useEffect, useState} from 'react'

function App(){
  const [message, setMessage] = useState("Loading...")

  useEffect(()=>{
      fetch("/api/hello")
        .then(response => response.text())
        .then(data => setMessage(data))
        .catch(error => {
          console.error(error)
          setMessage("Could not connect to backend")
        })
  }, [])

  return(
      <div>
        <h1>JournalSystem</h1>
        <p>Backend response: {message}</p>
      </div>
  )
}

export default App
