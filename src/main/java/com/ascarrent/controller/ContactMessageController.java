package com.ascarrent.controller;

import com.ascarrent.dto.ContactMessageDTO;
import com.ascarrent.dto.request.ContactMessageRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ACRResponse response = new ACRResponse("The Message has been successfully saved ", true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All ContactMessages
    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
        List<ContactMessageDTO> contactMessageList =  contactMessageService.getAllContactMessages();
        return ResponseEntity.ok(contactMessageList);
    }

    // Gett All ContactMessages by Paging
    @GetMapping("/pages")
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
    public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {
       return ResponseEntity.ok(contactMessageService.getMessageWithId(id));
    }

    // Get a contact with id (RequestParam solution)
    @GetMapping("/message")
    public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(
            @RequestParam("id") Long id) {
        return ResponseEntity.ok(contactMessageService.getMessageWithId(id));
    }

}
