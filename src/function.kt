import java.util.concurrent.locks.Lock
import kotlin.math.max

/// declare a function
fun double(x: Int): Int {
    return x * 2
}

/// default value for params
fun hello(x: String = "world") {
    println(x)
}

//“如果一个默认参数在一个无默认值的参数之前，那么该默认值只能通过使用命名参数调用该函数来使用：”
//
fun foo(bar: Int = 0, baz: Int) {

}

//“不过如果最后一个 lambda 表达式参数从括号外传给函数函数调用，那么允许默认参数不传值：”
fun foo(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {

}


//“命名参数
//
//可以在调用函数时使用命名的函数参数。当一个函数有大量的参数或默认参数时这会非常方便。”
//

fun reformat(str: String,
             normalizeCase: Boolean = true,
             upperCaseFirstLetter: Boolean = true,
             divideByCamelHumps: Boolean = false,
             wordSeparator: Char = ' ') {
}


//可变数量参数
fun foo(vararg strings: String) {

}


//“返回 Unit 的函数
//
//如果一个函数不返回任何有用的值，它的返回类型是 Unit。Unit 是一种只有一个值——Unit 的类型。这个值不需要显式返回：”
//
//Excerpt From: hltj. “Kotlin 语言官方参考文档 中文版.” iBooks.
fun printHello(name: String?): Unit {
    if (name != null) {
        println("hello ${name}")
    }
    else {
        println("Hi there")
    }
    // `return Unit` 或者 `return` 是可选的
}

//“Unit 返回类型声明也是可选的。上面的代码等同于：”
//
//Excerpt From: hltj. “Kotlin 语言官方参考文档 中文版.” iBooks.
//fun printHello(name: String?) {
//
//}

//“单表达式函数
//“当函数返回单个表达式时，可以省略花括号并且在 = 符号之后指定代码体即可：
//fun integer(x: Int): Int = x * 10

//“当返回值类型可由编译器推断时，显式声明返回类型是可选的：”
//
//Excerpt From: hltj. “Kotlin 语言官方参考文档 中文版.” iBooks.
fun integer(x: Int) = x * 10

//可变数量的参数（Varargs）
//函数的参数（通常是最后一个）可以用 vararg 修饰符标记：

fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts)
        result.add(t)
    return result
}

//中缀表示法
//标有 infix 关键字的函数也可以使用中缀表示法（忽略该调用的点与圆括号）调用。中缀函
//数必须满足以下要求：
//它们必须是成员函数或扩展函数；
//它们必须只有一个参数；
//其参数不得接受可变数量的参数且不能有默认值。
infix fun Int.shl(x: Int): Int {
    return this * x
}

//请注意，中缀函数总是要求指定接收者与参数。当使用中缀表示法在当前接收者上调用方法
//时，需要显式使用 this ；不能像常规方法调用那样省略。这是确保非模糊解析所必需的。

class MyStringCollection {
    infix fun add(s: String) {

    }

    fun build() {
        this add "abc"
        this.add("abc")

        add("abc")

//        add "abc" //error
    }
}

//泛型函数
//函数可以有泛型参数，通过在函数名前使用尖括号指定：

fun <T> singletonList(item: T) {

}


//高阶函数与 lambda 表达式
//Kotlin 函数都是头等的，这意味着它们可以存储在变量与数据结构中、作为参数传递给其他高
//阶函数以及从其他高阶函数返回。可以像操作任何其他非函数值一样操作函数


//高阶函数
//高阶函数是将函数用作参数或返回值的函数。
//一个不错的示例是集合的函数式风格的 fold )， 它接受一个初始累积值与一个接合函数，并
//通过将当前累积值与每个集合元素连续接合起来代入累积值来构建返回值：

fun <T, R> Collection<T>.fold(initial: R, combine: (acc: R, nextElement: T) -> R): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

//在上述代码中，参数 combine 具有函数类型 (R, T) -> R ，因此 fold 接受一个函数作为
//参数， 该函数接受类型分别为 R 与 T 的两个参数并返回一个 R 类型的值。 在 for -循
//环内部调用该函数，然后将其返回值赋值给 accumulator 。
//为了调用 fold ，需要传给它一个函数类型的实例作为参数，而在高阶函数调用处，（下文
//详述的）lambda 表达 式广泛用于此目的。



//函数类型
//Kotlin 使用类似 (Int) -> String 的一系列函数类型来处理函数的声明： val onClick: () ->
//Unit = …… 。

//如需将函数类型指定为可空，请使用圆括号： ((Int, Int) -> Int)? 。
//箭头表示法是右结合的， (Int) -> (Int) -> Unit 与前述示例等价，但不等于 ((Int) -> (Int)) -> Unit

//使用类型别名给函数类型起一个别称：
//typealias ClickHandler = (Button, ClickEvent) -> Unit

fun testHighFunction() {
    //带与不带接收者的函数类型非字面值可以互换，其中接收者可以替代第一个参数，反之亦
    //然。例如， (A, B) -> C 类型的值可以传给或赋值给期待 A.(B) -> C 的地方，反之亦然
    val repeat: String.(Int) -> String = { times -> repeat(times) }
    val twoParameters: (String, Int) -> String = repeat

    fun runTransformation(f: (String, Int) -> String): String {
        return f("hello", 3)
    }

    val transformationResult = runTransformation(repeat)
    println("result = $transformationResult")
}


//函数类型实例调用

//函数类型的值可以通过其 invoke(……) 操作符调用： f.invoke(x) 或者直接 f(x) 。
//如果该值具有接收者类型，那么应该将接收者对象作为第一个参数传递。 调用带有接收者的
//函数类型值的另一个方式是在其前面加上接收者对象， 就好比该值是一个扩展函
//数： 1.foo(2) ，
fun testHighFunction2() {
    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    println(stringPlus.invoke("<-", "->"))
    println(stringPlus("hello", "world"))

    println(intPlus.invoke(1, 1))
    println(intPlus(1, 2))
    println(2.intPlus(3)) // 类扩展调用
}





//Lambda 表达式与匿名函数

//lambda 表达式与匿名函数是“函数字面值”，即未声明的函数， 但立即做为表达式传递。考虑

//max(strings, { a, b -> a.length < b.length })

//函数 max 是一个高阶函数，它接受一个函数作为第二个参数。 其第二个参数是一个表达
//式，它本身是一个函数，即函数字面值，它等价于以下命名函数：

//fun compare(a: String, b: String): Boolean = a.length < b.length


//Lambda 表达式语法
//Lambda 表达式的完整语法形式如下：
//val sum = { x: Int, y: Int -> x + y }

//lambda 表达式总是括在花括号中， 完整语法形式的参数声明放在花括号内，并有可选的类型
//标注， 函数体跟在一个 -> 符号之后。如果推断出的该 lambda 的返回类型不是 Unit ，那
//么该 lambda 主体中的最后一个（或可能是单个）表达式会视为返回值。
//如果我们把所有可选标注都留下，看起来如下：

//val sum: (Int, Int) -> Int = { x, y -> x + y }

//将 lambda 表达式传给最后一个参数
//在 Kotlin 中有一个约定：如果函数的最后一个参数接受函数，那么作为相应参数传入的
//lambda 表达式可以放在圆括号之外：

//val product = items.fold(1) { acc, e -> acc * e }

//如果该 lambda 表达式是调用时唯一的参数，那么圆括号可以完全省略：

//run { println("...") }

//如果编译器自己可以识别出签名，也可以不用声明唯一的参数并忽略 -> 。 该参数会隐式声
//明为 it ：

//ints.filter { it > 0 } // 这个字面值是“(it: Int) -> Boolean”类型的

fun testLambdaFunction() {
//    max(1, 2)

    //从 lambda 表达式中返回一个值
    //我们可以使用限定的返回语法从 lambda 显式返回一个值。 否则，将隐式返回最后一个表达
    //式的值。
    //因此，以下两个片段是等价的：

    val ints = arrayListOf(1, 2, 3)

    ints.filter {
        val shouldFilter = it > 0
        shouldFilter
    }

    ints.filter {
        val shouldFilter = it > 0
        return@filter shouldFilter
    }


    //这一约定连同在圆括号外传递 lambda 表达式一起支持 LINQ-风格 的代码：
    val result = ints.filter { it > 2 }.sortedBy { it }.map { "$it" }
    println(result)

    //下划线用于未使用的变量（自 1.1 起）
    //如果 lambda 表达式的参数未使用，那么可以用下划线取代其名称：
    //map.forEach { _, value -> println("$value!") }


    ///闭包

    //Lambda 表达式或者匿名函数（以及局部函数和对象表达式） 可以访问其 闭包 ，即在外部作
    //用域中声明的变量。 与 Java 不同的是可以修改闭包中捕获的变量：
    var sum = 0
    ints.filter { it > 0 }.forEach {
        sum += it
    }
    print("lambda sum: $sum")

    //带有接收者的函数字面值
    //带有接收者的函数类型，例如 A.(B) -> C ，可以用特殊形式的函数字面值实例化—— 带有接收者的函数字面值

    //这种行为与扩展函数类似，扩展函数也允许在函数体内部访问接收者对象的成员。
    //这里有一个带有接收者的函数字面值及其类型的示例，其中在接收者对象上调用了 plus ：

    val sumFunction: Int.(Int) -> Int = { plus(it) }

    println("1.sumFunction(2): ${1.sumFunction(2)}")

    //匿名函数语法允许你直接指定函数字面值的接收者类型。 如果你需要使用带接收者的函数类型声明一个变量，并在之后使用它，这将非常有用
    val sum2 = fun Int.(other: Int): Int = this + other


    //当接收者类型可以从上下文推断时，lambda 表达式可以用作带接收者的函数字面值
    class HTML {
        fun body() {

        }
    }

    fun html(init: HTML.() -> Unit): HTML {
        val html = HTML() // 创建接收者对象
        html.init() //// 将该接收者对象传给该 lambda
        return html
    }

    html { //// 带接收者的 lambda 由此开始
        body() //调用该接收者对象的一个方法
    }

    //内联函数
    //使用高阶函数会带来一些运行时的效率损失：每一个函数都是一个对象，并且会捕获一个闭
    //包。 即那些在函数体内会访问到的变量。 内存分配（对于函数对象和类）和虚拟调用会引入
    //运行时间开销。
    //但是在许多情况下通过内联化 lambda 表达式可以消除这类的开销。 下述函数是这种情况的
    //很好的例子。即 lock() 函数可以很容易地在调用处内联。 考虑下面的情况：

//    lock(Lock()) {
//
//    }

    //非局部返回
    //在 Kotlin 中，我们可以只使用一个正常的、非限定的 return 来退出一个命名或匿名函数。
    //这意味着要退出一个 lambda 表达式，我们必须使用一个标签，并且在 lambda 表达式内部禁
    //止使用裸 return ，因为 lambda 表达式不能使包含它的函数返回：

    /*
    fun foo() {
        ordinaryFunction {
            return // 错误：不能使 `foo` 在此处返回
        }
    }
    */

    /*
    但是如果 lambda 表达式传给的函数是内联的，该 return 也可以内联，所以它是允许的：
    fun foo() {
        inlineFunction {
            return // OK：该 lambda 表达式是内联的
        }
    }
    */

    //这种返回（位于 lambda 表达式中，但退出包含它的函数）称为非局部返回。 我们习惯了在
    //循环中用这种结构，其内联函数通常包含：

    fun hasZeros(ints: List<Int>): Boolean {
        ints.forEach {
            if (it == 0) return  true //从hasZeros 返回
        }
        return false
    }
}

inline fun <T> lock(lock: Lock, body: () -> T): T {

}


fun main(args: Array<String>) {
    val results = double(2)
    println(results)

    hello()

    foo(baz = 2)


    foo(1) {
        println("hello")
    }

    foo {
        print("welcome")
    }

    reformat("hello-reformat")

    //
    reformat("hello-2", upperCaseFirstLetter = false, wordSeparator = '_')

    //“可以通过使用星号操作符将可变数量参数（vararg） 以命名形式传入：”
    foo(strings = *arrayOf("a", "b", "c"))

    val list = asList(1, 2, 3)
    //当我们调用 vararg -函数时，我们可以一个接一个地传参，例如 asList(1, 2, 3) ，或者，
    //如果我们已经有一个数组并希望将其内容传给该函数，我们使用伸展（spread）操作符（在
    //数组前面加 * ）：
    val a = arrayOf(1, 2, 3)
    val list2 = asList(4, 5, *a, 6)

    // 用中缀表示法调用该函数
    val shlResult = 1 shl 2

    // 等同于这样
    1.shl(2)

    val items = listOf(1, 2, 3, 4, 5)
    val result = items.fold(0) {
        //如果一个lambda表达式有参数，签名是参数，后面跟 ->
        acc: Int, nextElement: Int ->

        println("acc = $acc, i = $nextElement")
        val result = acc + nextElement
        // lambda 表达式中的最后一个表达式是返回值：
        result
    }

    //lambda 表达式的参数类型是可选的，
    val joinedToString = items.fold("Elements: ") { acc, nextElement -> acc + " " + nextElement }

    ///函数应用也可以用高阶函数调用
    val product = items.fold(1, Int::times)

    println("joinedToString: $joinedToString")
    println("product: $product")

    testHighFunction()
    testHighFunction2()

    testLambdaFunction()
}
