package com.example.demo.controller;


import com.example.demo.exceptions.ApplicationException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/sign-up")

    public String showSignUpPage(User user){
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(@Valid User user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "add-user";
        }
        model.addAttribute("user",user);
        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showUserList(Model model){
        model.addAttribute("users", userRepository.findAll());
        //public ModelAndView showList (Model model){
        // model.addAttribute("users", userRepository.findAll());
        // return new ModelAndView("index","users", model)}
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return "update-form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-form";
        }

        userRepository.save(user);
//        model.addAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }

}
