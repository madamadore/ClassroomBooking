package it.tecnosphera.booking.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.repository.AulaRepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value="/aula")
public class AulaController  {

	@Autowired
	AulaRepositoryInterface aulaRepository;
	
    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public String editAula(@PathVariable String id, Model model) {
    	Aula aula = aulaRepository.getAula(Long.parseLong(id));
        model.addAttribute("aula", aula);
        return "aula/edit";
    }
  
    @RequestMapping(value="/view/{id}", method = RequestMethod.GET)
    public String viewAula(@PathVariable String id, Model model) {
    	Aula aula = aulaRepository.getAula(Long.parseLong(id));
        model.addAttribute("aula", aula);
        return "aula/view";
    }
    
    @RequestMapping(value="/new")
    public String newAula(Model model) {
    	model.addAttribute(new Aula());
        return "aula/edit";
    }       
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String viewAula(@PathVariable String id, Model model, List<Aula> aulas ) {
    	List<Aula> aule = aulaRepository.getAulas();
        model.addAttribute("aule", aule);
        return "aula/list";
    }

}
