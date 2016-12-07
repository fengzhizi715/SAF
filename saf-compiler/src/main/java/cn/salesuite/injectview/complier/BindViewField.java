package cn.salesuite.injectview.complier;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import cn.salesuite.injectview.annotations.InjectView;

/**
 * Created by Tony Shen on 2016/12/7.
 */

public class BindViewField {
    private VariableElement mFieldElement;
    private int mResId;

    public BindViewField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectView.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        InjectView injectView = mFieldElement.getAnnotation(InjectView.class);
        mResId = injectView.id();

        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", InjectView.class.getSimpleName(),
                            mFieldElement.getSimpleName()));
        }
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}
