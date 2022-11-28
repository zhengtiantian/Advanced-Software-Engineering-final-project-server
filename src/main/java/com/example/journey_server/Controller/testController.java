package com.example.journey_server.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.ServerSocket;

@RestController
public class testController {

    @GetMapping("/match")
    public String hello(HttpServletRequest request) throws IOException {
        return "The request is received by server" + (request.getLocalPort() - 8079);
    }
}
