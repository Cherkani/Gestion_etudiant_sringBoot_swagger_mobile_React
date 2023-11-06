package ma.projet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Student extends User{
	private String firstname;
	private String lastname;
	private String telephone;
	@ManyToOne
	private Filiere filiere;
}
