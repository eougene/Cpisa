package net.dot.com.cpisa.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by e-dot on 2017/10/20.
 * http://blog.csdn.net/github_35180164/article/details/52118286(注解案例)
 * <p>
 * 适用类、接口（包括注解类型）或枚举
 *
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target(ElementType.TYPE) public @interface ClassInfo {...}
 * 适用field属性，也包括enum常量
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target(ElementType.FIELD) public @interface FieldInfo {...}
 * 适用方法
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target(ElementType.METHOD) public @interface MethodInfo {...}
 * <p>
 * 自定义注解
 * 1、RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
 * 2、RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
 * 3、RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
 * <p>
 * 这3个生命周期分别对应于：Java源文件(.java文件) ---> .class文件 ---> 内存中的字节码。
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PresenterInject {
    Class<? extends BasePresenter> value();
}
