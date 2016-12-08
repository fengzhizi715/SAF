package cn.salesuite.injectview.complier;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import cn.salesuite.injectview.annotations.InjectViews;

/**
 * Created by Tony Shen on 2016/12/8.
 */

public class BindViewFields {

    private VariableElement mFieldElement;
    private int[] mResIds;

    public BindViewFields(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectViews.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        InjectViews injectViews = mFieldElement.getAnnotation(InjectViews.class);
        mResIds = injectViews.ids();

        if (mResIds == null || mResIds.length==0) {
            throw new IllegalArgumentException(
                    String.format("ids() in %s for field %s is not valid !", InjectViews.class.getSimpleName(),
                            mFieldElement.getSimpleName()));
        }
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public int[] getResIds() {
        return mResIds;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}
