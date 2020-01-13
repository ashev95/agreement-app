package config.valdiator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;

public class JSAttributeValidator extends BasicAttributeValidator {

    private String script;
    private HashMap<String, String> input;

    @Override
    public boolean validate() {
        boolean result = false;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            result = (Boolean) engine.eval(getPreparedScript());
        } catch (ScriptException e) {
            e.printStackTrace();
            //logging
            setMessage("Ошибка при выполнении скрипта");
        }
        return result;
    }

    private String getPreparedScript(){
        String result = script.replaceAll("let ", "var ")
                .replaceAll("return", "");
        for (String fieldName : input.keySet()){
            result = result.replaceAll("\\{" + fieldName + "\\}", input.get(fieldName));
        }
        return result;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public HashMap<String, String> getInput() {
        return input;
    }

    public void setInput(HashMap<String, String> input) {
        this.input = input;
    }
}
