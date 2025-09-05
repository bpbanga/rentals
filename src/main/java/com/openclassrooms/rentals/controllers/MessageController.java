package com.openclassrooms.rentals.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.rentals.dto.MessageRequestDto;
import com.openclassrooms.rentals.dto.MessageResponseDto;
import com.openclassrooms.rentals.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * <b>MessageController</b>
 * <p>
 * This controller manages operations related to user messages.
 * </p>
 * 
 * <ul>
 *   <li>Sending a new message</li>
 * </ul>
 *
 * <p>
 * It relies on the {@link MessageService} to handle message persistence.
 * </p>
 *
 * <p><b>Logging</b>: Uses Lombok {@code @Slf4j} for debug and error tracking.</p>
 */
@Tag(name = "MessageController", description = "Manage user messages")
@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MessageController extends AbstractController {

    private final MessageService messageSrvc;

    /**
     * Constructor for MessageController.
     *
     * @param messageSrvc the service used to manage message-related logic
     */
    public MessageController(MessageService messageSrvc) {
        this.messageSrvc = messageSrvc;
        log.debug("MessageController initialized.");
    }

    /**
     * Send a new message.
     *
     * @param messageReq the message request DTO containing message content and metadata
     * @return a {@link MessageResponseDto} indicating the result of the operation
     */
    @Operation(
        summary = "Send a message",
        description = "Create and send a new message based on the provided request body.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Message successfully sent"),
            @ApiResponse(responseCode = "400", description = "Invalid message request payload")
        }
    )
    @PostMapping
    public ResponseEntity<MessageResponseDto> create(@RequestBody MessageRequestDto messageReq) {
        log.debug("POST /api/messages - Sending new message: {}", messageReq);

        ResponseEntity<MessageResponseDto> response = responseFromOptional(
            messageSrvc.save(messageReq)
                .map(m -> new MessageResponseDto("Message sent successfully")),
            HttpStatus.BAD_REQUEST
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.debug("Message sent successfully: {}", messageReq);
        } else {
            log.error("Failed to send message with payload: {}", messageReq);
        }

        return response;
    }
}
