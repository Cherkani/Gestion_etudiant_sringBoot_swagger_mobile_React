import * as React from "react";
import axios from "axios";
import { useState, useEffect } from "react";
import Modal from "react-modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./role.css";

const Roles = () => {
  const [roles, setRoles] = useState([]);
  const [role, setRole] = useState({
    id: "",
    name: "",
  });
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [modal, setModal] = useState(false);
  const [id, setId] = useState(null);

  const url = "http://localhost:8088/api/v1/roles";

  const fetchRoles = async () => {
    setLoading(true);
    try {
      const response = await axios.get(url);
      setRoles(response.data);
    } catch (error) {
      console.error("Error fetching roles:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRoles();
  }, []);

  const handleRole = (e) => {
    setRole({ ...role, [e.target.name]: e.target.value });
  };

  const handleUpdate = (id, name) => {
    setUpdateMode(true);
    setRole({ id: id, name: name });
  };

  const addRole = async () => {
    try {
      await axios.post(url, role);
      fetchRoles();
      setRole({ id: "", name: "" });
      notify("added");
    } catch (error) {
      console.error("Error adding role:", error);
    }
  };

  const updateRole = async () => {
    try {
      await axios.put(`${url}/updateRole/${role.id}`, role);
      fetchRoles();
      setRole({ id: "", name: "" });
      setUpdateMode(false);
      notify("updated");
    } catch (error) {
      console.error("Error updating role:", error);
    }
  };

  const deleteRole = async () => {
    try {
      await axios.delete(`${url}/deleteRole/${id}`);
      fetchRoles();
      setRole({ id: "", name: "" });
      setUpdateMode(false);
      closeModal();
      notify("deleted");
    } catch (error) {
      console.error("Error deleting role:", error);
    }
  };

  const showModal = (id) => {
    setId(id);
    setModal(true);
  };

  const closeModal = () => {
    setModal(false);
  };

  const notify = (op) =>
    toast.success(`Role ${op} successfully`, {
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
    <div>
      <div className="container">
        <div className="role-container">
          <p className="role-header">Gestion de Role </p>
          <div className="role-input-container">
            <input
              placeholder="Entrer votre role"
              name="name"
              onChange={handleRole}
              value={role.name}
              className="role-input"
            />
            {!updateMode ? (
              <button className="add-button" onClick={addRole}>
                Ajouter
              </button>
            ) : (
              <button className="update-button" onClick={updateRole}>
                Modifier
              </button>
            )}
          </div>

          {loading ? (
            <p>Loading data</p>
          ) : (
            <div>
              <div className="data-container">
                <p className="data-header">Id</p>
                <p className="data-header">Nom</p>
                <p className="data-header">Action</p>
              </div>
              {roles.map((role, index) => (
                <div key={index} className="data-row">
                  <p>{role.id}</p>
                  <p>{role.name}</p>
                  <div className="action-buttons">
                    <button
                      className="button update-button"
                      onClick={() => handleUpdate(role.id, role.name)}
                    >
                      Modifier
                    </button>
                    <button
                      className="button delete-button"
                      onClick={() => showModal(role.id)}
                    >
                      Supprimer
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
        <Modal
          className="modal-content"
          isOpen={modal}
          onRequestClose={closeModal}
        >
          <button onClick={closeModal}>X</button>
          <p>es tu sur de supprimer ce role?</p>
          <div className="action-buttons">
            <button onClick={closeModal} className="cancel-button">
              Cancel
            </button>
            <button onClick={deleteRole} className="delete-button">
              Supprimer
            </button>
          </div>
        </Modal>
      </div>
    </div>
  );
};

export default Roles;
