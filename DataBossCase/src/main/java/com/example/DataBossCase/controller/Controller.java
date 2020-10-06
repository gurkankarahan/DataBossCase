package com.example.DataBossCase.controller;

import com.example.DataBossCase.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    public ServiceImpl serv;


    @GetMapping("/top5carriers")
    public List<String> getTop5carriers() throws IOException {
        return serv.getTop5carriers();
    }
    @GetMapping("/veniceData")
    public List<String> getVeniceData() throws IOException {
        return serv.getVeniceData();
    }
    @GetMapping("/top3AirportsWithDelay")
    public List<String> getTop3AirportsWithDelay() throws IOException {
        return serv.getTop3AirportsWithDelay();
    }

}
