package jaxb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jaxb.entity.Employee;
import jaxb.entity.Employees;

public class JaxbDemo {

	public static void main(String[] args) throws JAXBException, IOException {
		Employees employees = new Employees();
	    employees.setEmployees(new ArrayList<Employee>());
	    //Create two employees 
	    Employee emp1 = new Employee();
	    emp1.setId(1);
	    emp1.setFirstName("Lokesh");
	    emp1.setLastName("Gupta");
	    emp1.setIncome(100.0);
	     
	    Employee emp2 = new Employee();
	    emp2.setId(2);
	    emp2.setFirstName("John");
	    emp2.setLastName("Mclane");
	    emp2.setIncome(200.0);
	     
	    //Add the employees in list
	    employees.getEmployees().add(emp1);
	    employees.getEmployees().add(emp2);
	    marshalingExample(employees);
	}

	 
	private static void marshalingExample(Employees employees) throws JAXBException, IOException
	{
	    JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(employees, baos);
        String content = IOUtils.toString(baos.toByteArray(),Charset.defaultCharset().toString());
        System.out.println(content);
	}
}
