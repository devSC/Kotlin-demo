
//反射

//反射是这样的一组语言和库功能，它允许在运行时自省你的程序的结构。 Kotlin 让语言中的
//函数和属性做为一等公民、并对其自省（即在运行时获悉一个名称或者一个属性或函数的类
//型）与简单地使用函数式或响应式风格紧密相关。


val x = 1

val String.lastChar: Char
    get() = this[length - 1]

fun main(args: Array<String>) {

    //可调用引用
    //函数、属性以及构造函数的引用，除了作为自省程序结构外， 还可以用于调用或者用作函数
    //类型的实例。


    //函数引用

    fun isOdd(x: Int) = x % 2 != 0
    //我们可以很容易地直接调用它（ isOdd(5) ），但是我们也可以将其作为一个函数类型的值，
    //例如将其传给另一个函数。为此，我们使用 :: 操作符：

    val numbers = listOf(1, 2, 3)
    println(numbers.filter(::isOdd))
    //这里 ::isOdd 是函数类型 (Int) -> Boolean 的一个值。

    //当上下文中已知函数期望的类型时， :: 可以用于重载函数。 例如：

    fun isOdd(s: String) = s == "brilling" || s == "slith"

    println(numbers.filter(::isOdd)) /// 引用到 isOdd(x: Int)

    //或者，你可以通过将方法引用存储在具有显式指定类型的变量中来提供必要的上下文：

    //如果我们需要使用类的成员函数或扩展函数，它需要是限定的，例如 String::toCharArray 。

    //即使以扩展函数的引用初始化一个变量，其推断出的函数类型也会没有接收者（它
    //会有一个接受接收者对象的额外参数）。如需改为带有接收者的函数类型，请明确指定其类
    //型：

//    val isEmptyStringList: List<String>.() -> Boolean = List::isEmpty


    //示例：函数组合
    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
        return { x -> f(g(x))}
    }

    //它返回一个传给它的两个函数的组合： compose(f, g) = f(g(*)) 。 现在，你可以将其应用于
    //可调用引用：

    fun length(s: String) = s.length
    val oddLength = compose(::isOdd, ::length)
    val strings = listOf("a", "bc", "dfg", "213")
    println(strings.filter(oddLength))

    //属性引用
    //要把属性作为 Kotlin中 的一等对象来访问，我们也可以使用 :: 运算符：
    println(::x.get())
    println(::x.name)
    println(strings.map(String::length))

    //要访问属于类的成员的属性，我们这样限定它：
    class A(val p: Int)

    val prop = A::p
    println(prop.get(A(1000)))

    //对于扩展属性：
    println(String::lastChar.get("abcdefg"))

    //绑定的函数与属性引用
    val numberRegex = "\\d+".toRegex()
    println(numberRegex.matches("29"))

    val isNumber = numberRegex::matches
    println(isNumber("28"))

    //取代直接调用方法 matches 的是我们存储其引用。 这样的引用会绑定到其接收者上。 它可
    //以直接调用（如上例所示）或者用于任何期待一个函数类型表达式的时候：
    println(strings.filter(isNumber))

}