package mrcodesniper.me.apt_processor.readme;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;


/**
 * 类型元素说明
 */
public interface TypeElementClone {


        List<? extends Element> getEnclosedElements();

        NestingKind getNestingKind();//返回此类型元素的嵌套种类 成员类 本地类 匿名内部类 顶部类

        Name getQualifiedName(); //返回规范 名称。对于没有规范名称的局部类和匿名类，返回一个空名称.

        Name getSimpleName();

        TypeMirror getSuperclass();//返回此类型元素的直接超类。如果此类型元素表示一个接口或者类 java.lang.Object，则返回一个种类为 NONE 的 NoType

        List<? extends TypeMirror> getInterfaces();//返回直接由此类实现或直接由此接口扩展的接口类型

        List<? extends TypeParameterElement> getTypeParameters();//按照声明顺序返回此类型元素的形式类型参数

        Element getEnclosingElement();


    }

