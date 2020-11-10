package cz.kksffn.notebook.helpers;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ivohr
 */
@Named
@RequestScoped
public class RequestTools {
    
    
    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    private String uri = request.getRequestURI();
    private String url = request.getRequestURL().toString();
    private String pageName;
    private String classForNavigation;
    private Logger logger = LoggerFactory.getLogger(RequestTools.class);;
    
    @Inject
    SessionTools session;

    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getAppName(){
        String uriWithoutLastSegment = uri.substring(0, uri.lastIndexOf('/'));
        return uriWithoutLastSegment.substring(uriWithoutLastSegment.lastIndexOf('/') + 1);
    }
    public String getPageName(){
        pageName = getAppName();
        logger.debug(pageName);
        switch (pageName) {
            case "todo":
                pageName = "todoName";
                break;
            case "blog":
                pageName = "blogName";
                break;
            case "photo":
                pageName = "photoName";
                break;                
            default:
                pageName = "notebookName";                
                break;
        }
        ResourceBundle messages = ResourceBundle
            .getBundle("cz.kksffn.notebook.messages", new Locale(session.getLanguage()));    
        return messages.getString(pageName);
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getClassForNavigation(String linkName) {
        if (linkName.equals(pageName)) return "selected";
        return "";
    }

    public void setClassForNavigation(String classForNavigation) {
        this.classForNavigation = classForNavigation;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public SessionTools getSession() {
        return session;
    }

    public void setSession(SessionTools session) {
        this.session = session;
    }
    
    
    
    
}
