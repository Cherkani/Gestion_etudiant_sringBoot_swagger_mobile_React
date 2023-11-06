package ma.projet.repisitories;

import ma.projet.entities.Filiere;
import ma.projet.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    public List<Student> findStudentsByFiliere(Filiere filiere);
}
