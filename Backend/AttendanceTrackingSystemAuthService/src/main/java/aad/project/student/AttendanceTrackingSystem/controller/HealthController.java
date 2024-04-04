package aad.project.student.AttendanceTrackingSystem.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthController {

    @GetMapping("/health")
    @ResponseBody
    public String health() throws Exception {
        return "healthy";
    }
}
