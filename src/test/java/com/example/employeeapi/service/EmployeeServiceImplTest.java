package com.example.employeeapi.service;

import com.example.employeeapi.dto.EmployeeDTO;
import com.example.employeeapi.entity.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repo;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee sampleEmployee() {
        Employee e = new Employee();
        e.setId(1L);
        e.setName("Soniya");
        e.setEmail("soniya@gmail.com");
        e.setDepartment("IT");
        e.setSalary(50000.0);
        return e;
    }

    // ---------- getEmployeeById ----------

    @Test
    void getEmployeeById_success() {
        Employee e = sampleEmployee();
        when(repo.findById(1L)).thenReturn(Optional.of(e));

        Employee result = service.getEmployeeById(1L);

        assertEquals("Soniya", result.getName());
        verify(repo).findById(1L);
    }

    @Test
    void getEmployeeById_notFound() {
        when(repo.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getEmployeeById(2L));

        assertEquals("Employee not found", ex.getMessage());
    }

    // ---------- updateEmployee ----------

    @Test
    void updateEmployee_success() {
        Employee existing = sampleEmployee();
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Updated");
        dto.setEmail("updated@gmail.com");
        dto.setDepartment("HR");
        dto.setSalary(60000.0);

        Employee updated = service.updateEmployee(1L, dto);

        assertEquals("Updated", updated.getName());
        assertEquals("HR", updated.getDepartment());
        verify(repo).save(existing);
    }

    // ---------- deleteEmployee ----------

    @Test
    void deleteEmployee_success() {
        doNothing().when(repo).deleteById(1L);

        service.deleteEmployee(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}
