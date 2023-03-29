package com.ascarrent.mapper;

import com.ascarrent.domain.ContactMessage;
import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-29T18:16:31+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.13 (Oracle Corporation)"
)
@Component
public class ContactMessageMapperImpl implements ContactMessageMapper {

    @Override
    public ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage) {
        if ( contactMessage == null ) {
            return null;
        }

        ContactMessageDTO contactMessageDTO = new ContactMessageDTO();

        contactMessageDTO.setId( contactMessage.getId() );
        contactMessageDTO.setSenderName( contactMessage.getSenderName() );
        contactMessageDTO.setEmail( contactMessage.getEmail() );
        contactMessageDTO.setPhone( contactMessage.getPhone() );
        contactMessageDTO.setSubject( contactMessage.getSubject() );
        contactMessageDTO.setMessageBody( contactMessage.getMessageBody() );
        contactMessageDTO.setIpAddress( contactMessage.getIpAddress() );
        contactMessageDTO.setCreatedDateTime( contactMessage.getCreatedDateTime() );

        return contactMessageDTO;
    }

    @Override
    public ContactMessage contactMessageDTOToContactMessage(ContactMessageDTO contactMessageDTO) {
        if ( contactMessageDTO == null ) {
            return null;
        }

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setSenderName( contactMessageDTO.getSenderName() );
        contactMessage.setEmail( contactMessageDTO.getEmail() );
        contactMessage.setPhone( contactMessageDTO.getPhone() );
        contactMessage.setSubject( contactMessageDTO.getSubject() );
        contactMessage.setMessageBody( contactMessageDTO.getMessageBody() );
        contactMessage.setIpAddress( contactMessageDTO.getIpAddress() );
        contactMessage.setCreatedDateTime( contactMessageDTO.getCreatedDateTime() );

        return contactMessage;
    }

    @Override
    public ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest) {
        if ( contactMessageRequest == null ) {
            return null;
        }

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setSenderName( contactMessageRequest.getSenderName() );
        contactMessage.setEmail( contactMessageRequest.getEmail() );
        contactMessage.setPhone( contactMessageRequest.getPhone() );
        contactMessage.setSubject( contactMessageRequest.getSubject() );
        contactMessage.setMessageBody( contactMessageRequest.getMessageBody() );

        return contactMessage;
    }

    @Override
    public List<ContactMessageDTO> listContactMessageToListDTO(List<ContactMessage> contactMessageList) {
        if ( contactMessageList == null ) {
            return null;
        }

        List<ContactMessageDTO> list = new ArrayList<ContactMessageDTO>( contactMessageList.size() );
        for ( ContactMessage contactMessage : contactMessageList ) {
            list.add( contactMessageToDTO( contactMessage ) );
        }

        return list;
    }
}
