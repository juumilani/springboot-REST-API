package com.tutorial.restapi.payroll;

import com.tutorial.restapi.payroll.employee.Employee;
import com.tutorial.restapi.payroll.employee.EmployeeRepository;
import com.tutorial.restapi.payroll.order.Order;
import com.tutorial.restapi.payroll.order.OrderRepository;
import com.tutorial.restapi.payroll.order.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Lombok annotation used for log purposes
@Slf4j
public class LoadDatabase {

    @Bean
    // Spring Boot will run all the beans in the CommandLineRunner once the application context is loaded
    CommandLineRunner initDatabase(EmployeeRepository repository, OrderRepository orderRepository) {
        return args -> {
            repository.save(new Employee("Harry", "Potter", "Gryffindor"));
            repository.save(new Employee("Draco", "Malfoy", "Slytherin"));

            repository.findAll().forEach(employee -> log.info("Preloaded" + employee));

            orderRepository.save(new Order("Iphone", Status.COMPLETED));
            orderRepository.save(new Order("Lord of The Rings Book", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> log.info("Preloaded" + order));

        };
    }

}
