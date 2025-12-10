package tn.esprit.studentmanagement.controllers;

import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        Student s1 = new Student(1L, "Alice", "Smith", "alice@example.com", "12345678",
                LocalDate.of(2000,1,1), "Address1", null, null);
        Student s2 = new Student(2L, "Bob", "Johnson", "bob@example.com", "87654321",
                LocalDate.of(2001,2,2), "Address2", null, null);

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(s1, s2));

        List<Student> students = studentController.getAllStudents();

        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).getFirstName());
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetStudentById() {
        Student s = new Student(1L, "Alice", "Smith", "alice@example.com", "12345678",
                LocalDate.of(2000,1,1), "Address1", null, null);

        when(studentService.getStudentById(1L)).thenReturn(s);

        Student student = studentController.getStudent(1L);

        assertNotNull(student);
        assertEquals("Alice", student.getFirstName());
        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void testCreateStudent() {
        Student s = new Student(null, "Charlie", "Brown", "charlie@example.com", "55555555",
                LocalDate.of(2002,3,3), "Address3", null, null);
        Student saved = new Student(3L, "Charlie", "Brown", "charlie@example.com", "55555555",
                LocalDate.of(2002,3,3), "Address3", null, null);

        when(studentService.saveStudent(s)).thenReturn(saved);

        Student result = studentController.createStudent(s);

        assertEquals(3L, result.getIdStudent());
        verify(studentService, times(1)).saveStudent(s);
    }

    @Test
    void testUpdateStudent() {
        Student s = new Student(1L, "Alice", "Smith", "alice@example.com", "12345678",
                LocalDate.of(2000,1,1), "Address1", null, null);

        when(studentService.saveStudent(s)).thenReturn(s);

        Student updated = studentController.updateStudent(s);

        assertEquals("Alice", updated.getFirstName());
        verify(studentService, times(1)).saveStudent(s);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent(1L);

        studentController.deleteStudent(1L);

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
