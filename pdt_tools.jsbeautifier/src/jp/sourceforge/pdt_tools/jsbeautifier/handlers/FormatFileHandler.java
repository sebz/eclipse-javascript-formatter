package jp.sourceforge.pdt_tools.jsbeautifier.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import jp.sourceforge.pdt_tools.jsbeautifier.Activator;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifier;
import jp.sourceforge.pdt_tools.jsbeautifier.JSBeautifierOptions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class FormatFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof TreeSelection) {
			Iterator<?> it = ((TreeSelection) selection).iterator();
			while (it.hasNext()) {
				Object element = it.next();
				if (element instanceof IFile) {
					IFile file = (IFile) element;
					if (!file.isAccessible() || file.isReadOnly()) {
						continue;
					}
					if (FileSelectionTester.isTarget(file)) {
						format(file);
					}
				}
			}
		}
		return null;
	}

	private void format(IFile file) {
		try {
			IProject project = file.getProject();
			String delimiter = CommandUtil.getLineDelimiter(project);
			String charset = file.getCharset();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					file.getContents(), charset));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				buf.append(line).append(delimiter);
			}
			reader.close();
			String script = buf.toString();
			JSBeautifierOptions options = CommandUtil.getOptions(project);
			String formatted = new JSBeautifier().js_beautify(script, options);
			if (!"".equals(formatted)) {
				if (!delimiter.equals("\n")) {
					formatted = formatted.replaceAll("\n", delimiter);
				}
				if (script.endsWith(delimiter)) {
					formatted += delimiter;
				}
				if (!formatted.equals(script)) {
					ByteArrayInputStream source = new ByteArrayInputStream(
							formatted.getBytes(charset));
					file.setContents(source, IResource.FORCE
							| IResource.KEEP_HISTORY, null);
				}
			}
		} catch (CoreException e) {
			Activator.log(e);
		} catch (IOException e) {
			Activator.log(e);
		}
	}

}
