import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.css";
import ErrorPopup from "./assets/Componentes/ErrorPopup";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorPopup, setErrorPopup] = useState({ show: false, message: "", type: "error" });
  const [aCarregar, setACarregar] = useState(false);
  const redirecionar = useNavigate();

  const validarFormulario = () => {
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErrorPopup({
        show: true,
        message: "Por favor, insira um email válido.",
        type: "error"
      });
      return false;
    }
    
    // Validar password (não vazia)
    if (!password.trim()) {
      setErrorPopup({
        show: true,
        message: "Por favor, insira a sua password.",
        type: "error"
      });
      return false;
    }
    
    return true;
  };

  const processarLogin = async (e) => {
    e.preventDefault();
    
    // Validar o formulário antes de enviar
    if (!validarFormulario()) {
      return;
    }
    
    setACarregar(true);

    try {
      // Enviar as credenciais para o backend (Spring Boot)
      const resposta = await axios.post("http://localhost:8082/api/login", null, {
        params: {
          email,
          password,
        },
      });

      if (resposta.status === 200) {
        console.log("Login bem-sucedido");
        
        // Mostrar popup de sucesso
        setErrorPopup({
          show: true,
          message: "Login bem-sucedido! A redirecionar...",
          type: "success"
        });
        
        // Armazenar o email do utilizador no localStorage para controlo de autenticação
        localStorage.setItem("userEmail", email);
        
        // Disparar evento para notificar outros componentes sobre a mudança no localStorage
        window.dispatchEvent(new Event('storage'));
        
        // Redirecionar para a página inicial após 1 segundo
        setTimeout(() => {
          redirecionar("/");
        }, 1000);
      }
    } catch (erro) {
      console.error("Erro ao fazer login: ", erro);
      // Mostrar o popup de erro
      setErrorPopup({
        show: true,
        message: "Credenciais inválidas! Tente novamente.",
        type: "error"
      });
    } finally {
      setACarregar(false);
    }
  };

  const fecharErrorPopup = () => {
    setErrorPopup({ ...errorPopup, show: false });
  };

  return (
    <div className="pagina-login">
      <div className="formulario">
        <form className="formulario-login" onSubmit={processarLogin}>
          <input  
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" disabled={aCarregar}>
            {aCarregar ? "A processar..." : "Login"}
          </button>
          <p className="mensagem">
            Não tem conta?{" "}
            <a href="#" onClick={() => redirecionar("/register")}>
              Registe-se
            </a>
          </p>
        </form>
      </div>
      
      
      <ErrorPopup 
        message={errorPopup.message}
        show={errorPopup.show}
        onClose={fecharErrorPopup}
        type={errorPopup.type}
      />
    </div>
  );
};

export default Login;