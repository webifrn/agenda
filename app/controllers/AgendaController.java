package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import models.Contato;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.FormFactory;
import play.data.Form;

public class AgendaController extends Controller {
	private Map<String, List<Contato>> agenda = new HashMap<>();

	private List<Contato> getAgenda(String email) {
		return agenda.computeIfAbsent(email, k -> new ArrayList<>());
	}

	@Inject
	FormFactory formFactory;



	public Result index() {
		String email = session("email");
		if (email == null) {
			return redirect("/login");
		}
		List<Contato> contatos = getAgenda(email);
		Form<Contato> contatoForm = formFactory.form(Contato.class);
		return ok(views.html.index.render(contatos, contatoForm));
	}

	public Result excluir(String contato) {
		String email = session("email");
		if (email == null) {
			return redirect("/login");
		}
		getAgenda(email).remove((Object) contato);
		return redirect("/");
	}

	public Result novo() {
		return TODO;
	}
}
