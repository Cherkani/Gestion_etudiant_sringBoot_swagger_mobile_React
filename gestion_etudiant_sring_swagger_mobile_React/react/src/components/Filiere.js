import React, { useEffect, useState } from "react";
import axios from "axios";
import "./filiere.css";
import Modal from "react-modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Filiere = () => {
  const [majors, setMajors] = useState([]);
  const [major, setMajor] = useState({
    id: "",
    code: "",
    libelle: "",
  });
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [idToDelete, setIdToDelete] = useState(null);
  const url = "http://localhost:8080/api/v1/filieres";

  const fetchMajors = async () => {
    setLoading(true);
    try {
      const response = await axios.get(url);
      setMajors(response.data);
    } catch (error) {
      console.error("Error fetching majors:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMajors();
  }, []);

  const handleMajor = (e) => {
    setMajor({ ...major, [e.target.name]: e.target.value });
  };

  const handleUpdate = (id, code, libelle) => {
    setUpdateMode(true);
    setMajor({ id, code, libelle });
  };

  const addMajor = async () => {
    try {
      await axios.post(url, major);
      fetchMajors();
      setMajor({ id: "", code: "", libelle: "" });
      notify("added");
    } catch (error) {
      console.error("Error adding major:", error);
    }
  };

  const updateMajor = async () => {
    try {
      await axios.put(`${url}/updateFiliere/${major.id}`, major);
      fetchMajors();
      setMajor({ id: "", code: "", libelle: "" });
      setUpdateMode(false);
      notify("updated");
    } catch (error) {
      console.error("Error updating major:", error);
    }
  };

  const deleteMajor = async () => {
    try {
      await axios.delete(`${url}/deleteFiliere/${idToDelete}`);
      fetchMajors();
      setMajor({ id: "", code: "", libelle: "" });
      setUpdateMode(false);
      notify("deleted");
      closeModal();
    } catch (error) {
      console.error("Error deleting major:", error);
    }
  };

  const [modalIsOpen, setModalIsOpen] = useState(false);

  const showModal = (id) => {
    setIdToDelete(id);
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

  const notify = (op) =>
    toast.success(`Major ${op} successfully`, {
      position: "bottom-right",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "colored",
    });
  return (
    <div className="container">
      <div className="role-container">
        <p>
          <span></span>
          Gestion des niveaux
        </p>
        <div className="data-container">
          <input
            className="role-input"
            placeholder="Entrer votre niveau"
            name="code"
            onChange={handleMajor}
            value={major.code}
          />
          <input
            className="role-input"
            placeholder="Entrer votre nom"
            name="libelle"
            onChange={handleMajor}
            value={major.libelle}
          />
          {!updateMode ? (
            <button className="add-button" onClick={addMajor}>
              Ajouter
            </button>
          ) : (
            <button className="update-button" onClick={updateMajor}>
              <div>
                <p>Modifier</p>
              </div>
            </button>
          )}
        </div>

        {loading ? (
          <p>Loading data</p>
        ) : (
          <div>
            <div>
              <div className="data-row data-header">
                <p>Id</p>
                <p>Code</p>
                <p>Name</p>
                <p>Action</p>
              </div>
              {majors.map((major, index) => (
                <div
                  className={`data-row ${
                    index % 2 === 0 ? "even-row" : "odd-row"
                  }`}
                  key={index}
                >
                  <p>{major.id}</p>
                  <p>{major.code}</p>
                  <p>{major.libelle}</p>
                  <div className="action-buttons">
                    <button
                      className="update-button"
                      onClick={() =>
                        handleUpdate(major.id, major.code, major.libelle)
                      }
                    >
                      Modifier
                    </button>
                    <button
                      className="delete-button"
                      onClick={() => showModal(major.id)}
                    >
                      Supprimer
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
      <Modal
        className="modal-content"
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
      >
        <button onClick={closeModal}>X</button>
        <p>es tu sur de supprimer ce role?</p>
        <div className="action-buttons">
          <button onClick={closeModal} className="cancel-button">
            Cancel
          </button>
          <button onClick={deleteMajor} className="delete-button">
            Supprimer
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default Filiere;
