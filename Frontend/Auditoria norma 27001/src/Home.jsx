import React from "react";
import { useNavigate } from "react-router-dom";
import './Home.css';

const Home = () => {
  const navigate = useNavigate();

  
  const redirecionarParaLogin = () => {
    navigate("/recursos");
  };

  return (
    <div className="paginaInicial">
      <section className="hero">
        <h1>Bem-vindo ao site de apoio a norma ISO 27001!!</h1>
        <p>Aqui vai encontrar tudo o que precisa para implementar a norma na sua empresa e aprender tudo relacionado com a mesma. </p>
        <button className="botaoInicio" onClick={redirecionarParaLogin}>Comece Agora</button>
      </section>

      <section className="recursos" id="servicos">
        <h2>Recursos</h2>
        <div className="cardRecursos">
          <div className="card">
            <h3>Aprendizagem</h3>
            <p>Uma página para autoaprendizagem que fornece documentos, videos, cursos e até mesmo um chatbot ai!</p>
          </div>
          <div className="card">
            <h3>Relatórios</h3>
            <p>Preenchimento de checklists e relatórios detalhados sobre como pode melhorar a sua empresa</p>
          </div>
          <div className="card">
            <h3>Armazenamento</h3>
            <p>Aqui é possivel guardar os seus relatorios ou fazer download de forma a que fique ao seu alcance</p>
          </div>
        </div>
      </section>

      <section className="sobre" id="sobre">
        <h2>Sobre</h2>
        <p>Projeto de final de Curso de Engenharia Informática 2024/2025</p>
      </section>
    </div>
  );
};

export default Home;