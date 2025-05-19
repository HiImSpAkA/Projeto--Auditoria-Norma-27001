import React from "react";
import { BrowserRouter as Router, Route, Routes, useLocation } from "react-router-dom";
import Navbar from "./assets/Componentes/navbar";
import Home from "./Home";  
import Login from "./Login";
import Register from "./Register";
import Recursos from "./Recursos";
import Relatorio from "./Relatorio";
import Perfil from "./Perfil";


const Layout = () => {
  const location = useLocation();
  const hideNavbar = location.pathname === "/register" || location.pathname === "/login";

  return (
    <div className={`App ${hideNavbar ? "no-navbar" : ""}`}>
      {!hideNavbar && <Navbar />}
      <div className="content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/relatorio" element={<Relatorio />} />
          <Route path="/perfil" element={<Perfil />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/recursos" element={<Recursos />}/>
        </Routes>
      </div>
    </div>
  );
};

const App = () => {
  return (
    <Router>
      <Layout />
    </Router>
  );
};

export default App;