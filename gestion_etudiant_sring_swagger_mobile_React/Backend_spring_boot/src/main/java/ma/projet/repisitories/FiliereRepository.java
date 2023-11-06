package ma.projet.repisitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.projet.entities.Filiere;
@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Integer>{
	
}
