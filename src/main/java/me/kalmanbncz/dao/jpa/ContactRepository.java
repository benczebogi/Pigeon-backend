package me.kalmanbncz.dao.jpa;

import me.kalmanbncz.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository can be used to delegate CRUD operations against the data source: http://goo.gl/P1J8QH
 */
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
    Contact findContactByCity(String city);

    Page findAll(Pageable pageable);
}
