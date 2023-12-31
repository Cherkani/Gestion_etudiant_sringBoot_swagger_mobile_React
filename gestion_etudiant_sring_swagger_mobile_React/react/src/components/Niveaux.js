import React, { useEffect, useState } from "react";
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPen,
  faTrash,
  faPlus,
  faUserGraduate,
  faTimes,
} from "@fortawesome/free-solid-svg-icons";
import "./niveau.css";
import Modal from "react-modal";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Niveaux = () => {
  const [majors, setMajors] = useState([]);
  const [major, setMajor] = useState({
    id: "",
    code: "",
    name: "",
  });
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [idToDelete, setIdToDelete] = useState(null);
  const url = "http://localhost:8088/api/v1/filieres";

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

  const handleUpdate = (id, code, name) => {
    setUpdateMode(true);
    setMajor({ id, code, name });
  };

  const addMajor = async () => {
    try {
      await axios.post(url, major);
      fetchMajors();
      setMajor({ id: "", code: "", name: "" });
      notify("added");
    } catch (error) {
      console.error("Error adding major:", error);
    }
  };

  const updateMajor = async () => {
    try {
      await axios.put(`${url}/updateFiliere/${major.id}`, major);
      fetchMajors();
      setMajor({ id: "", code: "", name: "" });
      setUpdateMode(false);
      notify("updated");
    } catch (error) {
      console.error("Error updating major:", error);
    }
  };

  const deleteMajor = async () => {
    try {
      console.log("Deleting major with ID:", idToDelete);
      await axios.delete(`${url}/deleteFiliere/${idToDelete}`);
      fetchMajors();
      setMajor({ id: "", code: "", name: "" });
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
            name="name"
            onChange={handleMajor}
            value={major.name}
          />
          {!updateMode ? (
            <button className="add-button" onClick={addMajor}>
              Ajouter
            </button>
          ) : (
            <button className="update-button" onClick={updateMajor}>
              Modifier
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
                  <p>{major.name}</p>
                  <div className="action-buttons">
                    <button
                      className="update-button"
                      onClick={() =>
                        handleUpdate(major.id, major.code, major.name)
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
        <button onClick={closeModal}>
          <FontAwesomeIcon icon={faTimes} />
        </button>
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

export default Niveaux;
