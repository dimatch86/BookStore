package com.example.bookshop.controllers;

import com.example.bookshop.model.dto.StatusDto;
import com.example.bookshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class ChangeBookStatusAuthorizedController extends CommonAttributeHandler{
    private final UserService userService;


    @PostMapping("/changeBookStatus/authorized/{slug}")
    @ResponseBody
    public String changeBookStatusAuthenticated(@PathVariable("slug") String slug,
                                                @RequestBody StatusDto statusDto) {

        userService.changeBookStatus(slug, statusDto.getStatus());
        return "redirect:/books/" + slug;
    }

    @GetMapping("authorized/cart")
    public String handleCartRequestAuthorized(Model model) {
        model.addAttribute("cartBooks", userService.getBooksFromCart());
        return "/cart";
    }

    @GetMapping("authorized/postponed")
    public String handlePostponedRequestAuthorized(Model model) {
        model.addAttribute("postponedBooks", userService.getBooksFromPostponed());
        return "/postponed";
    }

    @PostMapping("/changeBookStatus/remove/authorized/{slug}")
    public String removeMyBook(@PathVariable("slug") String slug) {

        userService.removeBook(slug);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/move/authorized/{slug}")
    public String moveMyBook(@PathVariable("slug") String slug, @RequestBody StatusDto statusDto) {

        userService.updateBookStatus(slug, statusDto.getStatus());

        return "redirect:/books/postponed";
    }
}
