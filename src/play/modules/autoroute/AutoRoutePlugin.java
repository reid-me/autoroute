package play.modules.autoroute;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.mvc.Router;
import play.mvc.Router.Route;

public class AutoRoutePlugin extends PlayPlugin {
	
	/**
	 * example 
	 * request : /users/1000
	 * auto generate : /users/1000.json
	 */
	 private static final String TARGET_PATTERN = "(/[a-zA-Z0-9]+)$";

    public void onApplicationStart() {
    }
    
    @Override
    public void onRoutesLoaded() {
    	
    	String formats;

    	formats = (String) Play.configuration.get("autoroute.format");
    	if (formats == null) return;
    	
    	Logger.debug("configuration format : %s", formats);
    	
    	String[] extensions;
    	extensions = formats.split(",");
        Logger.debug("auto routes loaded ");

        //final String[] extensions = {"json", "xml"};
        
        List<AutoRoute> candidates = new ArrayList<AutoRoute>();
        for (Route r : Router.routes) {
                Logger.debug("### route action : %s, method :%s, path :%s", r.action, r.method, r.path);
                if (r.method.equals("GET") && r.path.matches(TARGET_PATTERN)) {
                    //Route route = null;
                    for (String format : extensions) {
                    	//route = Router.getRoute("GET", r.path + "." + format, r.action, null, null);
                    	candidates.add(new AutoRoute(r.method, r.path, r.action, format));
                    }
                }
        }
        
        // check all routes file. and add
        for ( AutoRoute route : candidates ) {
            Router.addRoute(route.method, route.path, route.action, route.getParams(), null);
        }

    }
    
    public class AutoRoute {
    	AutoRoute(String method, String path, String action, String format) {
    		this.method = method;
    		this.path = path + "." + format;
    		this.action = action;
    		this.format = format;
    	}
    	String method;
    	String path;
    	String action;
    	String format;
    	
    	public String getParams() {
    		return String.format("(format:'%s', args)", format);
    	}
    };

}
