package com.zikozee.all_spring_security.student;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    // hasRole('ROLE_')  hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE', 'ROLE_SUPER_ADMIN')") //role base authentication appending with ROLE_
    @PreAuthorize("hasAuthority('student:read')") //This will be most appropriate i.e any role that has student:read permission, though student should not be able to read all students, good enough student has no permission
    public List<Student> getAllStudents(){
        log.info("getAllStudents");
        return STUDENTS;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('student:write')") // permission based authentication, added just as it is
    public void registerNewStudent(@RequestBody Student student){
        log.info("registerNewStudent: " +student.toString());
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        log.info("deleteStudent: " + "studentId: " +studentId);
    }

    @PutMapping(path ="{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId")Integer studentId, @RequestBody Student student){
        log.info("updateStudent: " + studentId + " " + student);
    }
}
