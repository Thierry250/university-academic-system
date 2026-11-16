package com.university.academic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // One department has one head. The foreign key lives in the departments table.
    @OneToOne
    @JoinColumn(name = "head_lecturer_id", unique = true)
    private Lecturer head;

    @OneToMany(mappedBy = "department")
    private List<Lecturer> lecturers = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

    public Department() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Lecturer getHead() { return head; }
    public void setHead(Lecturer head) { this.head = head; }

    public List<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(List<Lecturer> lecturers) { this.lecturers = lecturers; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}