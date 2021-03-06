package com.kingthy.util;
import org.springframework.beans.propertyeditors.PropertiesEditor;  
/**
 * @author:xumin
 * @Description:
 * @Date:11:39 2017/11/2
 */
public class LongEditor extends PropertiesEditor {    
    @Override    
    public void setAsText(String text) throws IllegalArgumentException {    
        if (text == null || "".equals(text)) {
            text = "0";    
        }    
        setValue(Long.parseLong(text));    
    }    
    
    @Override    
    public String getAsText() {    
        return getValue().toString();    
    }    
} 