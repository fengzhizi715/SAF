package cn.salesuite.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.FilerException;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import cn.salesuite.base.BaseAbstractProcessor;
import cn.salesuite.base.TypeUtils;
import cn.salesuite.base.Utils;
import cn.salesuite.router.annotations.RouterRule;

/**
 * Created by Tony Shen on 2016/12/14.
 */

@AutoService(Processor.class)
public class RouterProcessor extends BaseAbstractProcessor {

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(RouterRule.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterRule.class);

        try {
            TypeSpec type = getRouterTableInitializer(elements);
            if(type != null) {
                JavaFile.builder("cn.salesuite.router", type).build().writeTo(mFiler);
            }
        } catch (FilerException e){
            e.printStackTrace();
        } catch (Exception e) {
            Utils.error(mMessager,e.getMessage());
        }

        return true;
    }

    private TypeSpec getRouterTableInitializer(Set<? extends Element> elements) throws ClassNotFoundException {
        if(elements == null || elements.size() == 0){
            return null;
        }

        MethodSpec.Builder routerInitBuilder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(TypeUtils.CONTEXT,"context");

        routerInitBuilder.addStatement("$T.getInstance().setContext(context)",TypeUtils.ROUTER);
        routerInitBuilder.addStatement("$T options = null",TypeUtils.ROUTER_OPTIONS);

        for(Element element : elements){
            TypeElement classElement = (TypeElement) element;

            // 检测是否是支持的注解类型，如果不是里面会报错
            if (!Utils.isValidClass(mMessager,classElement,"@RouterRule")) {
                continue;
            }

            RouterRule routerRule = element.getAnnotation(RouterRule.class);
            String [] routerUrls = routerRule.url();
            int enterAnim = routerRule.enterAnim();
            int exitAnim = routerRule.exitAnim();
            if(routerUrls != null){
                for(String routerUrl : routerUrls){
                    if (enterAnim>0 && exitAnim>0) {
                        routerInitBuilder.addStatement("options = new $T()",TypeUtils.ROUTER_OPTIONS);
                        routerInitBuilder.addStatement("options.enterAnim = "+enterAnim);
                        routerInitBuilder.addStatement("options.exitAnim = "+exitAnim);
                        routerInitBuilder.addStatement("$T.getInstance().map($S, $T.class,options)",TypeUtils.ROUTER, routerUrl, ClassName.get((TypeElement) element));
                    } else {
                        routerInitBuilder.addStatement("$T.getInstance().map($S, $T.class)",TypeUtils.ROUTER, routerUrl, ClassName.get((TypeElement) element));
                    }
                }
            }
        }

        MethodSpec routerInitMethod = routerInitBuilder.build();

        return TypeSpec.classBuilder("RouterManager")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(routerInitMethod)
                .build();
    }

}
