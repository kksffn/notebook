package cz.kksffn.notebook.helpers;


import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author ivohr
 */
@Named
@SessionScoped
public class SessionTools implements Serializable{
   
    
//=============      Language of the page    ===================================
    private String language = LanguageHelper.getDefaultLanguage();
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
//--------------   This changes language by user's choice   --------------------
    public String changeLanguage(String lang){
        if (LanguageHelper.getSupportedLanguages().contains(lang)) {
            language = lang;
        }
        return "";
    } 
    
}
