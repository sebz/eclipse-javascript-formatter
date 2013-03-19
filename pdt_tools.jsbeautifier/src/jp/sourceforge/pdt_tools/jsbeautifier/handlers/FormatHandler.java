package jp.sourceforge.pdt_tools.jsbeautifier.handlers;

import jp.sourceforge.pdt_tools.jsbeautifier.Activator;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifier;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;
import jp.sourceforge.pdt_tools.jsbeautifier.test.JSBeautifierTest;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public class FormatHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean debug = false;
		Object trigger = event.getTrigger();
		if (trigger instanceof Event) {
			int mask = ((Event) trigger).stateMask;
			if ((mask & SWT.CTRL) != 0 && (mask & SWT.SHIFT) != 0) {
				debug = true;
			}
		}
		ITextEditor editor = null;
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (editorPart instanceof ITextEditor) {
			editor = (ITextEditor) editorPart;
		}
		if (editor == null) {
			return null;
		}
		Object obj = editor.getAdapter(IDocument.class);
		if (obj == null) {
			obj = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
		}
		if (!(obj instanceof IDocument)) {
			Activator.log(new RuntimeException("Could not get IDocument"));
			return null;
		}
		IDocument document = (IDocument) obj;
		ISelectionProvider provider = editor.getEditorSite()
				.getSelectionProvider();
		if (provider == null) {
			Activator.log(new RuntimeException(
					"Could not get ISelectionProvider"));
			return null;
		}
		ISelection selection = provider.getSelection();
		if (selection instanceof ITextSelection) {
			int offset = ((ITextSelection) selection).getOffset();
			int length = ((ITextSelection) selection).getLength();
			if (length > 0) {
				IProject project = null;
				Object resource = editor.getEditorInput().getAdapter(
						IResource.class);
				if (resource instanceof IResource) {
					project = ((IResource) resource).getProject();
				}
				JSBeautifierOptions options = CommandUtil.getOptions(project);
				try {
					String string = document.get(offset, length);
					String delimiter = document.getLineDelimiter(document
							.getLineOfOffset(offset + length - 1));
					if (delimiter == null) {
						delimiter = document.getLineDelimiter(document
								.getLineOfOffset(offset));
						if (delimiter == null) {
							delimiter = CommandUtil.getLineDelimiter(project);
						}
					}
					String formatted = new JSBeautifier().js_beautify(string,
							options);
					if (debug) {
						JSBeautifierTest.getInstance().test(string, formatted,
								options);
					}
					if (!"".equals(formatted)) {
						if (!delimiter.equals("\n")) {
							formatted = formatted.replaceAll("\n", delimiter);
						}
						if (string.endsWith(delimiter)) {
							formatted += delimiter;
						}
						if (!formatted.equals(string)) {
							document.replace(offset, length, formatted);
							editor.selectAndReveal(offset, formatted.length());
						}
					}
				} catch (BadLocationException e) {
					Activator.log(e);
				}
			}
		}
		return null;
	}

}
