// App.js
import React from "react";

import NavigationBar from "./components/NavigationBar";
import Etudiants from "./components/Etudiants";
import Niveaux from "./components/Niveaux";
import Roles from "./components/Roles";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

function App() {
  return (
    <Router>
      <NavigationBar />
      <Routes>
        <Route path="/etudiants" element={<Etudiants />} />
        <Route path="/niveaux" element={<Niveaux />} />
        <Route path="/roles" element={<Roles />} />
      </Routes>
    </Router>
  );
}

export default App;
