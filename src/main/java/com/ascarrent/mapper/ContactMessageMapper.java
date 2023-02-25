package com.ascarrent.mapper;

import com.ascarrent.domain.ContactMessage;
import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") // I can inject any class
public interface ContactMessageMapper {

        // ContactMessage ---> ContactMessageDTO
        ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);

        // ContactMessageDTO ---> ContactMessage
        @Mapping(target="id", ignore = true) // There is no id in DTO
        ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);

        // List<ContactMessage> ---> List<ContactMessageDTO>
        List<ContactMessageDTO> listContactMessageToListDTO(List<ContactMessage> contactMessageList);


}
