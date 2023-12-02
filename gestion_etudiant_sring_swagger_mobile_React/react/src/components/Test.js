import React from "react";
import axios from "axios";
import { useEffect, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPen, faTrash, faPlus, faX } from "@fortawesome/free-solid-svg-icons";
import Modal from "react-modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./role.css";

const Test = () => {
  const [roles, setRoles] = useState([]);
  const [role, setRole] = useState({
    id: "",
    name: "",
  });
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const url = "http://localhost:8080/api/v1/roles";

  const fetchRoles = async () => {
    setLoading(true);
    const response = await axios.get(url);
    setRoles(response.data);
    setLoading(false);
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
      const response = await axios.post(url, role);
      fetchRoles();
      setRole({
        id: "",
        name: "",
      });
      notify("added");
    } catch (error) {
      console.log(error);
    }
  };

  const updateRole = async () => {
    try {
      const response = await axios.put(`${url}/${role.id}`, role);
      fetchRoles();
      setRole({
        id: "",
        name: "",
      });
      setUpdateMode(false);
      notify("updated");
    } catch (error) {
      console.log(error);
    }
  };

  const deleteRole = async () => {
    try {
      const response = await axios.delete(`${url}/${id}`);
      fetchRoles();
      setRole({
        id: "",
        name: "",
      });
      setUpdateMode(false);
      closeModal();
      notify("deleted");
    } catch (error) {
      console.log(error);
    }
  };

  const [modal, setModal] = useState(false);
  const [id, setId] = useState(null);

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
        {" "}
        {/* Apply container class here */}
        <div className="role-container">
          <p className="role-header">Role management</p>
          <div className="role-input-container">
            <input
              placeholder="Enter role name"
              name="name"
              onChange={handleRole}
              value={role.name}
              className="role-input"
            />
            {!updateMode ? (
              <button className="add-button" onClick={addRole}>
                Add
              </button>
            ) : (
              <button className="update-button" onClick={updateRole}>
                Update
              </button>
            )}
          </div>

          {loading ? (
            <p>Loading data</p>
          ) : (
            <div>
              <div className="data-container">
                <p className="data-header">Id</p>
                <p className="data-header">Name</p>
                <p className="data-header">Action</p>
              </div>
              {roles.map((role, index) => (
                <div key={index} className="data-row">
                  <p>{role.id}</p>
                  <p>{role.name}</p>
                  <div className="action-buttons">
                    <button onClick={() => handleUpdate(role.id, role.name)}>
                      <FontAwesomeIcon
                        icon={faPen}
                        className="mr-4 hover:text-indigo-500 hover:scale-125"
                      />
                    </button>
                    <button onClick={() => showModal(role.id)}>
                      <FontAwesomeIcon
                        icon={faTrash}
                        className="mr-4 hover:text-red-500 hover:scale-125"
                      />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
        <Modal isOpen={modal} onRequestClose={closeModal}>
          <button onClick={closeModal}>
            <FontAwesomeIcon icon={faX} />
          </button>
          <p>Do you want to delete this role?</p>
          <div className="action-buttons">
            <button onClick={closeModal} className="cancel-button">
              Cancel
            </button>
            <button onClick={deleteRole} className="delete-button">
              Delete
            </button>
          </div>
        </Modal>
      </div>
    </div>
  );
};

export default Test;
