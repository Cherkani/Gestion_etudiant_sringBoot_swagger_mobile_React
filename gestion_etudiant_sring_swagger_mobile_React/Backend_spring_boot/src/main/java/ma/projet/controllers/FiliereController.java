package ma.projet.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.projet.entities.Filiere;
import ma.projet.services.FiliereService;

@RestController
@RequestMapping("/api/v1/filieres")
@CrossOrigin(origins = "http://localhost:3000")
public class FiliereController {

	@Autowired
	private FiliereService service;
	
	@GetMapping
	public List<Filiere> findAllFilieres(){
		return service.findAll();
	}

	@PostMapping
	public Filiere createFiliere(@RequestBody Filiere filiere){
		filiere.setId(0);
		return service.create(filiere);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable int id) {
		Filiere filiere=service.findById(id);
		if (filiere==null) {
			return new ResponseEntity<Object>("la filiere avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
		}else {
			return ResponseEntity.ok(filiere);
		}
	}
		@PutMapping("/updateFiliere/{id}")
		public ResponseEntity<Object> updatefiliere(@PathVariable int id,@RequestBody Filiere newfiliere){
			Filiere filiere=service.findById(id);
			if (filiere ==null) {
				return new ResponseEntity<Object>("la filiere avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
			}else {
			newfiliere.setId(id);
				return ResponseEntity.ok(service.update(newfiliere));
			}
			
		}
@DeleteMapping("/deleteFiliere/{id}")
	public ResponseEntity<Object> deleteFiliere(@PathVariable int id){
			Filiere filiere = service.findById(id);
			if (filiere==null){
				return new ResponseEntity<Object>("la filiere avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
			}else{
				return ResponseEntity.ok(service.delete(filiere));
			}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
