import React, { useEffect, useState } from "react";
import axios from "axios";
import "./filiere.css";
import Modal from "react-modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Filiere = () => {
  const [Filieres, setFilieres] = useState([]);
  const [Filiere, setFiliere] = useState({
    id: "",
    code: "",
    libelle: "",
  });
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [idToDelete, setIdToDelete] = useState(null);
  const url = "http://localhost:8080/api/v1/filieres";

  const fetchFilieres = async () => {
    setLoading(true);
    try {
      const response = await axios.get(url);
      setFilieres(response.data);
    } catch (error) {
      console.error("Error fetching Filieres:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFilieres();
  }, []);

  const handleFiliere = (e) => {
    setFiliere({ ...Filiere, [e.target.name]: e.target.value });
  };

  const handleUpdate = (id, code, libelle) => {
    setUpdateMode(true);
    setFiliere({ id, code, libelle });
  };

  const addFiliere = async () => {
    try {
      await axios.post(url, Filiere);
      fetchFilieres();
      setFiliere({ id: "", code: "", libelle: "" });
      notify("added");
    } catch (error) {
      console.error("Error adding Filiere:", error);
    }
  };

  const updateFiliere = async () => {
    try {
      await axios.put(`${url}/updateFiliere/${Filiere.id}`, Filiere);
      fetchFilieres();
      setFiliere({ id: "", code: "", libelle: "" });
      setUpdateMode(false);
      notify("updated");
    } catch (error) {
      console.error("Error updating Filiere:", error);
    }
  };

  const deleteFiliere = async () => {
    try {
      await axios.delete(`${url}/deleteFiliere/${idToDelete}`);
      fetchFilieres();
      setFiliere({ id: "", code: "", libelle: "" });
      setUpdateMode(false);
      notify("deleted");
      closeModal();
    } catch (error) {
      console.error("Error deleting Filiere:", error);
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
    toast.success(`Filiere ${op} successfully`, {
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
            onChange={handleFiliere}
            value={Filiere.code}
          />
          <input
            className="role-input"
            placeholder="Entrer votre nom"
            name="libelle"
            onChange={handleFiliere}
            value={Filiere.libelle}
          />
          {!updateMode ? (
            <button className="add-button" onClick={addFiliere}>
              Ajouter
            </button>
          ) : (
            <button className="update-button" onClick={updateFiliere}>
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
              {Filieres.map((Filiere, index) => (
                <div
                  className={`data-row ${
                    index % 2 === 0 ? "even-row" : "odd-row"
                  }`}
                  key={index}
                >
                  <p>{Filiere.id}</p>
                  <p>{Filiere.code}</p>
                  <p>{Filiere.libelle}</p>
                  <div className="action-buttons">
                    <button
                      className="update-button"
                      onClick={() =>
                        handleUpdate(Filiere.id, Filiere.code, Filiere.libelle)
                      }
                    >
                      Modifier
                    </button>
                    <button
                      className="delete-button"
                      onClick={() => showModal(Filiere.id)}
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
          <button onClick={deleteFiliere} className="delete-button">
            Supprimer
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default Filiere;
