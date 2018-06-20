import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class A {
//    var allByDefault: Int? //// 错误：需要显式初始化器，隐含默认 getter 和 setter
    var initialized = 1  // 类型 Int、默认 getter 和 setter

    //一个只读属性的语法和一个可变的属性的语法有两方面的不同：1、只读属性的用 val 开始 代替 var 2、只读属性不允许 setter

    //val simple: Int?  // 类型 Int、默认 getter、必须在构造函数中初始化
    val inferredType = 1


    //only get method
    val isEmpty: Boolean
        get() = false

    //setter
    var _string = ""
    var stringRepresentation: String
        get() = _string
        set(value) {
            _string = value // // 解析字符串并赋值给其他属性
        }

    //自 Kotlin 1.1 起，如果可以从 getter 推断出属性类型，则可以省略它：
    val isEmpty2 get() = _string.isEmpty() // 具有类型 Boolean

    //如果你需要改变一个访问器的可见性或者对其注解，但是不需要改变默认的实现， 你可以定义访问器而不定义其实现:
    var setterVisibility: String = "abc"
        private set //此 setter 是私有的并且有默认实现

    var setterWithAnnotation: Any? = null
//        @Inject set //用 Inject 注解此 setter


    //幕后字段
    //在 Kotlin 类中不能直接声明字段。然而，当一个属性需要一个幕后字段时，Kotlin 会自动提供。这个幕后字段可以使用 field 标识符在访问器中引用：
    var counter = 0
        set(value) {
            if (value >= 0) field = value
        }

    //field 标识符只能用在属性的访问器内。
    //如果属性至少一个访问器使用默认实现，或者自定义访问器通过 field 引用幕后字段，将会
    //为该属性生成一个幕后字段。
    //例如，下面的情况下， 就没有幕后字段：

    //val isEmpty: Boolean
        //get() = this.size == 0


    //幕后属性
    //如果你的需求不符合这套“隐式的幕后字段”方案，那么总可以使用 幕后属性（backing property）：

    private var _table: Map<String, Int>? = null
    public val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap() //类型参数已推断出
            }
            return  _table ?: throw AssertionError("Set to null by another thread")
        }

    //从各方面看，这正是与 Java 相同的方式。因为通过默认 getter 和 setter 访问私有属性会被优化，所以不会引入函数调用开销。



    //延迟初始化属性与变量

    lateinit var lateinitVar: String

    fun initializedLogic() {
        //要检测一个 lateinit var 是否已经初始化过，请在该属性的引用上使用 .isInitialized ：
        println("isInitialized before assignment: " + this::lateinitVar.isInitialized)
        lateinitVar = "value"
        println("isInitialized after assignment: " + this::lateinitVar.isInitialized)
    }
}

//编译期常量
//已知值的属性可以使用 const 修饰符标记为 编译期常量。 这些属性需要满足以下要求：
//* 位于顶层或者是 object 的一个成员
//* 用 String 或原生类型 值初始化
//* 没有自定义 getter
const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"
@Deprecated(SUBSYSTEM_DEPRECATED) fun foo() {}




//委托属性

class P {
    var p: String by PDelegate()

    //标准委托
    //延迟属性 Lazy
    //lazy() 是接受一个 lambda 并返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延
    //迟属性的委托： 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结
    //果， 后续调用 get() 只是返回记录的结果。
    val lazyValue: String by lazy {
        println("computed")
        "hello"
    }


    //可观察属性 Observable
    //Delegates.observable() 接受两个参数：初始值和修改时处理程序（handler）。 每当我们给
    //属性赋值时会调用该处理程序（在赋值后执行）。它有三个参数：被赋值的属性、旧值和新
    //值：

    var name: String by Delegates.observable("<no name>") { property, oldValue, newValue ->
        println("$oldValue -> $newValue")
    }

    //如果你想能够截获一个赋值并“否决”它，就使用 vetoable() 取代 observable() 。 在属性被赋新值生效之前会调用传递给 vetoable 的处理程序。
    //相当于加了一个filter

    var age: String by Delegates.vetoable("0") { property: KProperty<*>, oldValue: String, newValue: String ->
        println("$oldValue -> $newValue")
        return@vetoable newValue.toInt() > 10
    }

}

//语法是： val/var <属性名>: <类型> by <表达式> 。在 by 后面的表达式是该 委托， 因为属性
//对应的 get() （和 set() ）会被委托给它的 getValue() 和 setValue() 方法。 属性的委
//托不必实现任何的接口，但是需要提供一个 getValue() 函数（和 setValue() ——对于
//var 属性）。 例如:

class PDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef")
    }
}


//把属性储存在映射中
//一个常见的用例是在一个映射（map）里存储属性的值。 这经常出现在像解析 JSON 或者做
//其他“动态”事情的应用中。 在这种情况下，你可以使用映射实例自身作为委托来实现委托属
//性。
class User(val  map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

//如果把只读的 Map 换成 MutableMap 的话, 这也适用于 var 属性:
class MutableUser(val map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int by map
}


//局部委托属性（自 1.1 起）
//你可以将局部变量声明为委托属性。 例如，你可以使一个局部变量惰性初始化：
fun example(computeFoo: () -> A) {
    val memoizedA by lazy(computeFoo)

    val someCondition = false
    if (someCondition && memoizedA.isEmpty) {
        memoizedA.initializedLogic()
    }
}
//memoizedA 变量只会在第一次访问时计算。 如果 someCondition 失败，那么该变量根本不会计算。

fun main(args: Array<String>) {


    ////委托属性

    //当我们从委托到一个 Delegate 实例的 p 读取时，将调用 Delegate 中的 getValue() 函
    //数， 所以它第一个参数是读出 p 的对象、第二个参数保存了对 p 自身的描述 （例如你可
    //以取它的名字)。 例如:
    val e = P()
    println(e.p)
    e.p = "hello"

    println(e.lazyValue)
    println(e.lazyValue)
    e.name = "first"
    e.name = "second"

    println("e.age: ${e.age}")
    e.age = "8"
    println("e.age: ${e.age}")
    e.age = "20"
    println("e.age: ${e.age}")
    e.age = "30"
    println("e.age: ${e.age}")

    //把属性储存在映射中
    val user = User(mapOf(
            "name" to "Jone Doe",
            "age" to 25
    ))

    println(user.name) // Prints "John Doe"
    println(user.age) // Prints 25
}