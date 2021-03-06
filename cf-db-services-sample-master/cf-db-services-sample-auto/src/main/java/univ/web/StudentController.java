package univ.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.domain.Course;
import univ.domain.Student;
import univ.repos.CourseRepo;
import univ.repos.StudentRepo;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    @Autowired
    public StudentController(StudentRepo studentRepo, CourseRepo courseRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Student> save(@RequestBody @Valid Student student) {
        Student saved = this.studentRepo.save(student);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Student> update(@RequestBody @Valid Student student) {
        Student saved = this.studentRepo.save(student);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Student>> getPage(Pageable pageable) {
        Page<Student> page = this.studentRepo.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Student> get(@PathVariable("id") Long id) {
        Student student = this.studentRepo.findOne(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @RequestMapping(path = "/{studentId}/courses", method = RequestMethod.GET)
    public ResponseEntity<Set<Course>> getStudentCourses(@PathVariable("studentId") Long studentId) {
        Student student = this.studentRepo.findOne(studentId);
        Set<Course> coursesByStudent = student.getCourses();//should resolve lazily
        return new ResponseEntity<>(coursesByStudent, HttpStatus.OK);
    }

    @RequestMapping(path = "/{studentId}/courses", method = RequestMethod.PUT)
    public ResponseEntity<Course> addCourseToStudent(@RequestBody Course course, @PathVariable("studentId") Long studentId) {
        Student studentFromDb = this.studentRepo.findOne(studentId);
        Course courseFromDb = this.courseRepo.findOne(course.getCourseId());
        studentFromDb.getCourses().add(courseFromDb);
        this.studentRepo.save(studentFromDb);
        return new ResponseEntity<>(courseFromDb, HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        this.studentRepo.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
}
