package com.university.academic;

import com.university.academic.model.Course;
import com.university.academic.model.Department;
import com.university.academic.model.Lecturer;
import com.university.academic.model.Registration;
import com.university.academic.model.RegistrationStatus;
import com.university.academic.model.Student;
import com.university.academic.model.StudentProfile;
import com.university.academic.repository.CourseRepository;
import com.university.academic.repository.DepartmentRepository;
import com.university.academic.repository.LecturerRepository;
import com.university.academic.repository.RegistrationRepository;
import com.university.academic.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
class EntityMappingTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void allRelationshipsPersistAndReload() {
        Department department = new Department();
        department.setName("Computer Science");
        department.setCode("CS");
        department = departmentRepository.save(department);

        Lecturer lecturer = new Lecturer();
        lecturer.setFirstName("Alice");
        lecturer.setLastName("Mukamana");
        lecturer.setEmail("alice.mukamana@university.test");
        lecturer.setDepartment(department);
        lecturer = lecturerRepository.save(lecturer);

        department.setHead(lecturer);
        departmentRepository.save(department);

        Course course = new Course();
        course.setCode("CS101");
        course.setTitle("Introduction to Programming");
        course.setCredits(3);
        course.setDepartment(department);
        course = courseRepository.save(course);

        lecturer.addCourse(course);
        lecturerRepository.save(lecturer);

        Student student = new Student();
        student.setStudentNumber("STU-0001");
        student.setFirstName("Jean");
        student.setLastName("Habimana");
        student.setEmail("jean.habimana@student.test");
        student.setDepartment(department);

        StudentProfile profile = new StudentProfile();
        profile.setDateOfBirth(LocalDate.of(2004, 5, 14));
        profile.setGender("Male");
        profile.setPhoneNumber("+250780000000");
        profile.setAddress("Kigali, Gasabo");
        student.setProfile(profile);
        student = studentRepository.save(student);

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setRegistrationDate(LocalDate.of(2026, 9, 1));
        registration.setAcademicYear("2026/2027");
        registration.setSemester("Semester 1");
        registration.setStatus(RegistrationStatus.PENDING);
        registration = registrationRepository.save(registration);

        Long departmentId = department.getId();
        Long lecturerId = lecturer.getId();
        Long courseId = course.getId();
        Long studentId = student.getId();

        entityManager.flush();
        entityManager.clear();

        Department loadedDepartment = departmentRepository.findById(departmentId).orElseThrow();
        assertEquals(lecturerId, loadedDepartment.getHead().getId());
        assertEquals(1, loadedDepartment.getLecturers().size());
        assertEquals(1, loadedDepartment.getCourses().size());
        assertEquals(1, loadedDepartment.getStudents().size());

        Course loadedCourse = courseRepository.findById(courseId).orElseThrow();
        assertEquals(1, loadedCourse.getLecturers().size());

        Student loadedStudent = studentRepository.findById(studentId).orElseThrow();
        assertEquals("Kigali, Gasabo", loadedStudent.getProfile().getAddress());

        List<Registration> registrations = registrationRepository.findByStudentId(studentId);
        assertEquals(1, registrations.size());
        assertEquals(RegistrationStatus.PENDING, registrations.get(0).getStatus());
        assertNull(registrations.get(0).getGrade());
    }
}
