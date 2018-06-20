import org.w3c.dom.Node

fun main(args: Array<String>) {

    var a: String = "abc"
    //a = null // 编译错误

    var b: String? = "abc"
    b = null // ok

    //在条件中检查 null


    //首先，你可以显式检查 b 是否为 null ，并分别处理两种可能：
    var l = if (b != null) b.length else -1


    //更负责的情况
    if (b != null && b.length > 0) {
        print("String of length ${b.length}")
    } else {
        print("Empty string")
    }


    //安全的调用
    //你的第二个选择是安全调用操作符，写作 ?. ：
    b?.length

    //如果要只对非空值执行某个操作，安全调用操作符可以与 let 一起使用：

    val listWithNulls: List<String?> = listOf("A", null)

    for (item in listWithNulls) {
        item?.let {
            println("$it")
        }
    }


    //Elvis 操作符

    //当我们有一个可空的引用 r 时，我们可以说“如果 r 非空，我使用它；否则使用某个非空
    //的值 x ”：

    //val l: Int = if (b != null) b.length else -1

    //除了完整的 if -表达式，这还可以通过 Elvis 操作符表达，写作 ?: ：
    l = b?.length ?: -1
    //如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。 请注意，当且仅当左侧为空时，才会对右侧表达式求值。

    //请注意，因为 throw 和 return 在 Kotlin 中都是表达式，所以它们也可以用在 elvis 操作符右侧。这可能会非常方便，例如，检查函数参数：
    fun foo(node: Node): String? {
        val parent = node.parentNode ?: return null
        val name = node.nodeName ?: throw IllegalArgumentException("name")
        return ""
    }


    //!! 操作符
    //非空断言运算符（ !! ）将任何值转换为非空类型，若
    //该值为空则抛出异常。我们可以写 b!! ，这会返回一个非空的 b 值 （例如：在我们例子
    //中的 String ）或者如果 b 为空，就会抛出一个 NPE 异常：
    l = b!!.length


    //安全的类型转换
    //如果对象不是目标类型，那么常规类型转换可能会导致 ClassCastException 。 另一个选择是
    //使用安全的类型转换，如果尝试转换不成功则返回 null ：

    val aInt: Int? = l as? Int

    //可空类型的集合

    //如果你有一个可空类型元素的集合，并且想要过滤非空元素，你可以使用 filterNotNull 来实现：
    val nullableList: List<Int?> = listOf(1, 2, null, 3)
    val intList = nullableList.filterNotNull()
}