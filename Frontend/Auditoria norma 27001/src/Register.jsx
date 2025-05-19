import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.css";
import ErrorPopup from "./assets/Componentes/ErrorPopup";

const Register = () => {
  const [nome, setNome] = useState("");
  const [apelido, setApelido] = useState("");
  const [dia, setDia] = useState("");
  const [mes, setMes] = useState("");
  const [ano, setAno] = useState("");
  const [empresa, setEmpresa] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmarPassword, setConfirmarPassword] = useState("");
  const [aCarregar, setACarregar] = useState(false);
  const [errorPopup, setErrorPopup] = useState({ show: false, message: "", type: "error" });
  const redirecionar = useNavigate();

  // Criar arrays para os dias, meses e anos
  const dias = Array.from({ length: 31 }, (_, i) => i + 1);
  const meses = [
    { value: 1, label: "Janeiro" },
    { value: 2, label: "Fevereiro" },
    { value: 3, label: "Março" },
    { value: 4, label: "Abril" },
    { value: 5, label: "Maio" },
    { value: 6, label: "Junho" },
    { value: 7, label: "Julho" },
    { value: 8, label: "Agosto" },
    { value: 9, label: "Setembro" },
    { value: 10, label: "Outubro" },
    { value: 11, label: "Novembro" },
    { value: 12, label: "Dezembro" }
  ];
  const anoAtual = new Date().getFullYear();
  const anos = Array.from({ length: anoAtual - 1899 }, (_, i) => anoAtual - i);

  // Ajustar dias disponíveis com base no mês e ano selecionados
  const [diasDisponiveis, setDiasDisponiveis] = useState(dias);

  useEffect(() => {
    if (mes && ano) {
      const ultimoDiaDoMes = new Date(ano, mes, 0).getDate();
      setDiasDisponiveis(Array.from({ length: ultimoDiaDoMes }, (_, i) => i + 1));
      
      // Se o dia selecionado for maior que o último dia do mês, ajustar
      if (dia > ultimoDiaDoMes) {
        setDia("");
      }
    }
  }, [mes, ano, dia]);

  const validarFormulario = () => {
    // Validar nome e apelido (apenas letras)
    if (!/^[A-Za-zÀ-ÖØ-öø-ÿ\s]+$/.test(nome)) {
      setErrorPopup({
        show: true,
        message: "O nome deve conter apenas letras.",
        type: "error"
      });
      return false;
    }
    
    if (!/^[A-Za-zÀ-ÖØ-öø-ÿ\s]+$/.test(apelido)) {
      setErrorPopup({
        show: true,
        message: "O apelido deve conter apenas letras.",
        type: "error"
      });
      return false;
    }
    
    // Validar data de nascimento
    if (!dia || !mes || !ano) {
      setErrorPopup({
        show: true,
        message: "Por favor, selecione uma data de nascimento completa.",
        type: "error"
      });
      return false;
    }
    
    // Validar idade mínima (13 anos)
    const dataNascimento = new Date(ano, mes - 1, dia);
    const hoje = new Date();
    const idade = hoje.getFullYear() - dataNascimento.getFullYear();
    const mesAtual = hoje.getMonth();
    const diaAtual = hoje.getDate();
    const ajusteIdade = (mesAtual < dataNascimento.getMonth() || 
                        (mesAtual === dataNascimento.getMonth() && 
                         diaAtual < dataNascimento.getDate())) ? 1 : 0;
    const idadeReal = idade - ajusteIdade;
    
    if (idadeReal < 13) {
      setErrorPopup({
        show: true,
        message: "É necessário ter pelo menos 13 anos para se registar.",
        type: "error"
      });
      return false;
    }
    
    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErrorPopup({
        show: true,
        message: "Por favor, insira um email válido.",
        type: "error"
      });
      return false;
    }
    
    // Validar password (mínimo 6 caracteres)
    if (password.length < 6) {
      setErrorPopup({
        show: true,
        message: "A password deve ter pelo menos 6 caracteres.",
        type: "error"
      });
      return false;
    }
    
    // Validar confirmação de password
    if (password !== confirmarPassword) {
      setErrorPopup({
        show: true,
        message: "As passwords não coincidem.",
        type: "error"
      });
      return false;
    }
    
    return true;
  };

  const processarRegisto = async (e) => {
    e.preventDefault();
    
    // Validar o formulário antes de enviar
    if (!validarFormulario()) {
      return;
    }
    
    setACarregar(true);

    // Formatar a data no formato ISO (YYYY-MM-DD)
    const dataNascimento = `${ano}-${mes.toString().padStart(2, '0')}-${dia.toString().padStart(2, '0')}`;

    const utilizador = {
      nome,
      apelido,
      dataNascimento,
      empresa,
      email,
      password,
    };

    try {
      // Enviar os dados para o backend (Spring Boot)
      const resposta = await axios.post("http://localhost:8082/api/register", utilizador);
      console.log(resposta.data);
      
      // Mostrar popup de sucesso
      setErrorPopup({
        show: true,
        message: "Registo bem-sucedido! A redirecionar para login...",
        type: "success"
      });
      
      // Redirecionar para a página de login após 2 segundos
      setTimeout(() => {
        redirecionar("/login");
      }, 2000);
      
    } catch (erro) {
      console.error("Erro ao registar: ", erro);
      // Determinar a mensagem de erro com base na resposta
      let mensagemErro = "Erro ao registar utilizador.";
      
      if (erro.response) {
        // Verificar tipos específicos de erro
        if (erro.response.status === 409) {
          mensagemErro = "Este email já está registado. Por favor, use outro email.";
        } else if (erro.response.data && erro.response.data.message) {
          mensagemErro = erro.response.data.message;
        }
      }
      
      setErrorPopup({
        show: true,
        message: mensagemErro,
        type: "error"
      });
    } finally {
      setACarregar(false);
    }
  };

  // Função para fechar o popup
  const fecharErrorPopup = () => {
    setErrorPopup({ ...errorPopup, show: false });
  };

  return (
    <div className="pagina-registo">
      <div className="formulario">
        <form className="formulario-registo" onSubmit={processarRegisto}>
          <input
            type="text"
            placeholder="Nome"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Apelido"
            value={apelido}
            onChange={(e) => setApelido(e.target.value)}
            required
          />
          
          
          <div className="selecao-data">
            <select 
              value={dia} 
              onChange={(e) => setDia(e.target.value)}
              required
              className="selecao-data-campo"
            >
              <option value="">Dia</option>
              {diasDisponiveis.map((d) => (
                <option key={`dia-${d}`} value={d}>
                  {d}
                </option>
              ))}
            </select>
            
            <select 
              value={mes} 
              onChange={(e) => setMes(e.target.value)}
              required
              className="selecao-data-campo"
            >
              <option value="">Mês</option>
              {meses.map((m) => (
                <option key={`mes-${m.value}`} value={m.value}>
                  {m.label}
                </option>
              ))}
            </select>
            
            <select 
              value={ano} 
              onChange={(e) => setAno(e.target.value)}
              required
              className="selecao-data-campo"
            >
              <option value="">Ano</option>
              {anos.map((a) => (
                <option key={`ano-${a}`} value={a}>
                  {a}
                </option>
              ))}
            </select>
          </div>
          
          <input
            type="text"
            placeholder="Empresa"
            value={empresa}
            onChange={(e) => setEmpresa(e.target.value)}
          />
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
          <input
            type="password"
            placeholder="Confirmar Password"
            value={confirmarPassword}
            onChange={(e) => setConfirmarPassword(e.target.value)}
            required
          />
          <button type="submit" disabled={aCarregar}>
            {aCarregar ? "A processar..." : "Criar Conta"}
          </button>
          <p className="mensagem">
            Já tem conta?{" "}
            <a href="#" onClick={() => redirecionar("/login")}>
              Iniciar sessão
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

export default Register;