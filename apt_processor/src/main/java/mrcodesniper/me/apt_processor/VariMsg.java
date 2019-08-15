package mrcodesniper.me.apt_processor;

import java.util.HashMap;

import javax.lang.model.element.VariableElement;

public class VariMsg {

    /*包名*/
    private String pk;
    /*类名*/
    private String clsName;
    /*注解对象*/
    private HashMap<Integer, VariableElement> varMap;//id对应一个变量元素


    public VariMsg(String pk, String clsName) {
        this.pk = pk;
        this.clsName = clsName;
    }

    public HashMap<Integer, VariableElement> getVarMap() {
        return varMap;
    }
}
