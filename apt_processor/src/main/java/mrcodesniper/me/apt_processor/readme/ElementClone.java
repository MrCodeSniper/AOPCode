package mrcodesniper.me.apt_processor.readme;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;


/**
 *  Element说明接口
 */
interface ElementClone extends AnnotatedConstruct {

    TypeMirror asType(); //返回元素的类型镜像

    ElementKind getKind(); //返回枚举值 包含 包、类、接口、方法、字段等

    Set<Modifier> getModifiers();//得到修饰符枚举值 集合

    Name getSimpleName(); //返回简单名称

    Element getEnclosingElement();//返回封装此元素的最里层元素 其实为上层元素

    List<? extends Element> getEnclosedElements();//返回上层元素列表

    boolean equals(Object var1);

    int hashCode();

    List<? extends AnnotationMirror> getAnnotationMirrors();

    <A extends Annotation> A getAnnotation(Class<A> var1);//返回此元素针对指定类型的注解 比如返回@Bindview过view的注解

    <R, P> R accept(ElementVisitor<R, P> var1, P var2);


    ///////////元素子类///////////////

    TypeElement getClass_Interface();//定义的一个类或接口程序元素，相当于当前注解所在的class对象

    VariableElement field_enum_method_construct_varable_errorparam();//获取类的相关信息 变量初始化的值

    ExecutableElement classmethod_interfacemethod_construct_init();

    PackageElement packageElement();

    TypeParameterElement fanxingT();


}


