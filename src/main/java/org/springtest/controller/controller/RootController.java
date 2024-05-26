package org.springtest.controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springtest.service.service.UserDetailsServiceImpl;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class RootController {

    private final UserDetailsServiceImpl userService;

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView root() {
        return new ModelAndView("note/index");
    }

    @RequestMapping(value = "/note", method = {RequestMethod.GET})
    public ModelAndView rootNotes() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        ModelAndView result = new ModelAndView("note/notes");
        result.addObject("username", userDetails.getUsername());
        return result;
    }
}
