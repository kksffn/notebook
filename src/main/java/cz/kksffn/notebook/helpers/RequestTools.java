package cz.kksffn.notebook.helpers;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
    
    public String getPageName(){
        pageName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println(pageName);
        switch (pageName) {
            case "todo.xhtml":
                pageName = "todoName";
                break;
            case "blog.xhtml":
                pageName = "blogName";
                break;
            case "photo.xhtml":
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
    
    
    
}
