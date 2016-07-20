package it.tecnosphera.booking.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxController {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;

//	@RequestMapping(value = "/prenotazioni", method = RequestMethod.GET)
//	public @ResponseBody String elaboraPrenotazioni() {
//		StringBuilder returnText = new StringBuilder();
//		Iterator<Prenotazione> i = prenotazioneRepository.findAll().iterator();
//		returnText.append("[");
//		while (i.hasNext()) {
//			Prenotazione p = i.next();
//			returnText.append("{");
//			returnText.append("title:'" + p.getDescription() + "',");
//			returnText.append("start:'" + p.getStart() + "',");
//			returnText.append("end:'" + p.getEnd() + "'");
//			returnText.append("}");
//			if (i.hasNext()) {
//				returnText.append(",");
//			}
//		}
//		returnText.append("]");
//		return returnText.toString();
//	}
	
	@RequestMapping(value = "/prenotazioni", method = RequestMethod.GET)
	public @ResponseBody List<Prenotazione> elaboraPrenotazioni() {
		return prenotazioneRepository.findAll();
	}
}
