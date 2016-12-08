package cn.salesuite.injectview.complier;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import cn.salesuite.injectview.annotations.InjectExtra;

/**
 * Created by Tony Shen on 2016/12/8.
 */

public class ExtraField {

    private VariableElement mFieldElement;
    private String mKey;

    public ExtraField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectExtra.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        InjectExtra injectExtra = mFieldElement.getAnnotation(InjectExtra.class);
        mKey = injectExtra.key();

        if (mKey==null || mKey=="") {
            throw new IllegalArgumentException(
                    String.format("key() in %s for field %s is not valid !", InjectExtra.class.getSimpleName(),
                            mFieldElement.getSimpleName()));
        }
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public String getKey() {
        return mKey;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}
