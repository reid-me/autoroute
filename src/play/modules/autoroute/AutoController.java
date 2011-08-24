package play.modules.autoroute;

import play.mvc.Controller;

public class AutoController extends Controller {

	protected static void autoRender(Object... args) {
		if (args.length > 0 && isJson() == true) {
			if (args.length == 1) {
				renderJSON(args[0]);
			} else {
				renderJSON(args);
			}
		} else if (isXml()) {
			renderXml(args);
		}
		render(args);
	}
	
	public static boolean isJson() {
		if (request.headers.get("accept").value().contains("application/json") || request.format.equals("json")) {
			return true;
		}
		return false;
	}
	
	public static boolean isXml() {
		if (request.format.equals("xml")) {
			return true;
		}
		return false;
	}


}
