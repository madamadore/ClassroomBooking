package it.tecnosphera.booking.classroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.tecnosphera.booking.classroom.model.Aula;

@Controller
@RequestMapping(value="/aula")
public class AulaController  {

	protected Aula loadAulaFromDatabase() {
		Aula aula = new Aula();
		aula.setId(1L);
		aula.setName("Aula Corsi");
		aula.setDescription("Aula al sesto piano, San Donato (MI)");
		return aula;
	}
	
    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public String editAula(@PathVariable String id, Model model) {
    	Aula aula = loadAulaFromDatabase();
        model.addAttribute("aula", aula);
        return "aula/edit";
    }
  
    @RequestMapping(value="/view/{id}", method = RequestMethod.GET)
    public String viewAula(@PathVariable String id, Model model) {
    	Aula aula = loadAulaFromDatabase();
        model.addAttribute("aula", aula);
        return "aula/view";
    }
       
}
