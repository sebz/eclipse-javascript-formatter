package jp.sourceforge.pdt_tools.jsbeautifier.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "jp.sourceforge.pdt_tools.jsbeautifier.preferences.messages"; //$NON-NLS-1$

	public static String PreferencePage_group_title;
	public static String OptionBraceStyle_label;
	public static String OptionBraceStyle_collapse;
	public static String OptionBraceStyle_expand;
	public static String OptionBraceStyle_expand_strict;
	public static String OptionBraceStyle_end_expand;
	public static String OptionIndentSize_label;
	public static String OptionIndentChar_label;
	public static String OptionIndentChar_space_label;
	public static String OptionIndentChar_tab_label;
	public static String OptionPreserveNewlines_label;
	public static String OptionMaxPreserveNewlines_label;
	public static String OptionJslintHappy_label;
	public static String OptionBreakChainedMethods_label;
	public static String OptionKeepArrayIndentation_label;
	public static String OptionSpaceBeforeConditional_label;
	public static String OptionUnescapeStrings_label;

	public static String ConfigureWorkspaceSettings_label;
	public static String ConfigureProjectSettings_label;
	public static String EnableProjectSettings_label;

	public static String ProjectSelectionDialog_title;
	public static String ProjectSelectionDialog_desciption;
	public static String ProjectSelectionDialog_filter;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
