package com.ascarrent.service;

import com.ascarrent.domain.ContactMessage;
import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import com.ascarrent.mapper.*;
import com.ascarrent.repository.ContactMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageMapper contactMessageMapper;
    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageMapper contactMessageMapper, ContactMessageRepository contactMessageRepository) {
        this.contactMessageMapper = contactMessageMapper;
        this.contactMessageRepository = contactMessageRepository;
    }

    public void createMessage(ContactMessageRequest contactMessageRequest) {
       ContactMessage contactMessage
               = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
       contactMessageRepository.save(contactMessage);

    }

    public List<ContactMessageDTO> getAllContactMessages() {
        List<ContactMessage> allContacts =  contactMessageRepository.findAll();
        return contactMessageMapper.map(allContacts);
    }

    public Page<ContactMessageDTO> getAllContactMessagesWithPage(Pageable pageable) {
        Page<ContactMessage> contactMessagePage = contactMessageRepository.findAll(pageable);
        Page<ContactMessageDTO> contactMessageDTOPage = getPageDTO(contactMessagePage);

        return  contactMessageDTOPage;

    }

    // getPageDTO
    private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage) {
        return contactMessagePage.map(contactMessage -> contactMessageMapper.contactMessageToDTO(contactMessage));
    }


}
