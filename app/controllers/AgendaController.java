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
	private Optional<Contato> getContato(String usuario, String email) {
		return getAgenda(usuario).stream().filter(c -> c.email.equals(email)).findFirst();
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
		Optional<Contato> contato = getContato(usuario, email);
		if (contato.isPresent()) {
			Form<Contato> contatoForm = formFactory.form(Contato.class).fill(contato.get());
			return ok(views.html.editar.render(contatoForm));	
		}
		return redirect("/");
	}

	public Result salvar() {
		String usuario = session("email");
		if (usuario == null) {
			return redirect("/login");
		}
		Contato novoContato = formFactory.form(Contato.class).bindFromRequest().get();
		Optional<Contato> contato = getContato(usuario, novoContato.getEmail());
		if (contato.isPresent()) {
			contato.get().setNome(novoContato.getNome());
			contato.get().setTelefone(novoContato.getTelefone());
		}
		return redirect("/");
	}

	public Result excluir(String email) {
		String usuario = session("email");
		if (usuario == null) {
			return redirect("/login");
		}
		Optional<Contato> contato = getContato(usuario, email);
		if (contato.isPresent()) {
			getAgenda(usuario).remove(contato.get());
		}
		return redirect("/");
	}
}
