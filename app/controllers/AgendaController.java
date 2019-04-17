package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Contato;
import play.mvc.Controller;
import play.mvc.Result;

public class AgendaController extends Controller {
	private Map<String, List<Contato>> agenda = new HashMap<>();

	private List<Contato> getAgenda(String email) {
		return agenda.computeIfAbsent(email, k -> new ArrayList<>());
	}

	public Result index() {
		String email = session("email");
		if (email == null) {
			return redirect("/login");
		}
		List<Contato> contatos = getAgenda(email);
		return ok(views.html.index.render(contatos, null));
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
