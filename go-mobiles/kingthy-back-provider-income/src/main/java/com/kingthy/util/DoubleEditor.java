package com.kingthy.util;
import org.springframework.beans.propertyeditors.PropertiesEditor;  
 
/**
 * @author:xumin
 * @Description:
 * @Date:17:25 2017/11/1
 */
public class DoubleEditor extends PropertiesEditor {    
    @Override    
    public void setAsText(String text) throws IllegalArgumentException {    
        if (text == null || "".equals(text)) {
            text = "0";    
        }    
        setValue(Double.parseDouble(text));    
    }    
    
    @Override    
    public String getAsText() {    
        return getValue().toString();    
    }    
}