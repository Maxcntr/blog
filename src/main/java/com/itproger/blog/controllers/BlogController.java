package com.itproger.blog.controllers;

import com.itproger.blog.models.Post;
import com.itproger.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;
    //функция для кнопки "БЛОГ" и вывод странички "blog-main"
    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }
    //фунция для кнопки "ДОБАВИТЬ СТАТЬЮ" и вывод странички "blog-add"
    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }
    // функция добавления в базу данных со странички "blog"
    @PostMapping("/blog/add") //title из файла blog-add и параметра name="title" и т.д.
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post= new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }
    // функция обработки кнопки "ДЕТАЛЬНЕЕ" и вывод на страничку "blog-details"
    @GetMapping("/blog/{id}")  // "/blog/{id}" и (value = "id") должны совпадать id "название"
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {

        //проверка, чтоб не выводить строницу, которой нет в базе данных
        //и перенаправить по адресу return "redirect:/blog"; можно ввести желаемый адрес
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";
    }
    //функцич для обработки "РЕДАКТИРОВАНИЕ"
    @GetMapping("/blog/{id}/edit")  // "/blog/{id}" и (value = "id") должны совпадать id "название"
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {

        //проверка, чтоб не выводить строницу, которой нет в базе данных
        //и перенаправить по адресу return "redirect:/blog"; можно ввести желаемый адрес
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }
    //функция для обработки данных со страницы "РЕДАКТИРОВАНИЯ"
    @PostMapping("/blog/{id}/edit") //title из файла blog-add и параметра name="title" и т.д.
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }
    //функция для обработки данных со страницы "РЕДАКТИРОВАНИЯ"
    @PostMapping("/blog/{id}/remove") //title из файла blog-add и параметра name="title" и т.д.
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
