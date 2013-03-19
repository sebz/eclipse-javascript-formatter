package jp.sourceforge.pdt_tools.jsbeautifier.handlers;

import java.util.LinkedList;
import java.util.List;

import jp.sourceforge.pdt_tools.jsbeautifier.Activator;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;
import jp.sourceforge.pdt_tools.jsbeautifier.preferences.PreferenceConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class CommandUtil {

	/**
	 * 
	 * @param project
	 * @return JSBeautifierOptions
	 */
	public static JSBeautifierOptions getOptions(IProject project) {
		IPreferencesService preference = Platform.getPreferencesService();
		IScopeContext[] contexts = getScopeContext(project);

		JSBeautifierOptions options = new JSBeautifierOptions();
		options.indent_size = preference.getInt(Activator.PLUGIN_ID,
				PreferenceConstants.INDENT_SIZE, options.indent_size, contexts);
		options.indent_char = preference.getString(Activator.PLUGIN_ID,
				PreferenceConstants.INDENT_CHAR, options.indent_char, contexts);
		options.preserve_newlines = preference.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.PRESERVE_NEWLINES,
				options.preserve_newlines, contexts);
		options.max_preserve_newlines = preference.getInt(Activator.PLUGIN_ID,
				PreferenceConstants.MAX_PRESERVE_NEWLINES,
				options.max_preserve_newlines, contexts);
		options.jslint_happy = preference.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.JSLINT_HAPPY, options.jslint_happy,
				contexts);
		options.brace_style = preference.getInt(Activator.PLUGIN_ID,
				PreferenceConstants.BRACE_STYLE, options.brace_style, contexts);
		options.keep_array_indentation = preference.getBoolean(
				Activator.PLUGIN_ID,
				PreferenceConstants.KEEP_ARRAY_INDENTATION,
				options.keep_array_indentation, contexts);
		options.unescape_strings = preference.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.UNESCAPE_STRINGS, options.unescape_strings,
				contexts);
		return options;
	}

	/**
	 * 
	 * @param project
	 * @return new text file line delimiter
	 */
	public static String getLineDelimiter(IProject project) {
		IScopeContext[] contexts = getScopeContext(project);
		String separator = Platform.getPreferencesService().getString(
				Platform.PI_RUNTIME, Platform.PREF_LINE_SEPARATOR, "\n",
				contexts);
		return separator;
	}

	/**
	 * 
	 * @return target file extensions
	 */
	public static String[] getFileExtensions() {
		IPreferencesService preference = Platform.getPreferencesService();
		IScopeContext[] contexts = { InstanceScope.INSTANCE };
		String extensions = preference.getString(Activator.PLUGIN_ID,
				PreferenceConstants.FILE_EXTENSIONS, "js", contexts);
		List<String> fileExtensions = new LinkedList<String>();
		for (String fileExtension : extensions.split(",")) {
			fileExtension = fileExtension.trim();
			if (fileExtension.length() > 0) {
				if (fileExtension.startsWith("*")) {
					fileExtension = fileExtension.substring(1);
				}
				if (fileExtension.startsWith(".")) {
					fileExtension = fileExtension.substring(1);
				}
				if (fileExtension.length() > 0) {
					fileExtensions.add(fileExtension);
				}
			}
		}
		return fileExtensions.toArray(new String[fileExtensions.size()]);
	}

	private static IScopeContext[] getScopeContext(IProject project) {
		if (project != null) {
			return new IScopeContext[] { new ProjectScope(project),
					InstanceScope.INSTANCE };
		} else {
			return new IScopeContext[] { InstanceScope.INSTANCE };
		}
	}

}
