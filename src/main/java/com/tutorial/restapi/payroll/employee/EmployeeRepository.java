package com.tutorial.restapi.payroll.employee;

import org.springframework.data.jpa.repository.JpaRepository;

/* this interface extends the JPA's repository specifying the domain type as Employee and the id type as Long
*  it supports creating new instances, updating existing ones, deleting and finding (one, all, etc) */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
