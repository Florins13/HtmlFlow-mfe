package htmlflow.elements;

public class HtmlBr<T, U extends HtmlBr> extends HtmlSingleElement<T, U>{
	@Override
	public final String getElementName() {
		return ElementType.BR.toString();
	}
}