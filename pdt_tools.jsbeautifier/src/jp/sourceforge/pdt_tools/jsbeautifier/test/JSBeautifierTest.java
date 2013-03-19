package jp.sourceforge.pdt_tools.jsbeautifier.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;

public class JSBeautifierTest {

	private static JSBeautifierTest instance = new JSBeautifierTest();

	private ScriptEngine scriptEngine;
	private String beautify_js;
	private boolean ready = false;

	private JSBeautifierTest() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
		try {
			beautify_js = getScript();
			scriptEngine.eval(beautify_js);
			// Compilable compilable = (Compilable) scriptEngine;
			// CompiledScript compiledScript = compilable.compile(beautify_js);
			// compiledScript.eval();
			ready = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public static JSBeautifierTest getInstance() {
		return instance;
	}

	public void test(String script, String formatted,
			JSBeautifierOptions options) {
		try {
			String result = js_beautify(script, makeOptions(options));
			if (!result.equals(formatted)) {
				System.out.println("Different!");
				System.out.println(result);
				System.out.println("---");
				System.out.println(formatted);
			} else {
				System.out.println("OK");
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private String js_beautify(String text, String options)
			throws ScriptException, NoSuchMethodException {
		String string = null;
		if (ready) {
			String script = "function js_beautify_bridge(text) {var arg="
					+ options + ";return js_beautify(text, arg);}";
			scriptEngine.eval(script);
			// Compilable compilable = (Compilable) scriptEngine;
			// CompiledScript compiledScript = compilable.compile(script);
			// compiledScript.eval();
			Invocable inv = (Invocable) scriptEngine;
			Object result = inv.invokeFunction("js_beautify_bridge", text);
			if (result instanceof String) {
				string = (String) result;
			}
		}
		return string;
	}

	private String getScript() throws IOException {
		StringBuffer script = new StringBuffer();
		URL url = new URL(
				"platform:/plugin/jp.sourceforge.pdt_tools.jsbeautifier/jsbeautifier/beautify.js");
		InputStream inputStream = url.openConnection().getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null) {
			script.append(line + "\n");
		}
		reader.close();
		return script.toString();
	}

	private String makeOptions(JSBeautifierOptions options) {
		JSBeautifierOptions defaults = new JSBeautifierOptions();
		List<String> list = new LinkedList<String>();

		if (!options.indent_char.equals(defaults.indent_char)) {
			list.add("'indent_char': '" + options.indent_char + "'");
		}
		if (options.indent_size != defaults.indent_size) {
			list.add("indent_size: " + options.indent_size);
		}
		if (options.brace_style != defaults.brace_style) {
			String[] name = { "collapse", "expand", "expand-strict",
					"end-expand" };
			list.add("'brace_style': '" + name[options.brace_style] + "'");
		}
		if (options.preserve_newlines != defaults.preserve_newlines) {
			list.add("'preserve_newlines': " + options.preserve_newlines);
		}
		if (options.break_chained_methods != defaults.break_chained_methods) {
			list.add("'break_chained_methods': "
					+ options.break_chained_methods);
		}
		if (options.max_preserve_newlines != defaults.max_preserve_newlines) {
			list.add("'max_preserve_newlines': "
					+ options.max_preserve_newlines);
		}
		if (options.jslint_happy != defaults.jslint_happy) {
			list.add("'jslint_happy': " + options.jslint_happy);
		}
		if (options.keep_array_indentation != defaults.keep_array_indentation) {
			list.add("'keep_array_indentation': "
					+ options.keep_array_indentation);
		}
		if (options.space_before_conditional != defaults.space_before_conditional) {
			list.add("'space_before_conditional': "
					+ options.space_before_conditional);
		}
		if (options.unescape_strings != defaults.unescape_strings) {
			list.add("'unescape_strings': " + options.unescape_strings);
		}

		StringBuffer buf = new StringBuffer();
		for (String element : list) {
			buf.append(element).append(",");
		}
		return "{" + buf.toString() + "}";
	}
}
