package mrcodesniper.me.apt_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)//java版本支持
@SupportedAnnotationTypes({"mrcodesniper.me.apt_api.Test"})//标注注解处理器支持的注解类型，就是我们刚才定义的接口Test，可以写入多个注解类型。
public class AnnotationProcessor extends AbstractProcessor {

    public Messager mMessager;
    public Elements mElements;
    public Filer mFiler;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mFiler = processingEnv.getFiler();//文件相关的辅助类
        mElements = processingEnv.getElementUtils();//元素相关的辅助类
        mMessager = processingEnv.getMessager();//日志相关的辅助类

        MethodSpec methodMain = MethodSpec.methodBuilder("main")//创建main方法
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//定义修饰符为 public static
                .addJavadoc("@  此类由apt自动生成")//在生成的代码前添加注释
                .returns(void.class)//定义返回类型
                .addParameter(String[].class, "args")//定义方法参数
                .addStatement("$T.out.println($S)", System.class, "helloWorld")//定义方法体
                .build();
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//创建HelloWorld类
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//定义修饰符为 public final
                .addMethod(methodMain)//添加方法
                .addJavadoc("@  此方法由apt自动生成")//定义方法参数
                .build();
        JavaFile javaFile = JavaFile.builder("com.tuodao.apt", helloWorld).build();// 生成源   代码
        try {
            javaFile.writeTo(mFiler);//// 在 app module/build/generated/source/apt 生成一份源代码
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}

