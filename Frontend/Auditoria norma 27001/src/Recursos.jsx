import React, { useState, useEffect } from 'react';
import {FaRobot, FaFilePdf, FaFileExcel, FaFileWord, FaGraduationCap, FaUserGraduate, FaUsers, FaPlay, FaSearch, FaVideo, FaFile } from 'react-icons/fa';
import Chat from "./assets/Componentes/Chat";
import './Recursos.css';
import ErrorPopup from "./assets/Componentes/ErrorPopup"; 


const Recursos = () => {

  const [abaAtiva, setAbaAtiva] = useState('documentos');
  const [chatAberto, setChatAberto] = useState(false);
  const [documentos, setDocumentos] = useState([]);
  const [videos, setVideos] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [aCarregar, setACarregar] = useState(false);
  const [errorPopup, setErrorPopup] = useState({ show: false, message: "", type: "error" });
  const [termoPesquisa, setTermoPesquisa] = useState('');
  const [resultadosPesquisa, setResultadosPesquisa] = useState([]);
  const [pesquisaAtiva, setPesquisaAtiva] = useState(false);


  useEffect(() => {
    if (abaAtiva === 'documentos') {
      procurarDocumentos();
    } else if (abaAtiva === 'videos') {
      procurarVideos();
    } else if (abaAtiva === 'cursos') {
      procurarCursos();
    }
  }, [abaAtiva]);


  const closeErrorPopup = () => {
    setErrorPopup({ ...errorPopup, show: false });
  };

   //Função para procurar documentos da API
  const procurarDocumentos = async () => {
    setACarregar(true);
    try {
      const resposta = await fetch('http://localhost:8082/api/files');
      if (!resposta.ok) {
        throw new Error('Falha ao carregar documentos');
      }
      const dados = await resposta.json();
      setDocumentos(dados);
    } catch (err) {
      console.error('Erro ao procurar documentos:', err);
      setErrorPopup({
        show: true,
        message: 'Não foi possível carregar os documentos.',
        type: 'error'
      });
    } finally {
      setACarregar(false);
    }
  };

  
  //Função para procurar vídeos da API
  const procurarVideos = async () => {
    setACarregar(true);
    try {
      const resposta = await fetch('http://localhost:8082/api/videos');
      if (!resposta.ok) {
        throw new Error('Falha ao carregar vídeos');
      }
      const dados = await resposta.json();
      setVideos(dados);
    } catch (err) {
      console.error('Erro ao procurar vídeos:', err);
      setErrorPopup({
        show: true,
        message: 'Não foi possível carregar os vídeos.',
        type: 'error'
      });
    } finally {
      setACarregar(false);
    }
  };
  
  //Função para procurar cursos da API

  const procurarCursos = async () => {
    setACarregar(true);
    try {
      const resposta = await fetch('http://localhost:8082/api/cursos');
      if (!resposta.ok) {
        throw new Error('Falha ao carregar cursos');
      }
      const dados = await resposta.json();
      setCursos(dados);
    } catch (err) {
      console.error('Erro ao procurar cursos:', err);
      setErrorPopup({
        show: true,
        message: 'Não foi possível carregar os cursos.',
        type: 'error'
      });
    } finally {
      setACarregar(false);
    }
  };


   //Função para pesquisar recursos usando a API
   //Procura em todos os tipos de recursos (documentos, vídeos e cursos)
  const pesquisarRecursos = async () => {
    if (!termoPesquisa.trim()) {
      setPesquisaAtiva(false);
      return;
    }
    
    setACarregar(true);
    setPesquisaAtiva(true);
    
    try {
      
      const resposta = await fetch(`http://localhost:8082/api/search?termo=${encodeURIComponent(termoPesquisa)}`);
      if (!resposta.ok) {
        throw new Error('Falha ao realizar pesquisa');
      }
      const dados = await resposta.json();
      setResultadosPesquisa(dados);
    } catch (err) {
      console.error('Erro ao realizar pesquisa:', err);
      setErrorPopup({
        show: true,
        message: 'Não foi possível realizar a pesquisa.',
        type: 'error'
      });
      setResultadosPesquisa([]);
    } finally {
      setACarregar(false);
    }
  };

  
  //Determina o icon apropriado com base no tipo de ficheiro
   
  const obtericonFicheiro = (tipoFicheiro) => {
    if (tipoFicheiro && tipoFicheiro.includes('pdf')) return 'file-pdf';
    if (tipoFicheiro && (tipoFicheiro.includes('excel') || tipoFicheiro.includes('sheet'))) return 'file-excel';
    if (tipoFicheiro && (tipoFicheiro.includes('word') || tipoFicheiro.includes('document'))) return 'file-word';
    return 'file'; 
  };

  
  //Converte bytes para um formato legível (KB, MB, GB)
   
  const formatarTamanhoFicheiro = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const tamanhos = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + tamanhos[i];
  };

  
  //Atribuir icons a variaveis
  const mapaicons = {
    'file-pdf': FaFilePdf,
    'file-excel': FaFileExcel,
    'file-word': FaFileWord,
    'graduation-cap': FaGraduationCap,
    'user-graduate': FaUserGraduate,
    'users-class': FaUsers,
    'play': FaPlay,
    'search': FaSearch,
    'video': FaVideo,
    'file': FaFile  
  };

  //Renderiza um ícon com base no nome
  
  const renderizaricon = (nomeicon, classeCSS = '', tamanho = 24) => {
    const Componenteicon = mapaicons[nomeicon] || FaFile;
    return <Componenteicon className={classeCSS} size={tamanho} />;
  };

  //Alterna a visibilidade do chat
  const alternarChat = () => {
    setChatAberto(!chatAberto);
  };

  
  //Função para lidar com a submissão do formulário de pesquisa
  const submeterPesquisa = (evento) => {
    evento.preventDefault();
    pesquisarRecursos();
  };

  
  //Renderiza os resultados da pesquisa
  const renderizarResultadosPesquisa = () => {
    if (!pesquisaAtiva) return null;

    return (
      <div className="resultados-pesquisa">
        <h2>Resultados da pesquisa para: "{termoPesquisa}"</h2>
        {aCarregar && <div className="mensagem-carregamento">A procurar resultados...</div>}
        
        {!aCarregar && resultadosPesquisa.length === 0 && (
          <div className="mensagem-vazia">Nenhum resultado encontrado para a sua pesquisa.</div>
        )}
        
        <div className="recursos-grelha">
          {!aCarregar && resultadosPesquisa.map((item, index) => {
            // Determina o tipo de item e renderiza adequadamente
            if (item.tipoRecurso === 'documento') {
              return renderizarCardDocumento(item, index);
            } else if (item.tipoRecurso === 'video') {
              return renderizarCardVideo(item, index);
            } else if (item.tipoRecurso === 'curso') {
              return renderizarCardCurso(item, index);
            }
            return null;
          })}
        </div>
        
        <button 
          className="botao-voltar"
          onClick={() => {
            setPesquisaAtiva(false);
            setTermoPesquisa('');
          }}
        >
          Voltar aos recursos
        </button>
      </div>
    );
  };

  //Renderiza um card para os documentos
const renderizarCardDocumento = (doc, index) => (
  <div className="recurso-card" key={`doc-${doc.id || index}`}>
    <div className="recurso-card-cabecalho">
      {renderizaricon(obtericonFicheiro(doc.tipoFicheiro || doc.fileType))}
      <span className="recurso-tipo">{doc.tipoFicheiro || doc.fileType}</span>
    </div>
    <h3>{doc.nomeFicheiro || doc.fileName || doc.titulo}</h3>
    <p>{doc.descricao || "Documento disponibilizado para consulta"}</p>
    <div className="recurso-card-rodape">
      <span className="recurso-tamanho">{formatarTamanhoFicheiro(doc.tamanho || doc.size)}</span>
      <div className="recurso-acoes">
        <a 
          href={doc.url} 
          className="botao-download" 
          download
          onClick={(e) => {
            if (!doc.url) {
              e.preventDefault();
              setErrorPopup({
                show: true,
                message: 'O link de download não está disponível neste momento.',
                type: 'error'
              });
            }
          }}
        >
          Download
        </a>
        <a 
          href={doc.urlVisualizacao || doc.viewUrl} 
          className="botao-visualizar" 
          target="_blank" 
          rel="noopener noreferrer"
          onClick={(e) => {
            if (!doc.urlVisualizacao && !doc.viewUrl) {
              e.preventDefault();
              setErrorPopup({
                show: true,
                message: 'A visualização não está disponível neste momento.',
                type: 'error'
              });
            }
          }}
        >
          Visualizar
        </a>
      </div>
    </div>
  </div>
);
  
  //Renderiza um card para os videos
  const renderizarCardVideo = (video, index) => (
    <div className="recurso-card video-card" key={`video-${video.id || index}`}>
      <div className="video-thumbnail">
        <img src={video.thumbnail} alt={video.titulo} />
        <span className="video-duracao">{video.duracao}</span>
        <a 
          href={video.urlVideo || video.videoURL} 
          target="_blank" 
          rel="noopener noreferrer" 
          className="botao-play"
          onClick={(e) => {
            if (!video.urlVideo && !video.videoURL) {
              e.preventDefault();
              setErrorPopup({
                show: true,
                message: 'O vídeo não está disponível neste momento.',
                type: 'error'
              });
            }
          }}
        >
          {renderizaricon('play')}
        </a>
      </div>
      <h3>{video.titulo}</h3>
      <p>{video.descricao}</p>
      <a 
        href={video.urlVideo || video.videoURL} 
        className="botao-assistir" 
        target="_blank" 
        rel="noopener noreferrer"
        onClick={(e) => {
          if (!video.urlVideo && !video.videoURL) {
            e.preventDefault();
            setErrorPopup({
              show: true,
              message: 'O vídeo não está disponível neste momento.',
              type: 'error'
            });
          }
        }}
      >
        Ver vídeo
      </a>
    </div>
  );
  

  //Renderiza um card para os cursos
  const renderizarCardCurso = (curso, index) => (
    <div className="recurso-card" key={`curso-${curso.id || index}`}>
      <div className="recurso-card-cabecalho formacao-cabecalho">
        {renderizaricon(curso.icon || 'graduation-cap')}
        <span className="recurso-tipo">{curso.modalidade}</span>
      </div>
      <h3>{curso.titulo}</h3>
      <p>{curso.descricao}</p>
      <div className="recurso-card-rodape">
        <span className="recurso-duracao">{curso.duracao}</span>
        <a 
          href={curso.url} 
          className="botao-inscricao" 
          target="_blank" 
          rel="noopener noreferrer"
          onClick={(e) => {
            if (!curso.url) {
              e.preventDefault();
              setErrorPopup({
                show: true,
                message: 'O link do curso não está disponível neste momento.',
                type: 'error'
              });
            }
          }}
        >
          Saber mais
        </a>
      </div>
    </div>
  );

  
  //Renderiza o conteúdo principal com base na aba ativa
  const renderizarConteudo = () => {
    // Se a pesquisa estiver ativa, exibe os resultados da pesquisa
    if (pesquisaAtiva) {
      return renderizarResultadosPesquisa();
    }
    
    // Caso contrário, mostra o conteúdo da aba selecionada
    switch (abaAtiva) {
      case 'documentos':
        return (
          <div className="recursos-grelha">
            {aCarregar && <div className="mensagem-carregamento">A carregar documentos...</div>}
            
            {!aCarregar && documentos.length === 0 && (
              <div className="mensagem-vazia">Nenhum documento disponível de momento.</div>
            )}

            {!aCarregar && documentos.map((doc, index) => renderizarCardDocumento(doc, index))}
          </div>
        );
      case 'videos':
        return (
          <div className="recursos-grelha videos-grelha">
            {aCarregar && <div className="mensagem-carregamento">A carregar vídeos...</div>}
            
            {!aCarregar && videos.length === 0 && (
              <div className="mensagem-vazia">Nenhum vídeo disponível de momento.</div>
            )}
            
            {!aCarregar && videos.map((video, index) => renderizarCardVideo(video, index))}
          </div>
        );
      case 'cursos':
        return (
          <div className="recursos-grelha">
            {aCarregar && <div className="mensagem-carregamento">A carregar cursos...</div>}
            
            {!aCarregar && cursos.length === 0 && (
              <div className="mensagem-vazia">Nenhum curso disponível de momento.</div>
            )}
            
            {!aCarregar && cursos.map((curso, index) => renderizarCardCurso(curso, index))}
          </div>
        );
      default:
        return <div>Selecione uma categoria para ver os recursos disponíveis</div>;
    }
  };

  return (
    <div className="recursos-contentor">
      <div className="recursos-cabecalho">
        <h1>Recursos ISO 27001</h1>
        <p className="recursos-intro">
          Bem-vindo à nossa biblioteca de recursos sobre a norma ISO 27001. Aqui encontrará documentos, 
          vídeos e cursos para auxiliar na implementação e manutenção do seu Sistema de 
          Gestão de Segurança da Informação (SGSI).
        </p>
      </div>

      <div className="recursos-abas">
        <button 
          className={`aba ${abaAtiva === 'documentos' ? 'ativa' : ''}`}
          onClick={() => setAbaAtiva('documentos')}
        >
          {renderizaricon('file')} Documentos
        </button>
        <button 
          className={`aba ${abaAtiva === 'videos' ? 'ativa' : ''}`}
          onClick={() => setAbaAtiva('videos')}
        >
          {renderizaricon('video')} Vídeos
        </button>
        <button 
          className={`aba ${abaAtiva === 'cursos' ? 'ativa' : ''}`}
          onClick={() => setAbaAtiva('cursos')}
        >
          {renderizaricon('graduation-cap')} Cursos
        </button>
      </div>

      <div className="recursos-conteudo">
        <form className="barra-pesquisa" onSubmit={submeterPesquisa}>
          <input 
            type="text" 
            placeholder="Pesquisar recursos..." 
            value={termoPesquisa}
            onChange={(e) => setTermoPesquisa(e.target.value)}
          />
          <button type="submit" className="botao-pesquisa">{renderizaricon('search')}</button>
        </form>
        
        {renderizarConteudo()}
      </div>

   
      <button 
        className="botao-chat"
        onClick={alternarChat}
      >
        <FaRobot size={30} />
      </button>

      {chatAberto && <Chat />}
      
    
      <ErrorPopup 
        message={errorPopup.message}
        show={errorPopup.show}
        onClose={closeErrorPopup}
        type={errorPopup.type}
      />
    </div>
  );
};

export default Recursos;