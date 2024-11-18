package assertj;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import domain.Address;
import domain.Email;
import domain.Person;
import domain.Phone;

public class DeepEqualityTest {

	@Test
	public void deepEqualityTest() {
		
		var expectedAddress = new Address(1L,"Sesame Street", "New York",  "United States");
		var expectedEmails = List.of(new Email("jane.doe@gmail.com"));
		var expectedPhones = List.of(new Phone(12L,123456789L));
		var expectedPerson = new Person(1L, "Jane", "Doe",expectedAddress,expectedEmails,expectedPhones);


		
		var actualAddress = new Address(1L, "Sesame Street","New York",  "United States");
		var actualEmails = List.of(new Email("jane.doe@gmail.com"));
		var actualPhones = List.of(new Phone(12L,123456789L));
		var actualPerson = new Person(1L, "Jane", "Doe",actualAddress,actualEmails,actualPhones);

		assertThat(actualPerson)
		  .usingRecursiveComparison()
		  .isEqualTo(expectedPerson);
	}
}
