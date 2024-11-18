package jaxb.entity;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "employees")
@XmlAccessorType (XmlAccessType.FIELD)
public class Employees 
{
    @XmlElement(name = "employee")
    private List<Employee> employees = null;
 
    public List<Employee> getEmployees() {
        return employees;
    }
 
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}