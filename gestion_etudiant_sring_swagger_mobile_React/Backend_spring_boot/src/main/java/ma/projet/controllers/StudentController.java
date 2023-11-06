package ma.projet.controllers;

import ma.projet.entities.Filiere;
import ma.projet.entities.Role;
import ma.projet.entities.Student;
import ma.projet.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> findAllstudents(){
        return service.findAll();
    }
    @PostMapping
    public Student createstudent(@RequestBody Student student){
        //student.setId((int) 0);
        return service.create(student);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("la student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(student);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatestudent(@PathVariable int id, @RequestBody Student newstudent){
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("le student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            newstudent.setId(id);
            return ResponseEntity.ok(service.update(newstudent));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletestudent(@PathVariable int id){
        Student student = service.findById(id);
        if (student==null) {
            return new ResponseEntity<Object>("le student avec id : "+id+"est introuvable", HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(service.delete(student));
        }
    }







    @GetMapping("/filiere")
    public List<Student> findStudentsByFiliere(@RequestBody Filiere filiere){
        return service.findStudentsByFiliere(filiere);
    }

}
