package com.abdrakhimov.demo_corona.controllers;

import com.abdrakhimov.demo_corona.models.Country;
import com.abdrakhimov.demo_corona.models.LocationStats;
import com.abdrakhimov.demo_corona.services.CoronaVirusServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {


    private final CoronaVirusServices coronaVirusServices;

    @GetMapping("/")
    public String home(Model model) {
        List<Country> allStats = coronaVirusServices.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getStats()).sum();
        int totalCasesYesterday = allStats.stream().mapToInt(stat -> stat.getPrevStats()).sum();
        int diffFromPrevDay = totalCases - totalCasesYesterday;
        model.addAttribute("countries", allStats);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("diffFromPrevDay", diffFromPrevDay);
        return "home";
    }
}
