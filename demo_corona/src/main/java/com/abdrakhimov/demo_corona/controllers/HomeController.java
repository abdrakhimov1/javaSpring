package com.abdrakhimov.demo_corona.controllers;

import com.abdrakhimov.demo_corona.models.LocationStats;
import com.abdrakhimov.demo_corona.services.CoronaVirusServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {


    final CoronaVirusServices coronaVirusServices;

    public HomeController(CoronaVirusServices coronaVirusServices) {
        this.coronaVirusServices = coronaVirusServices;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusServices.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotal()).sum();
        int diffFromPrevDay = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("diffFromPrevDay", diffFromPrevDay);


        return "home";
    }
}
