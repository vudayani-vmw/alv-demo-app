package com.empinventory.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.empinventory.model.Employee;

@Service
public interface EmployeeService {
	String uploadFile(InputStream inputStreamm) throws IOException, InterruptedException;

	void update(int id, String name, int age);

	void delete(int id);

	Employee find(int id);

	List<Employee> findByName(String name);

	List<Employee> findByAge(int age);
	
	List<Employee> findAll();
}
