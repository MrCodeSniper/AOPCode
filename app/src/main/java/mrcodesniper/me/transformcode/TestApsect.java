package mrcodesniper.me.transformcode;

import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


/**
 * 切面类
 * JoinPoint 包含了切点的所有信息
 */
@Aspect
public class TestApsect {

    //创建切点函数
    //mrcodesniper.me.transformcode.MainActivity类路径下的 所有test方法
//    @Pointcut("execution(* mrcodesniper.me.transformcode.MainActivity.test(..))")
//    public void pointcut() {
//
//    }


    //通过寻找注解找到函数
    @Pointcut("execution(@mrcodesniper.me.transformcode.TestAnnoTrace * *(..))")
    public void pointcut() {}



    //在函数指向前织入
    @Before("pointcut()")
    public void beforeTest(JoinPoint joinPoint){
        //获得函数签名
        MethodSignature signature= (MethodSignature) joinPoint.getSignature();
        String methodName=signature.getName();//得到方法名
        Method method=signature.getMethod();//得到方法实例
        Class returntype=signature.getReturnType();// 返回值类型：void
        Class declaringType = signature.getDeclaringType(); // 方法所在类名：MainActivity
        String[] parameterNames = signature.getParameterNames(); // 参数名：view
        Class[] parameterTypes = signature.getParameterTypes(); // 参数类型：View
        Log.d("chenhong","@before");

        // 通过Method对象得到切点上的注解属性
        TestAnnoTrace annotation = method.getAnnotation(TestAnnoTrace.class);
        String value = annotation.value();
        int type = annotation.type();
        Log.d("chenhong","string:"+value+"type:"+type);
    }


    //around会阻断函数  需要继续运行joinPoint.proceed()
    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = SystemClock.currentThreadTimeMillis();
        Log.d("chenhong","@Around");
        joinPoint.proceed();// 目标方法执行完毕
        long endTime = SystemClock.currentThreadTimeMillis();
        long dx = endTime - beginTime;
        Log.d("chenhong","耗时：" + dx + "ms");
    }

    //在函数末尾织入
    @After("pointcut()")
    public void afterTest(JoinPoint joinPoint){
        Log.d("chenhong","@after");
    }

    //在返回值之后 织入
    @AfterReturning("pointcut()")
    public void afterReturn(JoinPoint joinPoint,Object returnV){
        Log.d("chenhong","@afterReturn");
    }

    //报错时织入
    @AfterThrowing(value = "pointcut()",throwing = "ex")
    public void afterThrow(Throwable ex){
        Log.d("chenhong","@afterThrowing");
        Log.e("chenhong","ex="+ex.getMessage());
    }





}
