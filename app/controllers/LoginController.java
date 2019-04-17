package controllers;

import java.util.Map;

import play.mvc.*;

public class LoginController extends Controller {
 
    public Result login() {
    	return ok(views.html.login.render());
    }
    
    public Result dados_login() {
    	Map<String, String[]> r = request().body().asFormUrlEncoded();
    	String email = r.get("email")[0];
    	String senha = r.get("senha")[0];
    	if (email.equals(senha)) {
    		session("email", email);
    		return redirect("/");
    	}
    	return redirect("/login");
    }
    
    public Result logout() {
    	session().clear();
    	return redirect("/login");
    }

}
