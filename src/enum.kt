import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

//每个枚举常量都是一个对象。枚举常量用逗号分隔。
enum class Direction {
    NORTH, SOURTH, WEST, EAST
}


//初始化
//因为每一个枚举都是枚举类的实例，所以他们可以是这样初始化过的：

enum class Color(val rgb: Int) {
    RED(0XFF0000),
    GREEN(0X00FF00),
}


//匿名类

//枚举常量也可以声明自己的匿名类：

enum class ProtocoState {
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocoState
}

//及相应的方法、以及覆盖基类的方法。注意，如果枚举类定义任何成员，要使用分号将成员
//定义中的枚举常量定义分隔开，就像在 Java 中一样。

enum class IntArithmetics: BinaryOperator<Int>, IntBinaryOperator {
    PLUS {
        override fun apply(t: Int, u: Int): Int {
            return t + u
        }
    },

    TIMES {
        override fun apply(t: Int, u: Int) = t * u
    };

    override fun applyAsInt(left: Int, right: Int) = apply(left, right)
}


//在枚举类中实现接口

inline fun <reified T: Enum<T>> printAllValues() {
    print(enumValues<T>().joinToString { it.name })
}


fun main(args: Array<String>) {

    val a = 13
    val b = 31
    for (f in IntArithmetics.values()) {
        println("$f($a, $b) = ${f.apply(a, b)}")
    }

    printAllValues<Direction>()
}