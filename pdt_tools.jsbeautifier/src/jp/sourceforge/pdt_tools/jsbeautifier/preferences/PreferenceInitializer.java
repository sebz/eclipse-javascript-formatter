package jp.sourceforge.pdt_tools.jsbeautifier.preferences;

import jp.sourceforge.pdt_tools.jsbeautifier.Activator;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.BRACE_STYLE,
				JSBeautifierOptions.BS_COLLAPSE);
		store.setDefault(PreferenceConstants.INDENT_SIZE, 4);
		store.setDefault(PreferenceConstants.INDENT_CHAR, " ");
		store.setDefault(PreferenceConstants.PRESERVE_NEWLINES, true);
		store.setDefault(PreferenceConstants.MAX_PRESERVE_NEWLINES, 0);
		store.setDefault(PreferenceConstants.JSLINT_HAPPY, false);
		store.setDefault(PreferenceConstants.BREAK_CHAINED_METHODS, false);
		store.setDefault(PreferenceConstants.KEEP_ARRAY_INDENTATION, false);
		store.setDefault(PreferenceConstants.SPACE_BEFORE_CONDITIONAL, true);
		store.setDefault(PreferenceConstants.UNESCAPE_STRINGS, false);

		store.setDefault(PreferenceConstants.FILE_EXTENSIONS, "js");
	}

}
