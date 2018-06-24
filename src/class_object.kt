import java.text.FieldPosition

//class

class Invoice {

}


//如果一个类没有类体，可以省略花括号。

class Empty


//构造函数
//在 Kotlin 中的一个类可以有一个主构造函数和一个或多个次构造函数。主构造函数是类头的
//一部分：它跟在类名（和可选的类型参数）后。

class Person constructor(firstName: String) {

}

//如果主构造函数没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字。
class Person1(firstName: String) {

}

//初始化的代码可以放到以 init 关键字作为前缀的初始化
//块（initializer blocks）中。

//在实例初始化期间，初始化块按照它们出现在类体中的顺序执行，与属性初始化器交织在一起：
class InitOrderDemo(name: String) {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)
    init {
        println("Second initializer block that prints ${name.length}")
    }
}


//请注意，主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用：

class Customer(name: String) {
    val customerKey = name.toUpperCase()
}


//事实上，声明属性以及从主构造函数初始化属性，Kotlin 有简洁的语法：
class Person2(val name: String, val lastName: String, val age: Int) {

}

//如果构造函数有注解或可见性修饰符，这个 constructor 关键字是必需的，并且这些修饰符在它前面：

class Customer2 public constructor(name: String) {

}


//次构造函数

//类也可以声明前缀有 constructor 的次构造函数：

class Person3 {
    constructor(name: String) {

    }
}


//如果类有一个主构造函数，每个次构造函数需要委托给主构造函数， 可以直接委托或者通过
//别的次构造函数间接委托。委托到同一个类的另一个构造函数用 this 关键字即可：
class Person4(val name: String) {
    constructor(name: String, parent: Person): this(name) {
//        parent.
    }
}


//请注意，初始化块中的代码实际上会成为主构造函数的一部分。委托给主构造函数会作为次
//构造函数的第一条语句，因此所有初始化块中的代码都会在次构造函数体之前执行。即使该
//类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块：

class Constructors {
    init {
        println("Init block")
    }
    constructor(i: Int) {
        println("Constructor")
    }
}

//默认值
class Customer3(val customerName: String = "") {

}



//继承

//在 Kotlin 中所有类都有一个共同的超类 Any ，这对于没有超类型声明的类是默认超类：

class Example2 // 从 Any 隐式继承

//注意： Any 并不是 java.lang.Object ；尤其是，它除了
//equals() 、 hashCode() 和 toString() 外没有任何成员。

//要声明一个显式的超类型，我们把类型放到类头的冒号之后：

open class Base(p: Int) {
    open val value: Int = p
    open val sp: Int = p
    //如果函数没有标注open 如 Base.nv() ，则子类中不允许定义相同签名的函数， 不论加不加 override。在一个
    //final 类中（没有用 open 标注的类），开放成员是禁止的。
    open fun v() {}
    fun nv() {}
}

class Deried(p: Int): Base(p) {

}
//类上的 open 标注与 Java 中 final 相反，它允许其他类从这个类继承。默认情况下，
//在 Kotlin 中所有的类都是 final，


//如果派生类有一个主构造函数，其基类型可以（并且必须） 用基类的主构造函数参数就地初
//始化。
//如果类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，或委
//托给另一个构造函数做到这一点。 注意，在这种情况下，不同的次构造函数可以调用基类型
//的不同的构造函数：

class Deapper: Base {
    //
    override val value: Int
        get() = super.value



    //你也可以用一个 var 属性覆盖一个 val 属性，但反之则不行。这是允许的，因为一个
    //val 属性本质上声明了一个 getter 方法，而将其覆盖为 var 只是在子类中额外声明一个
    //setter 方法。
    override var sp: Int
        get() = super.sp
        set(value) {
            println(value)
        }

    constructor(p: Int): super(p) {
        println("p: $p")
    }

    //如果你想禁止再
    //次覆盖，使用 final 关键字：
    final override fun v() {
        super.v()
    }

}


//派生类初始化顺序

//在构造派生类的新实例的过程中，第一步完成其基类的初始化（在之前只有对基类构造函数
//参数的求值），因此发生在派生类的初始化逻辑运行之前。

open class Base1(val name: String) {
    init { println("Initializing Base") }
    open val size: Int =
            name.length.also { println("Initializing size in Base: $it") }
}
class Derived(
        name: String,
        val lastName: String) : Base1(name.capitalize().also { println("Argument for Base: $it") }) {
    init {
        println("Initializing Derived")
    }

    override val size: Int =
            (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}


//调用超类实现
//派生类中的代码可以使用 super 关键字调用其超类的函数与属性访问器的实现：

open class Foo {
    open fun f() { println("Foo.f()") }
    open val x: Int get() = 1
}
class Bar : Foo() {
    override fun f() {
        super.f()
        println("Bar.f()")
    }
    override val x: Int get() = super.x + 1

    //在一个内部类中访问外部类的超类，可以通过由外部类名限定的 super 关键字来实
    //现： super@Outer ：
    inner class Baz {
        fun g() {
            super@Bar.f() // 调用 Foo 实现的 f()
            println(super@Bar.x) // 使用 Foo 实现的 x 的 getter
        }
    }
}

//覆盖规则
//在 Kotlin 中，实现继承由下述规则规定：如果一个类从它的直接超类继承相同成员的多个实
//现， 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）。 为了表示采用
//从哪个超类型继承的实现，我们使用由尖括号中超类型名限定的 super ，如 super<Base> ：

open class AB {
    open fun f() { print("A") }
    fun a() { print("a") }
}
interface B {
    fun f() { print("B") } // 接口成员默认就是“open”的
    fun b() { print("b") }
}
class C() : AB(), B {
    // 编译器要求覆盖 f()：
    override fun f() {
        super<AB>.f() // 调用 A.f()
        super<B>.f() // 调用 B.f()
    }

}




//抽象类
//类和其中的某些成员可以声明为 abstract 。 抽象成员在本类中可以不用实现。 需要注意的
//是，我们并不需要用 open 标注一个抽象类或者函数——因为这不言而喻。
//我们可以用一个抽象成员覆盖一个非抽象的开放成员

open class BaseAbstract {

    //伴生对象, 声明类方法
    companion object {

    }
    open fun f() {}
}

abstract class Derived2: BaseAbstract() {
    override abstract fun f()
}


//接口

//既包含抽象方法的声明，也包含实现。与抽象类不同的是，接
//口无法保存状态。它可以有属性但必须声明为抽象或提供访问器实现。

interface MyInterface {
    fun bar()
    fun foo() {
        //可选的方法体， 可添加默认实现

        println(prop)
    }


    //接口中的属性
    //你可以在接口中定义属性。在接口中声明的属性要么是抽象的，要么提供访问器的实现。在
    //接口中声明的属性不能有幕后字段（backing field），因此接口中声明的访问器不能引用它们。

    val prop: Int // 抽象的
    val propertyWithImplementation: String
        get() = "foo"
}


//一个类或者对象可以实现一个或多个接口。

class Child: MyInterface {
    override val prop: Int = 29

    override fun bar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


//接口继承
//一个接口可以从其他接口派生，从而既提供基类型成员的实现也声明新的函数与属性。很自
//然地，实现这样接口的类只需定义所缺少的实现：

interface Named {
    val name: String
}

interface Person5: Named {
    val firstName: String
    val lastName: String

    override val name: String
        get() = "$firstName $lastName"
}

data class Employee(
        //// 不必实现“name”
        override val firstName: String,
        override val lastName: String,
        val position: Int
): Person5


//解决覆盖冲突

interface A2 {
    fun foo() { print("A") }
    fun bar()
}
interface B1 {
    fun foo() { print("B") }
    fun bar() { print("bar") }
}
class C1 : A2 {
    override fun bar() { print("bar") }
}
class D : A2, B1 {
    override fun foo() {
        super<A2>.foo()
        super<B1>.foo()
    }
    override fun bar() {
        super<B1>.bar()
    }
}

//上例中，接口 A 和 B 都定义了方法 foo() 和 bar()。 两者都实现了 foo(), 但是只有 B 实现了
//bar() (bar() 在 A 中没有标记为抽象， 因为没有方法体时默认为抽象）。因为 C 是一个实现了
//A 的具体类，所以必须要重写 bar() 并实现这个抽象方法。
//然而，如果我们从 A 和 B 派生 D，我们需要实现我们从多个接口继承的所有方法，并指明 D
//应该如何实现它们。这一规则既适用于继承单个实现（bar()）的方法也适用于继承多个实现
//（foo()）的方法。


//扩展函数

//声明一个扩展函数，我们需要用一个 接收者类型 也就是被扩展的类型来作为他的前缀。 下面
//代码为 MutableList<Int> 添加一个 swap 函数：
/*
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}
*/

//这个函数对任何 MutableList<T> 起作用，我们可以泛化它：
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}


//扩展是静态解析的

//扩展不能真正的修改他们所扩展的类。通过定义一个扩展，你并没有在一个类中插入新成
//员， 仅仅是可以通过该类型的变量用点表达式去调用这个新函数。

//我们想强调的是扩展函数是静态分发的，即他们不是根据接收者类型的虚方法。 这意味着调
//用的扩展函数是由函数调用所在的表达式的类型来决定的， 而不是由表达式运行时求值结果
//决定的。

open class C2
class D2: C2()

fun C2.foo() = "c"
fun D2.foo() = "d"
fun printFoo(c: C2) {
    println(c.foo())
}

//如果一个类定义有一个成员函数和一个扩展函数，而这两个函数又有相同的接收者类型、相
//同的名字并且都适用给定的参数，这种情况总是取成员函数。

fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}

//扩展属性
//和函数类似，Kotlin 支持扩展属性：

val <T> List<T>.lastIndex: Int
    get() = size - 1


//伴生对象的扩展

class MyClass {
    companion object {

    }
}

fun MyClass.Companion.foo() {

}


//扩展声明为成员
//在一个类内部你可以为另一个类声明扩展。在这样的扩展内部，有多个 隐式接收者 —— 其中
//的对象成员可以无需通过限定符访问。扩展声明所在的类的实例称为 分发接收者，扩展方法
//调用所在的接收者类型的实例称为 扩展接收者

class E {
    fun bar() { }
}
class F {
    fun baz() { }
    fun E.foo() {
        bar() // 调用 E.bar
        baz() // 调用 F.baz

        toString()

        //对于分发接收者和扩展接收者的成员名字冲突的情况，扩展接收者优先。要引用分发接收者
        //的成员你可以使用 限定的 this 语法。
        this@F.toString()
    }
    fun caller(d: E) {
        d.foo() // 调用扩展函数
    }
}


//声明为成员的扩展可以声明为 open 并在子类中覆盖。这意味着这些函数的分发对于分发接
//收者类型是虚拟的，但对于扩展接收者类型是静态的。

open class G {

}

class G1: G() {

}

open class H {
    open fun G.foo() {
        println("G.fun in H")
    }

    open fun G1.foo() {
        println("G1.fun in H")
    }

    fun caller(g: G) {
        g.foo() //
    }
}

class H1: H() {
    override fun G.foo() {
        println("G.foo in H1")
    }

    override fun G1.foo() {
        println("G1.foo in H1")
    }
}



//数据类
//我们经常创建一些只保存数据的类。 在这些类中，一些标准函数往往是从数据机械推导而来
//的。在 Kotlin 中，这叫做 数据类 并标记为 data ：

data class User1(val name: String, val age: Int)



//密封类
//密封类用来表示受限的类继承结构：当一个值为有限集中的类型、而不能有任何其他类型
//时。在某种意义上，他们是枚举类的扩展：枚举类型的值集合也是受限的，但每个枚举常量
//只存在一个实例，而密封类的一个子类可以有可包含状态的多个实例。

//要声明一个密封类，需要在类名前面添加 sealed 修饰符。虽然密封类也可以有子类，但是
//所有子类都必须在与密封类自身相同的文件中声明。（在 Kotlin 1.1 之前， 该规则更加严
//格：子类必须嵌套在密封类声明的内部）。
sealed class Expr

data class Const(val number: Double): Expr()
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()


//泛型约束

//冒号之后指定的类型是上界：只有 Comparable<T> 的子类型可以替代 T 。 例如：
fun <T: Comparable<T>> sort(list: List<T>) {

}

//默认的上界（如果没有声明）是 Any? 。在尖括号中只能指定一个上界。 如果同一类型参数
//需要多个上界，我们需要一个单独的 where-子句：

fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
    where T: CharSequence,
          T: Comparable<T> {
    return list.filter { it > threshold }.map { it.toString() }
}


//嵌套类与内部类

class Outer {
    private val bar: Int = 1
    class Nested {
        fun foo() = 2
    }
}
val demo = Outer.Nested().foo() // == 2


//内部类
//类可以标记为 inner 以便能够访问外部类的成员。内部类会带有一个对外部类的对象的引
//用：

class Outer1 {
    private val bar: Int = 1
    inner class Inner {
        fun foo() = bar
    }
}
val demo1 = Outer1().Inner().foo() // == 1


//对象表达式与对象声明

//要创建一个继承自某个（或某些）类型的匿名类的对象，我们会这么写：

/*
window.addMouseListener(object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
// ……
    }
    override fun mouseEntered(e: MouseEvent) {
// ……
    }
})
*/

//如果超类型有一个构造函数，则必须传递适当的构造函数参数给它。 多个超类型可以由跟在
//冒号后面的逗号分隔的列表指定：

open class I(x: Int) {
    public open val y: Int = x
}

interface M {

}

val ab: I = object : I(2), M {
    override val y: Int
        get() = 15
}


//任何时候，如果我们只需要“一个对象而已”，并不需要特殊超类型，那么我们可以简单地写：

fun foo2() {
    val adHoc = object {
        var x: Int = 0
        var y: Int = 0
    }
    println(adHoc.x + adHoc.y)
}

//匿名对象可以用作只在本地和私有作用域中声明的类型。如果你使用匿名对象作为
//公有函数的返回类型或者用作公有属性的类型，那么该函数或属性的实际类型会是匿名对象
//声明的超类型，如果你没有声明任何超类型，就会是 Any 。在匿名对象中添加的成员将无法
//访问。

class N {
    //私有方法，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    //公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x
        //val x2 = publicFoo().x //error
    }
}

object DataProviderManager {
    fun registerDataProvider(provider: N) {

    }

//    val allDataProvider: Collection<N>
//        get() =
}

//伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象
//的实例成员，而且，例如还可以实现接口：

fun main(args: Array<String>) {
    InitOrderDemo("hello")
    Constructors(2)
    Customer3()

    println("Constructing Derived(\"hello\", \"world\")")
    val d = Derived("hello", "world")

    //
    val intL = mutableListOf(1, 2, 3)
    intL.swap(2, 1)
    println(intL)

    //
    printFoo(D2()) // c
    //这个例子会输出 "c"，因为调用的扩展函数只取决于参数 c 的声明类型，该类型是 C 类。

    MyClass.foo()

    H().caller(G())
    H1().caller(G())
    H().caller(G1())

    val jack = User1("jack", age = 2)
    val olderJack = jack.copy(age = 28)
    println("jack.equals(olderJack): ${jack.equals(olderJack)}")

    //数据类和解构声明
    val (name, age) = jack
    println("$name age: $age") //jack age: 2


    println("伴生对象： ${ab.y}")

    DataProviderManager.registerDataProvider(N())

    //类型投影
    //使用处型变：类型投影

}