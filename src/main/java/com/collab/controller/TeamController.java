package com.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.collab.entity.TeamMember;
import com.collab.service.TeamService;
import java.util.Map;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamService service;

    @PostMapping("/join")
    public TeamMember join(@RequestBody TeamMember t) {
        return service.joinProject(t);
    }

    @PutMapping("/status")
    public TeamMember updateStatus(@RequestBody Map<String, Object> body) {
        Long studentId = Long.valueOf(body.get("studentId").toString());
        Long projectId = Long.valueOf(body.get("projectId").toString());
        String message = body.get("statusMessage").toString();
        return service.updateStatusMessage(studentId, projectId, message);
    }
}
