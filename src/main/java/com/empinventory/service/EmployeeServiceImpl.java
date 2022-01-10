package com.empinventory.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empinventory.model.Employee;
import com.empinventory.repository.EmployeeRepository;
import com.sun.istack.Nullable;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private static Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Value("${file.upload.timeout}")
	private int delay;

	
	/* Log the status message after inserting the given number of records */
	@Value("${file.upload.logAfterRecordsCount}")
	private int logAfterRecordsCount;


	@Override
	@Transactional(timeout = 1)
	public String uploadFile(@NonNull final InputStream inputStreamm) throws IOException, InterruptedException {
		try {
			Thread.sleep(delay);
			loadData(inputStreamm);
		} catch (InterruptedException | IOException e) {
			log.error("Failed to load the file data " + e.getMessage());
		} 
		return "Success";
	}

	private void loadData(@NonNull final InputStream inputStream) throws IOException, InterruptedException {
		int count = 0;
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = reader.readLine();
		
		Thread.sleep(3000);
		while (( line = reader.readLine()) != null) {
			final String values[] = line.split(" ");
			log.debug("Employee -> " + values[0] + " " + values[1]);
			employeeRepository.save(new Employee(values[0], Integer.valueOf(values[1])));
			count++;
			
			if ((count % logAfterRecordsCount) == 0) {
				log.info("Processed " + count + " Record(s)");
			}
		}
		reader.close();
	}

	@Override
	public void update(@NonNull final int id, @Nullable final String name, @Nullable final int age) {
		final Optional<Employee> optionalEmployee = employeeRepository.findById(id);
		
		if (optionalEmployee.isPresent()) {
			final Employee emp = optionalEmployee.get();
			
			if (Objects.nonNull(name)) {
				emp.setName(name);
			}
			
			if (Objects.nonNull(age)) {
				emp.setAge(age);
			}
			
			employeeRepository.save(emp);
		}
	}

	@Override
	public void delete(@NonNull final int id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public Employee find(@NonNull final int id) {
		final Optional<Employee> optionalEmployee = employeeRepository.findById(id);
		if (optionalEmployee.isPresent()) {
			return optionalEmployee.get();
		}
		return null;
	}

	@Override
	public List<Employee> findByName(@NonNull final String name) {
		return employeeRepository.findByName(name);
	}

	@Override
	public List<Employee> findByAge(@NonNull final int age) {
		return employeeRepository.findByAge(age);
	}

	@Override
	@Transactional(timeout = 1)
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

}
