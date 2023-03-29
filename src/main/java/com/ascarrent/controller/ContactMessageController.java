package com.ascarrent.controller;

import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    // Create ContactMessage
    @PostMapping("/visitors")
    public ResponseEntity<ACRResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest, HttpServletRequest httpServletRequest) {
        contactMessageService.createMessage(contactMessageRequest,httpServletRequest);
        ACRResponse response = new ACRResponse(ResponseMessage.CONTACT_MESSAGE_CREATE_MESSAGE, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All ContactMessages
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
        List<ContactMessageDTO> contactMessageList =  contactMessageService.getAllContactMessages();
        return ResponseEntity.ok(contactMessageList);
    }

    // Gett All ContactMessages by Paging
    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessagesWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC")Sort.Direction direction)
    {
        Pageable pageable = PageRequest.of(page,size, Sort.by(direction,prop));
        return ResponseEntity.ok(contactMessageService.getAllContactMessagesWithPage(pageable));

    }

    // Get a contact with id (PathVariable solution)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {
       return ResponseEntity.ok(contactMessageService.getMessageWithId(id));
    }

    // Get a contact with id (RequestParam solution)
    @GetMapping("/message")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(
            @RequestParam("id") Long id) {
        return ResponseEntity.ok(contactMessageService.getMessageWithId(id));
    }

    // Delete Contact
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> deleteContactMessage(@PathVariable Long id) {
        contactMessageService.deleteContactMessage(id);
        ACRResponse acrResponse = new ACRResponse(ResponseMessage.CONTACT_MESSAGE_DELETE_RESPONSE,true);
        return ResponseEntity.ok(acrResponse);
    }

    // Update Contact
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> updateContactMessage(@PathVariable Long id, @Valid @RequestBody ContactMessageRequest contactMessageRequest) {
        contactMessageService.updateContactMessage(id,contactMessageRequest);
        ACRResponse acrResponse = new ACRResponse(ResponseMessage.CONTACT_MESSAGE_UPDATE_RESPONSE,true);
        return ResponseEntity.ok(acrResponse);

    }

}
