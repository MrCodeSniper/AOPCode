package mrcodesniper.me.transformcode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
@Target(ElementType.METHOD) //注解范畴 函数
@Retention(RetentionPolicy.RUNTIME)//运行时生效注解
public @interface TestAnnoTrace {
    String value();
    int type();
}
