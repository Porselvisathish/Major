package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;
import com.example.btba.busticketbookingapplication.dto.UserCredentialDto;
import com.example.btba.busticketbookingapplication.service.PassengerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
@AllArgsConstructor
public class AuthController {
    private PassengerService passengerService;

    /**
     * A mvc method for the login page
     *
     * @return the login view.
     */
    // http://localhost:8081/auth/login
    @GetMapping(value = "/login")
    public String home(Model model) {
        System.out.println("Rendering login page...");
        model.addAttribute("user", new UserCredentialDto());
        return "login";
    }

//    /**
//     * A mvc method for the login page
//     *
//     * @return the login view.
//     */
//    @PostMapping(value = "/perform_login")
//    public String performLogin(@ModelAttribute UserCredentialDto user) {
//        System.out.println("Validating user data...");
//        System.out.println(user.getUsername());
//        System.out.println(user.getPassword());
//        boolean isAuthenticated = passengerService.verify(user);
//        if (!isAuthenticated) {
//            System.out.println("Not authenticated. Redirecting back to login.");
//            return "redirect:/auth/login?error";
//        }
//        System.out.println("Yes, authenticated. Redirecting to home.");
//        return "redirect:/passenger/home";
//    }

    /**
     * A mvc method for the registration page
     *
     * @return the register view.
     */
    // http://localhost:8081/auth/register
    @GetMapping(value = "/register")
    public String registrationPage(Model model) {
        System.out.println("Rendering registration page...");
        model.addAttribute("newPassenger", new SavePassengerDto());
        return "register";
    }

    /**
     * A mvc method to save the new passenger
     *
     * @param newPassenger  Passenger data to be registered.
     * @param model  Model interface object.
     * @return Success or Error message on the same page based on the outcome.
     */
    @PostMapping(value = "/savePassenger") // Ashok@12345
    public String SavePassenger(@Valid @ModelAttribute SavePassengerDto newPassenger, Model model ) {
        System.out.println(newPassenger.toString());
        try{
            PassengerDto savedPassenger = passengerService.addPassenger(newPassenger);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            return "redirect:/auth/register?emailAlreadyExists";
        }
        return "redirect:/auth/register?success";
    }
}
