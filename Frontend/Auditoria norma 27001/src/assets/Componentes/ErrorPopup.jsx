import React, { useEffect } from 'react';
import { FaTimes, FaExclamationTriangle } from 'react-icons/fa';
import './ErrorPopup.css';


//Componente de popup para exibir mensagens de erro

const ErrorPopup = ({ message, show, onClose, type = 'error' }) => {
  // Fechar o popup ao pressionar a tecla ESC
  useEffect(() => {
    const handleEscKey = (event) => {
      if (event.key === 'Escape' && show) {
        onClose();
      }
    };

    window.addEventListener('keydown', handleEscKey);
    
    // Configurar temporizador para fechar automaticamente após 5 segundos
    let timer;
    if (show) {
      timer = setTimeout(() => {
        onClose();
      }, 5000);
    }

    return () => {
      window.removeEventListener('keydown', handleEscKey);
      clearTimeout(timer);
    };
  }, [show, onClose]);

  if (!show) return null;

  return (
    <div className="popup-overlay">
      <div className={`popup-container ${type}`}>
        <div className="popup-header">
          {type === 'error' ? (
            <FaExclamationTriangle className="popup-icon" />
          ) : (
            <div className="success-icon">✓</div>
          )}
          <h3>{type === 'error' ? 'Erro' : 'Sucesso'}</h3>
          <button className="close-button" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        <div className="popup-content">
          <p>{message}</p>
        </div>
        <div className="popup-footer">
          <button className="confirm-button" onClick={onClose}>
            OK
          </button>
        </div>
      </div>
    </div>
  );
};

export default ErrorPopup;