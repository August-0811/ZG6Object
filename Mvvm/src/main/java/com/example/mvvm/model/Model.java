package com.example.mvvm.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Model
 * @Description TODO
 * @Author 崔俊杰
 * @Date 2021/8/18 0018 14:38
 * @Version 1.0
 *Retention注解有一个属性value是RetentionPolicy类型的
 *RUNTIME表示注解不仅被保存在class文件中，jvm加载clss文件之后,仍然存在
 * ElementType 这个枚举类型的常量提供了一个简单的分类：
 *注释可能出现在Java程序中的语法位置（这些常量与元注释类型(@Target)一起指定在何处写入注释的合法位置）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Model {
    String value() default "";
}
