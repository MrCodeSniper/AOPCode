package mrcodesniper.me.apt_api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Retention
 * SOURCE：不参与编译，让开发者使用。
 * CLASS：参与编译，运行时不可见。给编译器使用。
 * RUNTIME：参与编译，运行时可见。给编译器和JVM使用。
 *
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BV {
    int value();// int值为控件ID
}
