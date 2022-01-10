package com.empinventory.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empinventory.model.Employee;
import com.empinventory.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@PostMapping("upload")
	public ResponseEntity<String> uploadEmployeeData() {
		try {
			File file = new ClassPathResource("sample-data.txt").getFile();
			employeeService.uploadFile(new DataInputStream(new FileInputStream(file)));
		} catch (IOException | InterruptedException e) {
			return new ResponseEntity<String>("Failed" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Data uploaded ", HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Employee>> findAll() {
		return new ResponseEntity<List<Employee>>(employeeService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> find(@PathVariable final Integer id) {
		if (Objects.isNull(id)) {
			return new ResponseEntity<String>("Id cannot be empty", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Employee>(employeeService.find(id.intValue()), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findByName(@RequestParam("name") final String name) {
		if (Objects.isNull(name)) {
			return new ResponseEntity<String>("Name cannot be empty", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Employee>>(employeeService.findByName(name), HttpStatus.OK);
	}

	@PutMapping
	public void update(@RequestBody final Employee employee) {
		employeeService.update(employee.getId(), employee.getName(), employee.getAge());
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable final Integer id) {
		employeeService.delete(id);
	}

}
