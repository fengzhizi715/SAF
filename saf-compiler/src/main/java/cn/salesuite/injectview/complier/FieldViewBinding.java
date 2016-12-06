package cn.salesuite.injectview.complier;

import javax.lang.model.type.TypeMirror;

/**
 * Created by Tony Shen on 2016/12/6.
 */

public class FieldViewBinding {

    public String name;
    public TypeMirror type;
    public int resId;

    public FieldViewBinding(String name, TypeMirror type, int resId) {
        this.name = name;
        this.type = type;
        this.resId = resId;
    }
}
