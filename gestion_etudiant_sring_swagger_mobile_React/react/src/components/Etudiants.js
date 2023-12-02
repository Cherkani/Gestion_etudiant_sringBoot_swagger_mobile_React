import React from "react";
import { useEffect, useState } from "react";
import axios from "axios";

import "primereact/resources/themes/lara-light-indigo/theme.css";
import Modal from "react-modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./etudiant.css";
const Etudiants = () => {
  const [students, setStudents] = useState([]);
  const [selectedRoles, setSelectedRoles] = useState([]);
  const [student, setStudent] = useState({
    id: "",
    roles: [],
    username: "",
    password: "",
    firstname: "",
    lastname: "",
    telephone: "",
    login: "",
    filiere: {
      id: "",
      code: "",
      name: "",
    },
  });
  const [roles, setRoles] = useState([]);
  const [majors, setMajors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [updateMode, setUpdateMode] = useState(false);
  const [id, setId] = useState(null);
  const url = "http://localhost:8088/api/v1/students";

  const fetchStudent = async () => {
    setLoading(true);
    const rep = await axios.get(url);
    setStudents(rep.data);
    setLoading(false);
  };

  const fetchRole = async () => {
    const rep = await axios.get("http://localhost:8088/api/v1/roles");
    setRoles(rep.data);
  };

  const fetchMajor = async () => {
    const rep = await axios.get("http://localhost:8088/api/v1/filieres");
    setMajors(rep.data);
  };

  useEffect(() => {
    fetchRole();
    fetchMajor();
    fetchStudent();
  }, []);

  const handleStudent = (e) => {
    setStudent({ ...student, [e.target.name]: e.target.value });
    console.log(students);
  };

  const handleMajor = (e) => {
    const selectedMajorId = e.target.value;
    setStudent({
      ...student,
      filiere: {
        ...student.filiere,
        id: selectedMajorId,
      },
    });
  };

  const handleRoleChange = (e) => {
    const roleid = e.target.value;
    if (e.target.checked) {
      setSelectedRoles((prevRoles) => [...prevRoles, roleid]);
    } else {
      setSelectedRoles((prevRoles) =>
        prevRoles.filter((role) => role !== roleid)
      );
    }
  };

  useEffect(() => {
    addStudentRoles();
  }, [selectedRoles]);

  const handleUpdate = async (id) => {
    const rep = await axios.get(`${url}/${id}`);
    setStudent(rep.data);
    setUpdateMode(true);
    setSelectedRoles([]);
  };

  const addStudentRoles = () => {
    const rolesArray = selectedRoles.map((role) => ({
      id: role,
      name: "",
    }));
    setStudent({
      ...student,
      roles: rolesArray,
    });
  };

  const reset = () => {
    setStudent({
      id: "",
      roles: [],
      username: "",
      password: "",
      firstname: "",
      lastname: "",
      telephone: "",
      login: "",
      filiere: {
        id: "",
        code: "",
        name: "",
      },
    });
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach((checkbox) => {
      checkbox.checked = false;
    });
    setSelectedRoles([]);
  };

  const addStudent = async () => {
    console.log(student);
    const rep = axios.post(url, student);
    notify("added");
    reset();
    fetchStudent();
    fetchStudent();
    fetchStudent();
  };

  const updateStudent = async () => {
    console.log(student);
    const rep = await axios.put(`${url}/${student.id}`, student);
    reset();
    fetchStudent();
    notify("updated");
  };

  const deleteStudent = async () => {
    const rep = await axios.delete(`${url}/${id}`);
    fetchStudent();
    closeModal();
    notify("deleted");
  };

  const [modal, setModal] = useState(false);

  const showModal = (stuid) => {
    setId(stuid);
    setModal(true);
  };

  const closeModal = () => {
    setModal(false);
  };

  const notify = (op) =>
    toast.success(`Student ${op} successfully`, {
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
      <p></p>
      <input
        className="role-input"
        placeholder="firstname"
        name="firstname"
        value={student.firstname}
        onChange={handleStudent}
      />
      <input
        className="role-input"
        placeholder="lastname"
        name="lastname"
        value={student.lastname}
        onChange={handleStudent}
      />

      <input
        className="role-input"
        placeholder="login"
        name="login"
        type="login"
        value={student.login}
        onChange={handleStudent}
      />
      <input
        className="role-input"
        placeholder="password"
        name="password"
        type="password"
        disabled={updateMode}
        value={student.password}
        onChange={handleStudent}
      />
      <input
        className="role-input"
        placeholder="telephone"
        name="telephone"
        type="telephone"
        value={student.telephone}
        onChange={handleStudent}
      />
      <br />

      <select
        className="role-input"
        value={student.filiere.id}
        onChange={handleMajor}
      >
        <option>Select major</option>
        {majors.map((major, index) => (
          <option value={major.id} key={index}>
            {major.name}
          </option>
        ))}
      </select>

      {roles.map((role, index) => (
        <span key={index}>
          <input
            id={role.id}
            type="checkbox"
            className="role-input"
            value={role.id}
            onChange={handleRoleChange}
          />
          <label className="larger-text">{role.name}</label>
        </span>
      ))}

      <br />

      {!updateMode ? (
        <button className="add-button" onClick={addStudent}>
          Ajouter
        </button>
      ) : (
        <button className="update-button" onClick={updateStudent}>
          Modifier
        </button>
      )}

      <div className="data-container">
        {loading ? (
          <p>loading data</p>
        ) : (
          <table>
            <thead>
              <tr className="data-row">
                <th className="data-header">Nom Complet</th>
                <th className="data-header">Rôles</th>

                <th className="data-header">Filière</th>
                <th className="data-header">login</th>
                <th className="data-header">Téléphone</th>
                <th className="data-header">Actions</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student, index) => (
                <tr className="data-row" key={index}>
                  <td>
                    <p>
                      {student.firstname} {student.lastname}
                    </p>
                  </td>
                  <td>
                    {student.roles.map((role, index) => (
                      <p className="role" key={index}>
                        {role.name}
                      </p>
                    ))}
                  </td>
                  <td>
                    <p>{student.filiere.name}</p>
                  </td>
                  <td>
                    <p>{student.login}</p>
                  </td>
                  <td>
                    <p>{student.telephone}</p>
                  </td>
                  <td>
                    <span className="action-buttons">
                      <button
                        className="update-button"
                        onClick={() => handleUpdate(student.id)}
                      >
                        Modifier
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => showModal(student.id)}
                      >
                        Supprimer
                      </button>
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <Modal isOpen={modal} onRequestClose={closeModal} className="Modal">
        <button className="cancel-button" onClick={closeModal}></button>
        <p className="modal-content">es ce sur de supprimer etudiant?</p>
        <div>
          <button className="cancel-button" onClick={closeModal}>
            Cancel
          </button>
          <button className="delete-button" onClick={deleteStudent}>
            Delete
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default Etudiants;
