import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Relatorio.css";
import { FaDownload } from "react-icons/fa"; // Importando apenas o ícone necessário
import ErrorPopup from "./assets/Componentes/ErrorPopup"; // Importando o componente ErrorPopup

const Relatorio = () => {
  // Estados do componente
  const [controlosChecklist, setControlosChecklist] = useState([]);
  const [aCarregar, setACarregar] = useState(true);
  const [nomeEmpresa, setNomeEmpresa] = useState("");
  const [empresasUtilizador, setEmpresasUtilizador] = useState([]);
  const [dataAvaliacao, setDataAvaliacao] = useState(
    new Date().toISOString().split("T")[0]
  );
  const [aExportar, setAExportar] = useState(false);
  const [estaAutenticado, setEstaAutenticado] = useState(false);
  const [mostrarMensagemAutenticacao, setMostrarMensagemAutenticacao] = useState(false);
  const [idUtilizador, setIdUtilizador] = useState(null);
  const [popupErro, setPopupErro] = useState({ mostrar: false, mensagem: "", tipo: "erro" });
  const redirecionar = useNavigate();

  
  const fecharPopupErro = () => {
    setPopupErro({ ...popupErro, mostrar: false });
  };

  // Verificar autenticação e carregar dados iniciais
  useEffect(() => {
    const emailUtilizador = localStorage.getItem("userEmail");
    
    if (!emailUtilizador) {
      setEstaAutenticado(false);
      setMostrarMensagemAutenticacao(true);
      // Redirecionar para login após 3 segundos
      const temporizador = setTimeout(() => {
        redirecionar("/login");
      }, 3000);
      
      return () => clearTimeout(temporizador);
    } else {
      setEstaAutenticado(true);
      obterInfoUtilizador(emailUtilizador);
      obterControlosChecklist();
    }
  }, [redirecionar]);
  
  // Procurar informações do utilizador pelo e-mail
  const obterInfoUtilizador = async (email) => {
    try {
      const resposta = await axios.get(`http://localhost:8082/api/users/profile?email=${email}`);
      
      if (resposta.data) {
        const utilizador = resposta.data;
        setIdUtilizador(utilizador.id);
        
        // Processar lista de empresas
        let empresas = [];
        if (utilizador.empresa) {
          empresas = utilizador.empresa.split(',').map(empresa => empresa.trim());
        }
        
        setEmpresasUtilizador(empresas);
        
        // Selecionar primeira empresa como padrão
        if (empresas.length > 0) {
          setNomeEmpresa(empresas[0]);
        }
      }
    } catch (erro) {
      console.error("Erro ao buscar informações do utilizador:", erro);
      setPopupErro({
        mostrar: true,
        mensagem: "Erro ao obter informações do utilizador.",
        tipo: "erro"
      });
    }
  };

  // Carregar todos os controlos da checklist
  const obterControlosChecklist = async () => {
    try {
      setACarregar(true);
      const resposta = await axios.get("http://localhost:8082/api/checklist");
      setControlosChecklist(resposta.data);
    } catch (erro) {
      console.error("Erro ao carregar a checklist:", erro);
      setPopupErro({
        mostrar: true,
        mensagem: "Erro ao carregar a checklist.",
        tipo: "erro"
      });
    } finally {
      setACarregar(false);
    }
  };

  // Atualizar o estado de conformidade
  const marcarConformidade = async (id) => {
    const controlo = controlosChecklist.find((controlo) => controlo.id === id);
    
    if (!controlo || controlo.isHeader) return;
    
    // Preparar objeto para atualização
    const controloAtualizado = {
      controlos: controlo.controlos,
      fases_implementacao: controlo.fase_implementacao,
      tarefas: controlo.tarefas,
      emConformidade: !controlo.emConformidade,
      notas: controlo.notas,
      ai_notas: controlo.ai_notas,
      isHeader: controlo.isHeader,
      displayOrder: controlo.displayorder
    };
    
    try {
      const resposta = await axios.put(`http://localhost:8082/api/checklist/${id}`, controloAtualizado);
      
      // Atualizar a lista
      setControlosChecklist(controlosAnteriores =>
        controlosAnteriores.map(controloAnterior => 
          controloAnterior.id === id ? resposta.data : controloAnterior
        )
      );
    } catch (erro) {
      console.error("Erro ao atualizar o controlo:", erro);
      setPopupErro({
        mostrar: true,
        mensagem: "Não foi possível atualizar o estado de conformidade.",
        tipo: "erro"
      });
    }
  };

  // Função para extrair o texto de recomendação do JSON
  const extrairRecomendacao = (texto) => {
    if (!texto) return null;
    
    try {
      // Se o texto já começa com "Nota:", deve estar limpo
      if (texto.trim().startsWith("Nota:")) {
        return texto;
      }
      
      // Verificar se o texto contém JSON
      if (texto.includes('"candidates":')) {
        try {
          // Tentar extrair usando expressão regular para capturar o texto dentro de "text": "..."
          const regex = /"text":\s*"([^"]*)"/;
          const correspondencia = texto.match(regex);
          
          if (correspondencia && correspondencia[1]) {
            return correspondencia[1].replace(/\\n/g, "\n").replace(/\\"/g, '"');
          }
          
          // Se falhar com regex, tentar outro método
          const inicioJson = texto.indexOf('{');
          if (inicioJson >= 0) {
            // Tentar extrair do JSON completo
            const strJson = texto.substring(inicioJson);
            try {
              const json = JSON.parse(strJson);
              if (json.candidates && 
                  json.candidates[0] && 
                  json.candidates[0].content && 
                  json.candidates[0].content.parts && 
                  json.candidates[0].content.parts[0] && 
                  json.candidates[0].content.parts[0].text) {
                return json.candidates[0].content.parts[0].text;
              }
            } catch (e) {
              console.error("Erro ao analisar JSON:", e);
            }
          }
        } catch (e) {
          console.error("Erro ao extrair texto da resposta:", e);
        }
      }
      
      // Procurar por "Nota:" no texto
      const indiceNota = texto.indexOf("Nota:");
      if (indiceNota >= 0) {
        return texto.substring(indiceNota);
      }
      
      // Se não conseguir extrair, retornar o texto original
      return texto;
    } catch (e) {
      console.error("Erro ao extrair recomendação:", e);
      return texto;
    }
  };

  // Formatar o texto da recomendação para exibição
  const formatarRecomendacao = (texto, estaConforme) => {
    if (!texto) return null;
    
    // Extrair apenas a nota e a sugestão do texto completo
    const textoExtraido = extrairRecomendacao(texto);
    
    // Formatar o texto para HTML com cores específicas baseadas no estado de conformidade
    let textoFormatado = textoExtraido;
    
    if (estaConforme) {
      textoFormatado = textoFormatado.replace(/Nota: Conforme\./gi, '<span class="conformidade conforme">Nota: Conforme.</span>');
      textoFormatado = textoFormatado.replace(/Nota: Não Conforme\./gi, '<span class="conformidade conforme">Nota: Conforme.</span>');
    } else {
      textoFormatado = textoFormatado.replace(/Nota: Conforme\./gi, '<span class="conformidade nao-conforme">Nota: Não Conforme.</span>');
      textoFormatado = textoFormatado.replace(/Nota: Não Conforme\./gi, '<span class="conformidade nao-conforme">Nota: Não Conforme.</span>');
    }
    
    // Formatar as sugestões
    textoFormatado = textoFormatado
      .replace(/Recomendação:/gi, '<br/><span class="sugestao-titulo">Recomendação:</span>')
      .replace(/Sugestão:/gi, '<br/><span class="sugestao-titulo">Sugestão:</span>');
    
    return textoFormatado;
  };

  // Criar e download PDF
  const downloadPDF = async () => {
    try {
      if (!nomeEmpresa) {
        setPopupErro({
          mostrar: true,
          mensagem: "Selecione uma empresa antes de criar o relatório.",
          tipo: "erro"
        });
        return;
      }
      
      setAExportar(true);
      
      // Preparar controlos com recomendações limpas para o PDF
      const controlosParaExportar = controlosChecklist.map(controlo => {
        // Criar uma cópia do controlo para não modificar o original
        const controloLimpo = {
          controlos: controlo.controlos,
          fases_implementacao: controlo.fase_implementacao, 
          tarefas: controlo.tarefas,
          emConformidade: controlo.emConformidade,
          notas: controlo.notas,
          isHeader: controlo.isHeader,
          displayOrder: controlo.displayorder || 0
        };
        
        // Limpar o texto da recomendação, se existir
        if (controlo.ai_notas || controlo.aiNotas) {
          const textoOriginal = controlo.ai_notas || controlo.aiNotas;
          const textoLimpo = extrairRecomendacao(textoOriginal);
          controloLimpo.ai_notas = textoLimpo;
        }
        
        return controloLimpo;
      });
      
      const dadosExportacao = {
        checklistItems: controlosParaExportar, // Enviar a lista processada
        companyName: nomeEmpresa || "Empresa",
        assessmentDate: dataAvaliacao || new Date().toISOString().split("T")[0],
        userId: idUtilizador
      };
      
      console.log("Enviando dados para exportação:", dadosExportacao);
      
      const resposta = await axios.post(
        `http://localhost:8082/api/checklist/export-pdf`, 
        dadosExportacao, 
        { responseType: 'blob' }
      );
      
      // Criar URL e iniciar download
      const url = window.URL.createObjectURL(new Blob([resposta.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `iso27001-checklist-${nomeEmpresa}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
      setPopupErro({
        mostrar: true,
        mensagem: "Relatório criado com sucesso e guardado no seu perfil!",
        tipo: "success"
      });
    } catch (erro) {
      console.error("Erro ao criar o PDF:", erro);
      setPopupErro({
        mostrar: true,
        mensagem: "Não foi possível criar o PDF",
        tipo: "erro"
      });
    } finally {
      setAExportar(false);
    }
  };

  // Renderização condicional para diferentes estados
  if (mostrarMensagemAutenticacao) {
    return (
      <div className="auth-message-container">
        <div className="auth-message">
          <h2>Acesso Restrito</h2>
          <p>Para poder utilizar a nossa checklist do Anexo A da norma ISO 27001 precisa de efetuar login</p>
          <p>Vai ser redirecionado para a página de login, caso não tenha conta crie uma!</p>
          <div className="loading-spinner"></div>
        </div>
      </div>
    );
  }

  if (aCarregar && estaAutenticado) {
    return (
      <div className="p-4 flex justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (!estaAutenticado) {
    return null;
  }

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h2 className="text-xl font-bold mb-4">ISO 27001 Checklist</h2>
      
      <div className="mb-4 p-4 border rounded bg-gray-50">
        <div className="flex flex-col md:flex-row md:space-x-4 space-y-2 md:space-y-0">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome da Empresa
            </label>
            {empresasUtilizador.length > 0 ? (
              <select
                value={nomeEmpresa}
                onChange={(e) => setNomeEmpresa(e.target.value)}
                className="w-full p-2 border rounded"
              >
                {empresasUtilizador.map((empresa, index) => (
                  <option key={index} value={empresa}>
                    {empresa}
                  </option>
                ))}
              </select>
            ) : (
              <div className="w-full p-2 border rounded bg-gray-100 text-gray-500">
                Nenhuma empresa registada. Adicione empresas no seu perfil.
              </div>
            )}
          </div>
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Data da Avaliação
            </label>
            <input
              type="date"
              value={dataAvaliacao}
              onChange={(e) => setDataAvaliacao(e.target.value)}
              className="w-full p-2 border rounded"
            />
          </div>
        </div>
        
        <div className="mt-4">
          <button
            onClick={downloadPDF}
            disabled={aExportar || empresasUtilizador.length === 0}
            className={`btn-edit ${
              (aExportar || empresasUtilizador.length === 0) ? "opacity-70 cursor-not-allowed" : ""
            }`}
          >
            <FaDownload className="btn-icon" /> Download Checklist (PDF)
          </button>
        </div>
      </div>
      
      
      <div className="overflow-x-auto">
        <table id="checklist-table" className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-header text-white">
              <th className="border p-2">CONTROLOS ISO 27001</th>
              <th className="border p-2">FASES DE IMPLEMENTAÇÃO</th>
              <th className="border p-2">TAREFAS</th>
              <th className="border p-2">EM CONFORMIDADE?</th>
              <th className="border p-2">RECOMENDAÇÕES/NOTAS</th>
            </tr>
          </thead>
          <tbody>
            {controlosChecklist.map((controlo) => (
              <tr
                key={controlo.id}
                className={`border ${controlo.isHeader ? "bg-gray-100" : ""}`}
              >
                {controlo.isHeader ? (
                  <td colSpan={5} className="border p-2 text-center text-gray-600">
                    <strong>{controlo.tarefas}</strong>
                  </td>
                ) : (
                  <>
                    <td className="border p-2 text-center">{controlo.controlos || "-"}</td>
                    <td className="border p-2">{controlo.fase_implementacao || "-"}</td>
                    <td className="border p-2">{controlo.tarefas || "-"}</td>
                    <td className="border p-2 text-center">
                      <button
                        onClick={() => marcarConformidade(controlo.id)}
                        className={`checkbox-status ${
                          controlo.emConformidade
                            ? "checkbox-conforme"
                            : "checkbox-nao-conforme"
                        }`}
                        aria-label={
                          controlo.emConformidade
                            ? "Marcar como não conforme"
                            : "Marcar como conforme"
                        }
                      >
                        {controlo.emConformidade ? "✓" : ""}
                      </button>
                    </td>
                    <td className="border p-2">
                      {(controlo.ai_notas || controlo.aiNotas) && (
                        <div 
                          className="text-sm" 
                          dangerouslySetInnerHTML={{ __html: formatarRecomendacao(controlo.ai_notas || controlo.aiNotas, controlo.emConformidade) }}
                        />
                      )}
                      
                      
                      {controlo.notas && (
                        <div className="mt-2 text-sm italic text-gray-700">
                          {controlo.notas}
                        </div>
                      )}
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      
      <ErrorPopup 
        message={popupErro.mensagem}
        show={popupErro.mostrar}
        onClose={fecharPopupErro}
        type={popupErro.tipo}
      />
    </div>
  );
};

export default Relatorio;