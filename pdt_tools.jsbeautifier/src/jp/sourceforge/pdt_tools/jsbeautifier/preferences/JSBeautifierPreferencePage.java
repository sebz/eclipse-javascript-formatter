package jp.sourceforge.pdt_tools.jsbeautifier.preferences;

import java.util.HashSet;
import java.util.Set;

import jp.sourceforge.pdt_tools.jsbeautifier.Activator;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

public class JSBeautifierPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {

	private IAdaptable element;
	private IProject project;
	private Button projectSettings;
	private Link link;
	private Group group;

	private static final String PREF_ID = "jp.sourceforge.pdt_tools.jsbeautifier.preferences.PreferencePage";
	private static final String PROP_ID = "jp.sourceforge.pdt_tools.jsbeautifier.properties.PropertyPage";
	private static final String HIDE_LINK = "jp.sourceforge.pdt_tools.jsbeautifier.preferences.HideLink";

	public JSBeautifierPreferencePage() {
		super(GRID);
	}

	@Override
	protected Control createContents(Composite parent) {
		IScopeContext context = null;
		if (element != null) {
			Object resource = element.getAdapter(IProject.class);
			if (resource instanceof IProject) {
				project = (IProject) resource;
				context = new ProjectScope(project);
			}
		} else {
			context = InstanceScope.INSTANCE;
		}
		setPreferenceStore(new ScopedPreferenceStore(context,
				Activator.PLUGIN_ID));

		createHeader(parent);
		group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		group.setLayout(new GridLayout());
		group.setText(Messages.PreferencePage_group_title);
		Composite filler = new Composite(parent, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		return super.createContents(group);
	}

	@Override
	public void createFieldEditors() {
		Composite fields = getFieldEditorParent();
		String[][] styles = {
				{ Messages.OptionBraceStyle_collapse,
						Integer.toString(JSBeautifierOptions.BS_COLLAPSE) },
				{ Messages.OptionBraceStyle_expand,
						Integer.toString(JSBeautifierOptions.BS_EXPAND) },
				{ Messages.OptionBraceStyle_expand_strict,
						Integer.toString(JSBeautifierOptions.BS_EXPAND_STRICT) },
				{ Messages.OptionBraceStyle_end_expand,
						Integer.toString(JSBeautifierOptions.BS_END_EXPAND) } };
		addField(new ComboFieldEditor(PreferenceConstants.BRACE_STYLE,
				Messages.OptionBraceStyle_label, styles, fields));
		addField(new IntegerFieldEditor(PreferenceConstants.INDENT_SIZE,
				Messages.OptionIndentSize_label, fields, 3));
		addField(new RadioGroupFieldEditor(PreferenceConstants.INDENT_CHAR,
				Messages.OptionIndentChar_label, 1, new String[][] {
						{ Messages.OptionIndentChar_space_label, " " },
						{ Messages.OptionIndentChar_tab_label, "\t" } }, fields));
		addField(new BooleanFieldEditor(PreferenceConstants.PRESERVE_NEWLINES,
				Messages.OptionPreserveNewlines_label, fields));
		addField(new IntegerFieldEditor(
				PreferenceConstants.MAX_PRESERVE_NEWLINES,
				Messages.OptionMaxPreserveNewlines_label, fields, 3));
		addField(new BooleanFieldEditor(PreferenceConstants.JSLINT_HAPPY,
				Messages.OptionJslintHappy_label, fields));
		addField(new BooleanFieldEditor(
				PreferenceConstants.BREAK_CHAINED_METHODS,
				Messages.OptionBreakChainedMethods_label, fields));
		addField(new BooleanFieldEditor(
				PreferenceConstants.KEEP_ARRAY_INDENTATION,
				Messages.OptionKeepArrayIndentation_label, fields));
		addField(new BooleanFieldEditor(
				PreferenceConstants.SPACE_BEFORE_CONDITIONAL,
				Messages.OptionSpaceBeforeConditional_label, fields));
		addField(new BooleanFieldEditor(PreferenceConstants.UNESCAPE_STRINGS,
				Messages.OptionUnescapeStrings_label, fields));

		if (project != null) {
			enableProjectSettings(projectSettings.getSelection());
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	public IAdaptable getElement() {
		return element;
	}

	@Override
	public void setElement(IAdaptable element) {
		this.element = element;
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		if (project != null && projectSettings.getSelection()) {
			projectSettings.setSelection(false);
			enableProjectSettings(false);
		}
	}

	@Override
	public boolean performOk() {
		if (project != null && !projectSettings.getSelection()) {
			performDefaults();
		}
		return super.performOk();
	}

	@Override
	public void applyData(Object data) {
		if (data.equals(HIDE_LINK)) {
			link.setVisible(false);
		}
	}

	private void createHeader(Composite parent) {
		Composite header = new Composite(parent, SWT.NONE);
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		header.setLayout(new GridLayout(2, true));
		if (project != null) {
			projectSettings = new Button(header, SWT.CHECK);
			projectSettings.setText(Messages.EnableProjectSettings_label);
			projectSettings.setSelection(hasProjectSettings(project));
			projectSettings.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					enableProjectSettings(projectSettings.getSelection());
				}
			});
			link = new Link(header, SWT.UNDERLINE_LINK);
			link.setText("<A>" + Messages.ConfigureWorkspaceSettings_label
					+ "</A>");
			link.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PreferenceDialog dialog = PreferencesUtil
							.createPreferenceDialogOn(getShell(), PREF_ID,
									new String[] { PREF_ID }, HIDE_LINK);
					dialog.open();
				}
			});
			link.setLayoutData(new GridData(SWT.TRAIL, SWT.FILL, true, false));
		} else {
			new Label(header, SWT.NONE);
			link = new Link(header, SWT.NONE);
			link.setText("<A>" + Messages.ConfigureProjectSettings_label
					+ "</A>");
			link.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ProjectSelectionDialog select = new ProjectSelectionDialog(
							getShell(), getProjectsHavingSettings());
					if (select.open() == Window.OK) {
						Object element = select.getFirstResult();
						PreferenceDialog dialog = PreferencesUtil
								.createPropertyDialogOn(getShell(),
										(IAdaptable) element, PROP_ID,
										new String[] { PROP_ID }, HIDE_LINK);
						dialog.open();
					}
				}
			});
			link.setLayoutData(new GridData(SWT.TRAIL, SWT.FILL, true, false));
		}
	}

	private boolean hasProjectSettings(IProject project) {
		IEclipsePreferences node = new ProjectScope(project)
				.getNode(Activator.PLUGIN_ID);
		try {
			String[] keys = node.keys();
			if (keys.length > 0) {
				return true;
			}
		} catch (BackingStoreException e) {
			Activator.log(e);
		}
		return false;
	}

	private void enableProjectSettings(boolean enabled) {
		for (Control control : group.getChildren()) {
			setControl(control, enabled);
		}
	}

	private void setControl(Control control, boolean enabled) {
		if (control instanceof Combo) {
			control.setEnabled(enabled);
		} else if (control instanceof Composite) {
			for (Control children : ((Composite) control).getChildren()) {
				setControl(children, enabled);
			}
		} else {
			control.setEnabled(enabled);
		}
	}

	private Set<IProject> getProjectsHavingSettings() {
		HashSet<IProject> projects = new HashSet<IProject>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			if (hasProjectSettings(project)) {
				projects.add(project);
			}
		}
		return projects;
	}
}
