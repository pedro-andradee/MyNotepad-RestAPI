package com.bluetech.pedro.andrade.MyNotepad.services;

import com.bluetech.pedro.andrade.MyNotepad.dtos.ContactDTO;
import com.bluetech.pedro.andrade.MyNotepad.models.Contact;
import com.bluetech.pedro.andrade.MyNotepad.repositories.ContactRepository;
import com.bluetech.pedro.andrade.MyNotepad.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    public ContactDTO insertNewContact(ContactDTO contactDTO) {
        Contact entity = new Contact();
        copyDtoToEntity(contactDTO, entity);
        entity = contactRepository.save(entity);
        return new ContactDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<ContactDTO> getAllContactsPage(Pageable pageable) {
        Page<Contact> page = contactRepository.findAll(pageable);
        return page.map(ContactDTO::new);
    }

    @Transactional
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        try {
            Contact entity = contactRepository.getReferenceById(id);
            copyDtoToEntity(contactDTO, entity);
            entity = contactRepository.save(entity);
            return new ContactDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional
    public void deleteContact(Long id) {
        try {
            contactRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    private void copyDtoToEntity(ContactDTO contactDTO, Contact entity) {
        entity.setName(contactDTO.getName());
        entity.setEmail(contactDTO.getEmail());
        entity.setPhoneNumber(contactDTO.getPhoneNumber());
    }
}
