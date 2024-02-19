package by.ivan101454.sweater;

import by.ivan101454.sweater.domain.Message;
import by.ivan101454.sweater.repos.MessageRepo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Slf4j
@Controller
public class GreetingController {
    @Autowired
    MessageRepo messageRepo;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "2222") String name,
                           Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String main(Model model, @RequestParam(name = "filter", required = false) String filter) {
        Iterable<Message> messages = messageRepo.findAll();

        model.addAttribute("message", new Message());
        if(!(filter==null) && !(filter.isEmpty())) {
            messages = messageRepo.findByTag(filter);
            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("messages", messages);
        }
        return "main";
    }

    @PostMapping("/")
    public String add(@ModelAttribute Message message,
                      Model model) {
        log.info(String.valueOf(message));
        messageRepo.save(message);
        model.addAttribute("message", message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam(name = "filter") String filter) {
        return "redirect:/?filter=" + filter;
    }
}

