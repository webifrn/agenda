package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		String email = session("email");
		if (email == null) {
			return redirect("/login");
		}
		Form<Contato> contatoForm = formFactory.form(Contato.class).bindFromRequest();
		Contato contato = contatoForm.get();
		getAgenda(email).add(contato);
		return redirect("/");
	}

	public Result editar(String email) {
		String usuario = session("email");
		if (usuario == null) {
			return redirect("/login");
		}
		Optional<Contato> contato = getAgenda(usuario).stream().filter(c -> c.email.equals(email)).findFirst();
		if (contato.isPresent()){
			Form<Contato> contatoForm = formFactory.form(Contato.class).fill(contato.get());
			return ok(views.html.editar.render(contatoForm));	
		}
		return redirect("/");
	}

	public Result salvar(){
		return TODO;
	}
}
