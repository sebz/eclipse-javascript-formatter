package jp.sourceforge.pdt_tools.jsbeautifier.handlers;

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.TreeSelection;

public class FileSelectionTester extends PropertyTester {

	private static final String PROPERTY = "fileSelected";
	private static final String ARG_ATTR = "attr";

	private static String[] extensions = null;

	public FileSelectionTester() {
		if (extensions == null) {
			extensions = CommandUtil.getFileExtensions();
		}
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (PROPERTY.equals(property)) {
			boolean checkAttr = false;
			if (args.length > 0 && args[0] instanceof String
					&& args[0].equals(ARG_ATTR)) {
				checkAttr = true;
			}
			if (receiver instanceof TreeSelection) {
				Iterator<?> it = ((TreeSelection) receiver).iterator();
				while (it.hasNext()) {
					Object element = it.next();
					if (element instanceof IFile) {
						IFile file = (IFile) element;
						if (isTarget(file)) {
							if (checkAttr && file.isReadOnly()) {
								continue;
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param file
	 * @return has target extension
	 */
	public static boolean isTarget(IFile file) {
		String extension = file.getFileExtension().toLowerCase();
		return Arrays.binarySearch(extensions, extension) >= 0;
	}

}
