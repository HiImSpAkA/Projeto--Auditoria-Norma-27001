import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaHome, FaUserCircle, FaChartBar, FaSignInAlt, FaBook, FaSignOutAlt } from 'react-icons/fa';
import './navbar.css';

const Navbar = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    // Verificar se o utilizador está logado
    const checkLoginStatus = () => {
      const userEmail = localStorage.getItem('userEmail');
      setIsLoggedIn(!!userEmail);
    };

    // Verificar ao montar o componente
    checkLoginStatus();

    // Adicionar event listener para mudanças no localStorage
    window.addEventListener('storage', checkLoginStatus);

    // Limpar event listener
    return () => {
      window.removeEventListener('storage', checkLoginStatus);
    };
  }, []);

  const handleLogout = () => {
    // Remover dados de autenticação
    localStorage.removeItem('userEmail');
    
    // Atualizar estado
    setIsLoggedIn(false);
    
    // Redirecionar para home
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-link">
          <FaHome size={20} className="navbar-icon" />
          Home
        </Link>
      </div>

      <div className="navbar-right">
        <Link to="/recursos" className="navbar-link">
          <FaBook size={20} className="navbar-icon" />
          Recursos
        </Link>
        <Link to="/relatorio" className="navbar-link">
          <FaChartBar size={20} className="navbar-icon" />
          Relatório
        </Link>
        
        {isLoggedIn ? (
          // Links mostrados quando o utilizador está logado
          <>
            <Link to="/perfil" className="navbar-link">
              <FaUserCircle size={20} className="navbar-icon" />
              Perfil
            </Link>
            <a href="#" className="navbar-link" onClick={handleLogout}>
              <FaSignOutAlt size={20} className="navbar-icon" />
              Logout
            </a>
          </>
        ) : (
          // Link mostrado quando o utilizador não está logado
          <Link to="/login" className="navbar-link">
            <FaSignInAlt size={20} className="navbar-icon" />
            Login
          </Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar;