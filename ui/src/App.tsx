import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';

interface Document {
    id: number,
    name: string,
    url: string,
}

function App() {
    const [documents, setDocuments] = useState<Document[]>([]);

    useEffect(() => {
        const getAllDocuments = async () => {
            const response = await fetch('/v1/documents/');
            const body = await response.json();
            setDocuments(body);
        };

        getAllDocuments().catch(console.error);
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    {documents.map((document: Document) => {
                        return (
                            <div key={document.id}>
                                Name: {document.name}
                                URL: {document.url}
                            </div>
                        )
                    })}
                </p>
            </header>
        </div>
    );
}

export default App;
