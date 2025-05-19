import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { 
  FaUser, FaEnvelope, FaCalendarAlt, FaBuilding, FaPen, FaCheck, FaTimes, 
  FaKey, FaPlus, FaFileAlt, FaDownload, FaEye, FaClipboardList 
} from "react-icons/fa";
import "./Perfil.css";
import ErrorPopup from "./assets/Componentes/ErrorPopup";

const Perfil = () => {
  const redirecionar = useNavigate();
  const [dadosUtilizador, setDadosUtilizador] = useState({
    id: "",
    nome: "",
    apelido: "",
    dataNascimento: "",
    empresas: [],
    email: "",
  });
  
  const [relatoriosUtilizador, setRelatoriosUtilizador] = useState([]);
  const [empresaAtual, setEmpresaAtual] = useState("");
  const [novaEmpresa, setNovaEmpresa] = useState("");
  

  const [palavraPasseAtualParaMudarPalavraPassse, setPalavraPasseAtualParaMudarPalavraPassse] = useState("");
  const [palavraPasseAtualParaMudarEmail, setPalavraPasseAtualParaMudarEmail] = useState("");
  const [novaPalavraPassse, setNovaPalavraPassse] = useState("");
  const [confirmarPalavraPassse, setConfirmarPalavraPassse] = useState("");
  
  const [novoEmail, setNovoEmail] = useState("");
  const [modoEdicao, setModoEdicao] = useState(false);
  const [erroPopup, setErroPopup] = useState({ mostrar: false, mensagem: "", tipo: "erro" });
  const [aCarregar, setACarregar] = useState(true);
  const [tabAtiva, setTabAtiva] = useState("email");

  useEffect(() => {
    // Verificar se o utilizador está logado
    const verificarAutenticacao = async () => {
      try {
        // Recuperar o email do utilizador logado do localStorage
        const emailUtilizador = localStorage.getItem("userEmail");
        
        if (!emailUtilizador) {
          // utilizador não está logado - redirecionar para login apenas se estamos na página de perfil
          redirecionar("/login");
          return;
        }
        
        
        const resposta = await axios.get(`http://localhost:8082/api/users/profile?email=${emailUtilizador}`);
        
        if (resposta.data) {
          // Adaptar os dados do utilizador
          const utilizador = resposta.data;
          setDadosUtilizador({
            id: utilizador.id,
            nome: utilizador.nome || "",
            apelido: utilizador.apelido || "",
            dataNascimento: utilizador.dataNascimento || "",
            empresas: utilizador.empresa ? [utilizador.empresa] : [],
            email: utilizador.email || "",
          });
          
          setEmpresaAtual(utilizador.empresa || "");
          setNovoEmail(utilizador.email || "");
          
          // Procurar os relatórios do utilizador
          procurarRelatoriosUtilizador(utilizador.id);
        }
      } catch (erro) {
        console.error("Erro ao carregar dados do perfil:", erro);
        setErroPopup({
          mostrar: true,
          mensagem: "Não foi possível carregar seus dados.",
          tipo: "erro"
        });
        setTimeout(() => redirecionar("/login"), 2000);
      } finally {
        setACarregar(false);
      }
    };

    verificarAutenticacao();
  }, [redirecionar]);

  const fecharErroPopup = () => {
    setErroPopup({ ...erroPopup, mostrar: false });
  };
  
  // Procurar os relatórios do utilizador
  const procurarRelatoriosUtilizador = async (idUtilizador) => {
    try {
      const resposta = await axios.get(`http://localhost:8082/api/checklists/user/${idUtilizador}`);
      if (resposta.data) {
        setRelatoriosUtilizador(resposta.data);
      }
    } catch (erro) {
      console.error("Erro a procurar relatórios:", erro);
      setErroPopup({
        mostrar: true,
        mensagem: "Erro a procurar relatórios",
        tipo: "erro"
      });
    }
  };

  const alternarModoEdicao = () => {
    setModoEdicao(!modoEdicao);
    // Resetar campos de edição quando sair do modo de edição
    if (modoEdicao) {
      setNovaPalavraPassse("");
      setConfirmarPalavraPassse("");
      setPalavraPasseAtualParaMudarPalavraPassse("");
      setPalavraPasseAtualParaMudarEmail("");
      setNovoEmail(dadosUtilizador.email);
      setEmpresaAtual(dadosUtilizador.empresas[0] || "");
      setNovaEmpresa("");
    }
  };

  const atualizarPalavraPassse = async (e) => {
    e.preventDefault();
    
    if (novaPalavraPassse !== confirmarPalavraPassse) {
      setErroPopup({
        mostrar: true,
        mensagem: "As Palavras-passe não coincidem",
        tipo: "erro"
      });
      return;
    }
    
    try {
      const resposta = await axios.put(`http://localhost:8082/api/users/password`, {
        email: dadosUtilizador.email,
        currentPassword: palavraPasseAtualParaMudarPalavraPassse,
        newPassword: novaPalavraPassse
      });
      
      if (resposta.status === 200) {
        setErroPopup({
          mostrar: true,
          mensagem: "Palavra-passe atualizada com sucesso!",
          tipo: "sucesso"
        });
        setPalavraPasseAtualParaMudarPalavraPassse("");
        setNovaPalavraPassse("");
        setConfirmarPalavraPassse("");
      }
    } catch (erro) {
      console.error("Erro ao atualizar Palavra-passe:", erro);
      setErroPopup({
        mostrar: true,
        mensagem: "Não foi possível atualizar a Palavra-passe. Verifique se a Palavra-passe atual está correta.",
        tipo: "erro"
      });
    }
  };

  const atualizarEmail = async (e) => {
    e.preventDefault();
    
    if (novoEmail === dadosUtilizador.email) {
      setErroPopup({
        mostrar: true,
        mensagem: "O novo email é igual ao atual",
        tipo: "erro"
      });
      return;
    }
    
    try {
      const resposta = await axios.put(`http://localhost:8082/api/users/email`, {
        userId: dadosUtilizador.id,
        currentEmail: dadosUtilizador.email,
        newEmail: novoEmail,
        password: palavraPasseAtualParaMudarEmail
      });
      
      if (resposta.status === 200) {
        // Atualizar o email no localStorage
        localStorage.setItem("userEmail", novoEmail);
        
        setDadosUtilizador({
          ...dadosUtilizador,
          email: novoEmail
        });
        
        setErroPopup({
          mostrar: true,
          mensagem: "Email atualizado com sucesso!",
          tipo: "sucesso"
        });
        setPalavraPasseAtualParaMudarEmail("");
      }
    } catch (erro) {
      console.error("Erro ao atualizar email:", erro);
      setErroPopup({
        mostrar: true,
        mensagem: "Não foi possível atualizar o email. Verifique se a Palavra-passe está correta.",
        tipo: "erro"
      });
    }
  };

  const atualizarEmpresa = async (e) => {
    e.preventDefault();
    
    if (!novaEmpresa) {
      setErroPopup({
        mostrar: true,
        mensagem: "Escreva o nome da nova empresa",
        tipo: "erro"
      });
      return;
    }
    
    try {
      // Adicionar nova empresa se informada
      if (novaEmpresa) {
        const empresas = [...dadosUtilizador.empresas];
        if (!empresas.includes(novaEmpresa)) {
          empresas.push(novaEmpresa);
          
          await axios.post(`http://localhost:8082/api/users/empresas/add`, {
            userId: dadosUtilizador.id,
            empresa: novaEmpresa
          });
          
          setDadosUtilizador({
            ...dadosUtilizador,
            empresas: empresas
          });
          
          setErroPopup({
            mostrar: true,
            mensagem: "Empresa adicionada com sucesso!",
            tipo: "sucesso"
          });
          
          setNovaEmpresa("");
        } else {
          setErroPopup({
            mostrar: true,
            mensagem: "Esta empresa já está na sua lista",
            tipo: "erro"
          });
        }
      }
    } catch (erro) {
      console.error("Erro ao atualizar empresa:", erro);
      setErroPopup({
        mostrar: true,
        mensagem: "Não foi possível atualizar as informações da empresa.",
        tipo: "erro"
      });
    }
  };

  // Função para download um relatório existente
  const downloadRelatorio = async (idRelatorio, nomeFicheiro) => {
    try {
      
      const resposta = await axios.get(`http://localhost:8082/api/checklists/${idRelatorio}/download`, {
        responseType: 'blob'
      });
      
      // Criar um URL para o blob e iniciar o download
      const url = window.URL.createObjectURL(new Blob([resposta.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', nomeFicheiro || `relatorio-${idRelatorio}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
      setErroPopup({
        mostrar: true,
        mensagem: "Download do relatório iniciado!",
        tipo: "sucesso"
      });
    } catch (erro) {
      console.error("Erro ao download o relatório:", erro);
      setErroPopup({
        mostrar: true,
        mensagem: "Não foi possível download o relatório.",
        tipo: "erro"
      });
    }
  };

  // Função para ver um relatório
  const verRelatorio = (idRelatorio) => {
    window.open(`http://localhost:8082/api/checklists/${idRelatorio}/view`, '_blank');
  };

  if (aCarregar) {
    return (
      <div className="perfil-container">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>A carregar informações do perfil...</p>
        </div>
      </div>
    );
  }

  // Formatação da data para exibição
  const formatarData = (stringData) => {
    if (!stringData) return "N/A";
    const data = new Date(stringData);
    return data.toLocaleDateString();
  };

  return (
    <div className="perfil-container">
      <div className="perfil-card">
        <div className="perfil-cabecalho">
          <div className="perfil-titulo">
            <h1>Meu Perfil</h1>
          </div>
          <button 
            className={`botao-editar ${modoEdicao ? 'ativo' : ''}`}
            onClick={alternarModoEdicao}
          >
            {modoEdicao ? (
              <>
                <FaTimes className="botao-icon" /> Cancelar Edição
              </>
            ) : (
              <>
                <FaPen className="botao-icon" /> Editar Perfil
              </>
            )}
          </button>
        </div>

        <div className="perfil-conteudo">
          <div className="perfil-info">
            <div className="utilizador-avatar">
              <div className="avatar-lugar">
                <span>{dadosUtilizador.nome.charAt(0)}{dadosUtilizador.apelido.charAt(0)}</span>
              </div>
              <div className="utilizador-nome">
                <h2>{dadosUtilizador.nome} {dadosUtilizador.apelido}</h2>
                <p>{dadosUtilizador.email}</p>
              </div>
            </div>

            <div className="info-card">
              <div className="info-card-cabecalho">
                <h3>Informações Pessoais</h3>
              </div>
              <div className="info-card-conteudo">
                <div className="info-item">
                  <div className="info-icon">
                    <FaUser />
                  </div>
                  <div className="info-detalhes">
                    <label>Nome Completo</label>
                    <span>{dadosUtilizador.nome} {dadosUtilizador.apelido}</span>
                  </div>
                </div>
                
                <div className="info-item">
                  <div className="info-icon">
                    <FaEnvelope />
                  </div>
                  <div className="info-detalhes">
                    <label>Email</label>
                    <span>{dadosUtilizador.email}</span>
                  </div>
                </div>
                
                <div className="info-item">
                  <div className="info-icon">
                    <FaCalendarAlt />
                  </div>
                  <div className="info-detalhes">
                    <label>Data de Nascimento</label>
                    <span>{formatarData(dadosUtilizador.dataNascimento)}</span>
                  </div>
                </div>
                
                <div className="info-item">
                  <div className="info-icon">
                    <FaBuilding />
                  </div>
                  <div className="info-detalhes">
                    <label>Empresas</label>
                    <div className="empresas-lista">
                      {dadosUtilizador.empresas.length > 0 ? (
                        dadosUtilizador.empresas.map((empresa, index) => (
                          <span key={index} className="empresa-badge">
                            {empresa}
                          </span>
                        ))
                      ) : (
                        <span className="sem-dados">Nenhuma empresa cadastrada</span>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            {/* Seção de Relatórios */}
            <div className="info-card relatorios-card">
              <div className="info-card-cabecalho">
                <h3>Meus Relatórios</h3>
              </div>
              <div className="info-card-conteudo">
                {relatoriosUtilizador.length > 0 ? (
                  <div className="relatorios-lista">
                    {relatoriosUtilizador.map((relatorio) => (
                      <div key={relatorio.id} className="relatorio-item">
                        <div className="relatorio-info">
                          <div className="relatorio-icon">
                            <FaFileAlt />
                          </div>
                          <div className="relatorio-detalhes">
                            <h4>{relatorio.empresa}</h4>
                            <div className="relatorio-meta">
                              <span className="relatorio-data">{formatarData(relatorio.dataAvaliacao)}</span>
                              <span className="relatorio-tipo">ISO 27001</span>
                            </div>
                          </div>
                        </div>
                        <div className="relatorio-acoes">
                          <button 
                            className="botao-relatorio botao-ver"
                            onClick={() => verRelatorio(relatorio.id)}
                            title="Visualizar relatório"
                          >
                            <FaEye />
                          </button>
                          <button 
                            className="botao-relatorio botao-download"
                            onClick={() => downloadRelatorio(relatorio.id, `${relatorio.empresa}-${formatarData(relatorio.dataAvaliacao)}.pdf`)}
                            title="download relatório"
                          >
                            <FaDownload />
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="sem-relatorios">
                    <div className="sem-relatorios-icon">
                      <FaClipboardList />
                    </div>
                    <p>Você ainda não tem relatórios salvos.</p>
                    <button 
                      className="botao-criar-relatorio"
                      onClick={() => redirecionar('/relatorio')}
                    >
                      Criar Novo Relatório
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>

          {modoEdicao && (
            <div className="perfil-edicao">
              <div className="edicao-tabs">
                <button 
                  className={`tab-botao ${tabAtiva === 'email' ? 'ativo' : ''}`}
                  onClick={() => setTabAtiva('email')}
                >
                  <FaEnvelope className="tab-icon" />
                  <span>Email</span>
                </button>
                <button 
                  className={`tab-botao ${tabAtiva === 'password' ? 'ativo' : ''}`}
                  onClick={() => setTabAtiva('password')}
                >
                  <FaKey className="tab-icon" />
                  <span>Palavra-Passe</span>
                </button>
                <button 
                  className={`tab-botao ${tabAtiva === 'empresas' ? 'ativo' : ''}`}
                  onClick={() => setTabAtiva('empresas')}
                >
                  <FaBuilding className="tab-icon" />
                  <span>Empresas</span>
                </button>
              </div>

              <div className="edicao-conteudo">
                {tabAtiva === 'email' && (
                  <div className="edicao-seccao">
                    <h2>Alterar Email</h2>
                    <form onSubmit={atualizarEmail}>
                      <div className="formulario-grupo">
                        <label>Novo Email:</label>
                        <input
                          type="email"
                          value={novoEmail}
                          onChange={(e) => setNovoEmail(e.target.value)}
                          required
                        />
                      </div>
                      <div className="formulario-grupo">
                        <label>Palavra-Passe para verificação:</label>
                        <input
                          type="password"
                          value={palavraPasseAtualParaMudarEmail}
                          onChange={(e) => setPalavraPasseAtualParaMudarEmail(e.target.value)}
                          required
                        />
                      </div>
                      <button type="submit" className="botao-guardar">
                        <FaCheck className="botao-icon" /> Atualizar Email
                      </button>
                    </form>
                  </div>
                )}

                {tabAtiva === 'password' && (
                  <div className="edicao-seccao">
                    <h2>Alterar Palavra-Passe</h2>
                    <form onSubmit={atualizarPalavraPassse}>
                      <div className="formulario-grupo">
                        <label>Palavra-Passe Atual:</label>
                        <input
                          type="password"
                          value={palavraPasseAtualParaMudarPalavraPassse}
                          onChange={(e) => setPalavraPasseAtualParaMudarPalavraPassse(e.target.value)}
                          required
                        />
                      </div>
                      <div className="formulario-grupo">
                        <label>Nova Palavra-Passe:</label>
                        <input
                          type="password"
                          value={novaPalavraPassse}
                          onChange={(e) => setNovaPalavraPassse(e.target.value)}
                          required
                        />
                      </div>
                      <div className="formulario-grupo">
                        <label>Confirmar Nova Palavra-Passe:</label>
                        <input
                          type="password"
                          value={confirmarPalavraPassse}
                          onChange={(e) => setConfirmarPalavraPassse(e.target.value)}
                          required
                        />
                      </div>
                      <button type="submit" className="botao-guardar">
                        <FaCheck className="botao-icon" /> Atualizar Palavra-Passe
                      </button>
                    </form>
                  </div>
                )}

                {tabAtiva === 'empresas' && (
                  <div className="edicao-seccao">
                    <h2>Adicionar Nova Empresa</h2>
                    <form onSubmit={atualizarEmpresa}>
                      <div className="formulario-grupo">
                        <label>Nome da Empresa:</label>
                        <input
                          type="text"
                          value={novaEmpresa}
                          onChange={(e) => setNovaEmpresa(e.target.value)}
                          placeholder="Digite o nome da empresa"
                        />
                      </div>
                      <button type="submit" className="botao-guardar">
                        <FaPlus className="botao-icon" /> Adicionar Empresa
                      </button>
                    </form>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </div>
      
      <ErrorPopup 
        message={erroPopup.mensagem}
        show={erroPopup.mostrar}
        onClose={fecharErroPopup}
        type={erroPopup.tipo}
      />
    </div>
  );
};

export default Perfil;