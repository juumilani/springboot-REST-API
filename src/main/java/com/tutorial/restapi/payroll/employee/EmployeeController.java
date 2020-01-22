package com.tutorial.restapi.payroll.employee;

import com.tutorial.restapi.payroll.employee.exception.EmployeeNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// the data returned by each method will be written directly into the response body
@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    private final EmployeeRepresentationModelAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeRepresentationModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // AGGREGATE ROOT (idk what this stands for)

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }


    // the post doesn't work if you pass it as a "name" and not "firstname" and "lastname" -- i'll make it work later
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    // SINGLE ITEMS

    // the return type Resource<T> is a generic container from Spring HATEOAS that includes a collection of links with the data
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id){

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    @Component
    static
    class EmployeeRepresentationModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

        @Override
        // the method "toModel" converts a non-entity object (employee) into a entity-based object (EntityModel<Employee>)
        public EntityModel<Employee> toModel(Employee employee){
            return new EntityModel<>(employee,

                    // this method asks that HATEOAS build a link to the employee's one() method and flag it as a self link
                    linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                    // this method asks that HATEOAS build a link to the aggregate root all() and call it "employees"
                    linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
        }
    }
}
