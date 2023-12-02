import React from "react";
import { Link } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import "./navigationBar.css";

function NavigationBar() {
  return (
    <Navbar
      collapseOnSelect
      expand="lg"
      style={{
        backgroundColor: "#fff8dc",
        color: "#333",
        boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
      }}
    >
      <Container>
        <Navbar.Brand to="/" style={{ fontSize: "36px" }}>
          Gestion-Etudiants
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="me-auto"></Nav>
          <Nav>
            <Nav.Link
              as={Link}
              to="/etudiants"
              style={{ fontSize: "25px", color: "#333" }}
            >
              Etudiants
            </Nav.Link>
            <Nav.Link
              as={Link}
              to="/niveaux"
              style={{ fontSize: "25px", color: "#333" }}
            >
              Niveaux
            </Nav.Link>
            <Nav.Link
              as={Link}
              to="/roles"
              style={{ fontSize: "25px", color: "#333" }}
            >
              Roles
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavigationBar;
