package com.movinder.be.controller;

import com.movinder.be.controller.dto.AddChatRequest;
import com.movinder.be.entity.Message;
import com.movinder.be.entity.Room;
import com.movinder.be.exception.MalformedRequestException;
import com.movinder.be.service.ForumService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService){
        this.forumService = forumService;
    }

    //add message to room
    @PutMapping("/rooms")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Message addMessage(@RequestBody AddChatRequest newMessage){
        return forumService.addMessage(newMessage);
    }

    @GetMapping("/rooms/{customerID}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Room> getRoomByCustomerId(@PathVariable String customerID){
        return forumService.getRoomByCustomerId(customerID);
    }

}
