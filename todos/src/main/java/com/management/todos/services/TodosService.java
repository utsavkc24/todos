package com.management.todos.services;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.management.todos.data.TodoRepository;
import com.management.todos.model.Todos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
public class TodosService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login-page";
    }

    @GetMapping(path = "/main")
    public String getHome(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        return "home-page";
    }

    @GetMapping(path = "/addingTodos")
    public String addTodo(Model model) {
        Todos todos = new Todos();
        model.addAttribute("todos", todos);
        return "add-todos";
    }

    @PostMapping("/addingTodos")
    public String addingTodo(@Valid Todos todos, BindingResult result) {
        if (result.hasErrors()) {
            return "add-todos";
        }
        todoRepository.save(todos);
        return "redirect:/main";
    }

    @GetMapping("/updatingTodo/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Todos todos = todoRepository.getById(id);
        model.addAttribute("todos", todos);
        return "update-todo";
    }

    @PostMapping("/update/{id}")
    public String updateTodos(@PathVariable("id") int id, @Valid Todos todos,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "update-todo";
        }
        todos.setId(id);
        todoRepository.save(todos);
        return "redirect:/main";
    }

    @GetMapping(value = "/delete-todo")
    public String deleteTodo(@RequestParam int id, ModelMap model) {
        Optional<Todos> todo = todoRepository.findById(id);
        todoRepository.delete(todo.get());
        return "redirect:/main";
    }
}
