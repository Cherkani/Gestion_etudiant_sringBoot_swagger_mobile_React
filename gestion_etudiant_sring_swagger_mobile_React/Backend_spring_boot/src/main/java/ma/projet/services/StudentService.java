package ma.projet.services;

import ma.projet.IDao.IDao;
import ma.projet.entities.Filiere;
import ma.projet.entities.Student;
import ma.projet.repisitories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class StudentService implements IDao<Student> {
    @Autowired
    StudentRepository repisitory;
    @Override
    public Student create(Student o) {
        return repisitory.save(o);
    }

    @Override
    public Student update(Student o) {
        return repisitory.save(o);
    }

    @Override
    public Boolean delete(Student o) {
        try {
            repisitory.delete(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Student findById(int id) {
        return repisitory.findById(id).orElse(null);
    }

    @Override
    public List<Student> findAll() {
        return repisitory.findAll();
    }

    public List<Student>  findStudentsByFiliere(Filiere filiere){
        return repisitory.findStudentsByFiliere(filiere);
    }
}
