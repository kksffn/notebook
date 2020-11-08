package cz.kksffn.notebook.helpers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 *
 * @author ivohr
 */
public class LanguageHelper { 
            
    public static String getDefaultLanguage() {
        Application app = FacesContext.getCurrentInstance().getApplication();
        return app.getDefaultLocale().getLanguage();
    }
    
    public static Set<String> getSupportedLanguages() {
        Application app = FacesContext.getCurrentInstance().getApplication();
        Set<String> languageCodes = new HashSet<>();
        for (Iterator<Locale> itr = app.getSupportedLocales(); itr.hasNext();) {
          Locale locale = itr.next();
          languageCodes.add(locale.getLanguage());
        }    
        return languageCodes;
    }
    
    public static String setLanguage(String lang){
        Set<String> supportedLanguages = getSupportedLanguages();
        if (supportedLanguages.contains(lang)) {
            return lang;
        }
        return getDefaultLanguage();
    }
}
