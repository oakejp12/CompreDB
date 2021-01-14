import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";

interface Document {
    id: number,
    name: string,
    url: string,
}

function App() {
    const [documents, setDocuments] = useState<Document[]>([]);

    useEffect(() => {
        const getAllDocuments = async () => {
            const response = await axios.get<Document[]>('http://localhost:9443/v1/documents/');
            setDocuments(response.data);
        };

        getAllDocuments().catch(console.error);
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <ul>
                    {documents.map((document: Document) =>
                        <li key={document.id}>Name: {document.name} URL: {document.url}</li>)
                    }
                </ul>
            </header>
        </div>
    );
}

export default App;
