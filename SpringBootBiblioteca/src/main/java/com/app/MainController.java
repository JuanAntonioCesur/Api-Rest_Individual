/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app;

import java.util.Optional;
import models.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author paco
 */

@Controller
public class MainController {
    @Autowired 
    private LibroRepository repositorio;    
    
    @GetMapping(path="/")
    public String index() {
        return "vista-inicio";
    }

    @GetMapping(path="/all")
    @ResponseBody
    public Iterable<models.Libro> getAllLibros() {
        System.out.println(repositorio.count());
        System.out.println(repositorio.toString());
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
    }   
    
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Optional<Libro> getOneLibro( @PathVariable Integer id) {
        return repositorio.findById(id);
    }    
    
    @RequestMapping(path="/biblioteca", method = RequestMethod.GET)
    public String listado(Model pagina) {
        pagina.addAttribute("biblioteca",repositorio.findAll(Sort.by(Sort.Direction.ASC, "nombre")));
        return "vista-biblioteca";
    }   
    
    @RequestMapping(path="/biblioteca/JSON", method = RequestMethod.GET)
    public String listadoJSON(Model pagina) {
        pagina.addAttribute("biblioteca",repositorio.findAll(Sort.by(Sort.Direction.ASC, "nombre")));
        return "vista-biblioteca-JSON";
    }

    @RequestMapping(path="/disponibles", method = RequestMethod.GET)
    public String disponibles(Model pagina) {
        pagina.addAttribute("biblioteca",repositorio.findByEstado("disponible"));
        return "vista-biblioteca";
    } 
    
    @RequestMapping(path="/categoria/{categoria}", method = RequestMethod.GET)
    public String categoria(@PathVariable String categoria,Model pagina) {
        pagina.addAttribute("biblioteca",repositorio.findByCategoria(categoria));
        return "vista-biblioteca";
    }
    
    @RequestMapping(path="/autor/{autor}", method = RequestMethod.GET)
    public String autor(@PathVariable String autor,Model pagina) {
        pagina.addAttribute("biblioteca",repositorio.findByAutor(autor));
        return "vista-biblioteca";
    }
    
    @RequestMapping(path="/biblioteca/{id}", method = RequestMethod.GET)
    public String verLibro(@PathVariable Integer id, Model pagina2) {            
        pagina2.addAttribute("libro",repositorio.findById(id).get() );
        return "vista-libro";
    }
    
    @RequestMapping(path="/biblioteca/JSON/{id}", method = RequestMethod.GET)
    public String verLibroJSON(@PathVariable Integer id, Model pagina2) {            
        pagina2.addAttribute("libro",repositorio.findById(id).get() );
        return "vista-libro-JSON";
    } 
    

	
	@ResponseBody
        @RequestMapping(value = "/guardarLibro", method = RequestMethod.POST)
        public Libro guardarLibro(@ModelAttribute Libro libro, Model model) {
        repositorio.save(libro);
        return libro;
	}
        
    @PostMapping("/nuevo")
    public ResponseEntity<Libro> createBook(@RequestBody Libro libro) {
        Libro savedBook = repositorio.save(libro);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }
/*
    @ResponseBody
    @RequestMapping(value = "/guardarLibro", method = RequestMethod.POST)
    public Libro guardarLibro(@ModelAttribute Libro libro, Model model) {
        repositorio.save(libro);
        return libro;
    }
*/
    
}
