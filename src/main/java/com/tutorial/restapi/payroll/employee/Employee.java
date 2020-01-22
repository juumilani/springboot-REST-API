package com.tutorial.restapi.payroll.employee;

import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// Lombok annotation that creates all getters, setters, equal, hash and toString methods of the class attributes
@Data
// JPA annotation to make this object ready for storage
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String firstName;
    private String lastName;

    private String role;

    Employee() {}

    public Employee(String firstName, String lastName, String role){

        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getName(){
        return this.firstName + " " + this.lastName;
    }

    public void setName(){
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }
}



