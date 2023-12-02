package ma.projet.controllers;

import ma.projet.entities.Filiere;
import ma.projet.entities.Role;
import ma.projet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {
    @Autowired
    private RoleService service;
@GetMapping
    public List<Role> findAllroles(){
    return service.findAll();
}
@PostMapping
    public Role createfiliere(@RequestBody Role role){
    role.setId((int) 0);
    return service.create(role);
}


    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Role role = service.findById(id);
        if (role==null) {
            return new ResponseEntity<Object>("la role avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(role);
        }
    }

@PutMapping("/updateRole/{id}")
    public ResponseEntity<Object> updatefiliere(@PathVariable int id, @RequestBody Role newrole){
    Role role = service.findById(id);
    if (role==null) {
        return new ResponseEntity<Object>("le role avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
    }else {
        newrole.setId(id);
        return ResponseEntity.ok(service.update(newrole));
    }
}
    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<Object> deletefiliere(@PathVariable int id){
        Role role = service.findById(id);
        if (role==null) {
            return new ResponseEntity<Object>("le role avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(service.delete(role));
        }
    }

}
