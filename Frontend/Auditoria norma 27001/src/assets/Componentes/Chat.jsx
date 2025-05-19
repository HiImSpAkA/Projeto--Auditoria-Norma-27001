import React, { useEffect, useState } from "react";
import { FaRobot, FaUser, FaTimes } from "react-icons/fa";  
import axios from "axios";  
import './Chat.css';  

const Chat = () => {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState("");
  const [isChatOpen, setIsChatOpen] = useState(true); 

  // Função para processar texto e remover todos os tipos de asteriscos
  const processText = (text) => {
    if (!text) return "";
    
    // Primeiro, substitui combinações de asteriscos que representam formatação
    let processed = text.replace(/\*\*(.*?)\*\*/g, '$1'); // Remove negrito
    processed = processed.replace(/\*(.*?)\*/g, '$1');    // Remove itálico
    
    //Remove quaisquer asteriscos restantes (isolados ou em outras combinações)
    processed = processed.replace(/\*/g, '');
    
    // Garante quebras de linha adequadas (transforma \n em quebras reais)
    processed = processed.replace(/\\n/g, '\n');
    
    return processed;
  };

  // Função para enviar uma mensagem
  const sendMessage = async () => {
    if (inputMessage.trim() === "") return;

    // Mensagem do utilizador
    const userMessage = {
      text: inputMessage,
      sender: "user",  // Indica que a mensagem é do utilizador
    };

    // Adiciona a mensagem do utilizador ao estado
    setMessages((prevMessages) => [...prevMessages, userMessage]);

    // Limpa o campo de input
    setInputMessage("");

    // Chama a API do Spring Boot para obter a resposta
    try {
      const response = await axios.post("http://localhost:8082/api/qna/ask", {
        question: inputMessage,
      });

      // Obtém a resposta e garante que é uma string
      let robotResponse = "";
      if (response.data && 
          response.data.candidates && 
          response.data.candidates[0] && 
          response.data.candidates[0].content && 
          response.data.candidates[0].content.parts && 
          response.data.candidates[0].content.parts[0]) {
        robotResponse = response.data.candidates[0].content.parts[0].text || "";
      }

      // Mensagem do AI - processar para remover asteriscos
      const robotMessage = {
        text: processText(robotResponse),  // Processar a resposta da API Gemini
        sender: "robot",  // Indica que a mensagem é do ai
      };

      
      setMessages((prevMessages) => [...prevMessages, robotMessage]);
    } catch (error) {
      console.error("Erro ao buscar a resposta:", error);
      
      const errorMessage = {
        text: "Desculpe, posso responder apenas sobre auditorias e normas",
        sender: "robot",
      };
      setMessages((prevMessages) => [...prevMessages, errorMessage]);
    }
  };

  const closeChat = () => {
    setIsChatOpen(false); 
  };
  
  useEffect(() => {
    if (isChatOpen){
        const introductoryMessage ={
            text : "Olá, sou assistente de informação e consigo auxiliar em todas as dúvidas que tenha sobre auditorias e normas.",
            sender: "robot",
        };
        setMessages([introductoryMessage]);
        
    }
 }, [isChatOpen]);

  if (!isChatOpen) return null; 

  return (
    <div className="chat-container">
      
      <div className="chat-header">
        <span>Chat</span>
        <div>
          <button onClick={closeChat}><FaTimes size={20} /></button>
        </div>
      </div>

      
      <div className="messages">
        {messages.map((message, index) => (
          <div
            key={index}
            className={`message ${message.sender === "robot" ? "robot" : "user"}`}
          >
            
            <div className="icon">
              {message.sender === "robot" ? (
                <FaRobot size={20} />
              ) : (
                <FaUser size={20} />
              )}
            </div>

            <div className="text">
              {message.text.split('\n').map((line, i) => (
                <React.Fragment key={i}>
                  {line}
                  {i < message.text.split('\n').length - 1 && <br />}
                </React.Fragment>
              ))}
            </div>
          </div>
        ))}
      </div>

      
      <div className="input-container">
        <input
          type="text"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          placeholder="Digite sua mensagem..."
          onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
        />
        <button onClick={sendMessage}>✉️</button>
      </div>
    </div>
  );
};

export default Chat;