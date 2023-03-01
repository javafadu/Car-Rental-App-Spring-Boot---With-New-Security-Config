package com.ascarrent.service;

import com.ascarrent.domain.ContactMessage;
import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.*;
import com.ascarrent.repository.ContactMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageMapper contactMessageMapper;
    private final ContactMessageRepository contactMessageRepository;

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    public ContactMessageService(ContactMessageMapper contactMessageMapper, ContactMessageRepository contactMessageRepository) {
        this.contactMessageMapper = contactMessageMapper;
        this.contactMessageRepository = contactMessageRepository;
    }

    public void createMessage(ContactMessageRequest contactMessageRequest, HttpServletRequest request) {
        ContactMessage contactMessage
                = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
        System.out.println("CLIENT IP ADDRESS:" + getClientIpAddress(request));
        contactMessage.setIpAddress(getClientIpAddress(request));
        contactMessage.setCreatedDateTime(LocalDateTime.now());
        contactMessageRepository.save(contactMessage);

    }

    public List<ContactMessageDTO> getAllContactMessages() {
        List<ContactMessage> allContacts = contactMessageRepository.findAll();
        return contactMessageMapper.listContactMessageToListDTO(allContacts);
    }

    public Page<ContactMessageDTO> getAllContactMessagesWithPage(Pageable pageable) {
        Page<ContactMessage> contactMessagePage = contactMessageRepository.findAll(pageable);
        Page<ContactMessageDTO> contactMessageDTOPage = getPageDTO(contactMessagePage);

        return contactMessageDTOPage;

    }

    public ContactMessageDTO getMessageWithId(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION, id)));
        return contactMessageMapper.contactMessageToDTO(contactMessage);

    }

    public void deleteContactMessage(Long id) {
        ContactMessageDTO contactMessageDTO = getMessageWithId(id);
        contactMessageRepository.deleteById(id);
    }

    public void updateContactMessage(Long id, ContactMessageRequest contactMessageRequest) {
        ContactMessageDTO foundContactMessageDTO = getMessageWithId(id);

        foundContactMessageDTO.setSenderName(contactMessageRequest.getSenderName());
        foundContactMessageDTO.setMessageBody(contactMessageRequest.getMessageBody());
        foundContactMessageDTO.setEmail(contactMessageRequest.getEmail());
        foundContactMessageDTO.setSubject(contactMessageRequest.getSubject());
        foundContactMessageDTO.setPhone(contactMessageRequest.getPhone());

        ContactMessage contactMessage = contactMessageMapper.contactMessageDTOToContactMessage(foundContactMessageDTO);
        contactMessageRepository.save(contactMessage);




    }


    // getPageDTO
    private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage) {
        return contactMessagePage.map(contactMessage -> contactMessageMapper.contactMessageToDTO(contactMessage));
    }

    // getIpAddress
    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }



}
