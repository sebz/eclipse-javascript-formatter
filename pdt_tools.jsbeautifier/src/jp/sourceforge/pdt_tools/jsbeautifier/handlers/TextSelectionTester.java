package jp.sourceforge.pdt_tools.jsbeautifier.handlers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.text.ITextSelection;

public class TextSelectionTester extends PropertyTester {

	private static final String PROPERTY = "textSelected";

	public TextSelectionTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (PROPERTY.equals(property)) {
			if (receiver instanceof ITextSelection) {
				return ((ITextSelection) receiver).getLength() > 0;
			}
		}
		return false;
	}

}
