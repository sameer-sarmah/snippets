package domain;

import java.util.List;

public record Person(Long id,String firstName,String lastName,Address address,List<Email> emails,List<Phone> phones) {

}
