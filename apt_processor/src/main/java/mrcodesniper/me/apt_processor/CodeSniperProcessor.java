package mrcodesniper.me.apt_processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import mrcodesniper.me.apt_api.BV;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.PUBLIC;


/**
 * @AutoService
 * 用来告诉编译器我们定义的ButterKnifeProcessor是一个编译期注解处理器
 */
@AutoService(Processor.class)
public class CodeSniperProcessor extends AbstractProcessor {


    /**
     * 元素相关的工具类
     */
    private Elements elementUtils;
    /**
     * 文件相关的工具类
     */
    private Filer filer;
    /**
     * 日志相关的工具类
     */
    private Messager messager;
    /**
     * 类型相关工具类
     */
    private Types typeUtils;


    //内存缓存
    Map<String, VariMsg> veMap = new HashMap<>();


    //类和类中所有元素的映射
    private Map<TypeElement, List<Element>> elementPackage = new HashMap<>();

    private static final String VIEW_TYPE = "android.view.View";
    private static final String VIEW_BINDER = "com.me.codesniper.ViewInjector";


    //处理器支持的类型 bindview
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BV.class.getCanonicalName());
    }

    //处理器支持的java版本 java7
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }


    /**
     * 初始化 工具类
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    /**
     *
     * @param set 元素集合
     * @param roundEnvironment 环境
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE,"日志开始---------------");
        if (set == null || set.isEmpty()) {
            return false;
        }
        veMap.clear();
        elementPackage.clear();
        collectData(roundEnvironment.getElementsAnnotatedWith(BV.class));
        //2。根据elementPackage中的数据生成.java代码
//        generateCode();
        code(roundEnvironment);
        messager.printMessage(Diagnostic.Kind.NOTE,"日志结束---------------");
        return true;
    }

    //0。从环境中找到倍注解的元素的集合
    //1。收集数据放入elementPackage中
    private void collectData(Set<? extends Element> elements) {
        Iterator<? extends Element> iterable = elements.iterator();
        while (iterable.hasNext()) {
            Element element = iterable.next();
            TypeMirror elementTypeMirror = element.asType();//元素的类型

            TypeMirror viewTypeMirror = elementUtils.getTypeElement(VIEW_TYPE).asType();//得到view类型

            //如果注解元素的类型是view的子类或者就是view
            if (typeUtils.isSubtype(elementTypeMirror, viewTypeMirror) || typeUtils.isSameType(elementTypeMirror, viewTypeMirror)) {
                //拿到上层闭合元素即class
                TypeElement parent = (TypeElement) element.getEnclosingElement();
                /*类的绝对路径*/
                String qualifiedName = parent.getQualifiedName().toString();
                /*类名*/
                String clsName = parent.getSimpleName().toString();
                /*获取包名*/
                String packageName = processingEnv.getElementUtils().getPackageOf(parent).getQualifiedName().toString();
                //拿到类中的注解
                BV annotation = parent.getAnnotation(BV.class);
                int id = annotation.value();


                VariMsg variMsg = veMap.get(qualifiedName);
                if(variMsg==null){
                    variMsg = new VariMsg(packageName, clsName);
                    variMsg.getVarMap().put(id, (VariableElement) element);
                    //装配注解信息然后防在缓存
                    veMap.put(qualifiedName, variMsg);
                }else {
                    //拿到缓存替换内部的view映射 刷新
                    variMsg.getVarMap().put(id, (VariableElement) element);
                }


                ////////////////

                //找到父元素，这里认为是@BindView标记字段所在的类。
                //根据parent不同存储的List中
                List<Element> parentElements = elementPackage.get(parent);
                if (parentElements == null) {
                    parentElements = new ArrayList<>();
                    elementPackage.put(parent, parentElements);
                }
                parentElements.add(element);

            }else{
                throw new RuntimeException("错误处理，BV应该标注在类型是View的字段上");
            }
        }
    }



    private void generateCode(){
        Set<Map.Entry<TypeElement,List<Element>>> entries = elementPackage.entrySet();

        Iterator<Map.Entry<TypeElement,List<Element>>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<TypeElement,List<Element>> entry = iterator.next();
            //类元素
            TypeElement parent = entry.getKey();
            //当前类元素下，注解了BindView的元素
            List<Element> elements = entry.getValue();
            //通过JavaPoet生成bindView的MethodSpec
            MethodSpec methodSpec = generateBindViewMethod(parent,elements);

            String packageName = getPackage(parent).getQualifiedName().toString();

            //拿到接口的类名
            ClassName viewBinderInterface = ClassName.get(elementUtils.getTypeElement(VIEW_BINDER));
            String className = parent.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
            ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");

            try {
                //生成 className_ViewBinding.java文件
                JavaFile.builder(packageName, TypeSpec.classBuilder(bindingClassName)
                        .addModifiers(PUBLIC)
                        .addSuperinterface(viewBinderInterface)
                        .addMethod(methodSpec)
                        .build()
                ).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private MethodSpec generateBindViewMethod(TypeElement parent,List<Element> elementList) {
        ParameterSpec.Builder parameter = ParameterSpec.builder(TypeName.OBJECT, "target");
        //方法参数 方法名inject
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("inject");
        bindViewMethod.addParameter(parameter.build());
        bindViewMethod.addModifiers(Modifier.PUBLIC);
        bindViewMethod.addStatement("$T temp = ($T)target",parent,parent);
        for (Element element : elementList) {
            BV annotation = element.getAnnotation(BV.class);
            int id = annotation.value();
            bindViewMethod.addStatement("temp.$N = temp.findViewById($L)", element.getSimpleName().toString(), id);
        }
        return bindViewMethod.build();
    }


    private void code(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BV.class);
        for (Element element : elementsAnnotatedWith) {
            if (element.getKind() == ElementKind.FIELD) { //注解的为成员变量
                TypeElement typeElement = (TypeElement) element;
                PackageElement packageElement = elementUtils.getPackageOf(element);
                String packagePath = packageElement.getQualifiedName().toString();
                String className = typeElement.getSimpleName().toString();
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(packagePath + "." + className + "_ViewBinding", typeElement);
                    Writer writer = sourceFile.openWriter();
                    writer.write("package  " + packagePath + ";\n");
                    writer.write("import  " + packagePath + "." + className + ";\n");
                    writer.write("public class " + className + "_ViewBinding" + "  { \n");
                    writer.write("\n");
                    writer.append("       public " + className + "  targe;\n");
                    writer.write("\n");
                    writer.append("}");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}


