

fun main(args: Array<String>) {

    // _ 使常量更易读
    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val socialSecurityNumber = 999_99_9999L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010


    //注意数字装箱不必保留同一性:
    val a: Int = 10000
    println(a === a) //true
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA === anotherBoxedA) // false

    //On the other hand, it preserves equality:
    println(boxedA == anotherBoxedA) //true
    println(boxedA == a)

    //显式转换
    val doubleA = a.toDouble()

    //我们可以显式把字符转换为 Int 数字：
    fun  decimalDigitValue(c: Char): Int {
        if (c !in  '0'..'9') {
            throw IllegalArgumentException("Out of range")
        }
        return c.toInt() - '0'.toInt()
    }


    //Array
    val asc = Array(5, { i -> (i * i).toString() })


    //Kotlin 也有无装箱开销的专门的类来表示原生类型数组: ByteArray 、ShortArray 、 IntArray 等等。
    val x: IntArray = intArrayOf(1, 2, 3)
    x[0] = x[1] * x[2]
    println("x[0]: ${x[0]}")


    //字符串
    //字符串用 String 类型表示。字符串是不可变的。 字符串的元素——字符可以使用索引运算符访问: s[i] 。 可以用 for 循环迭代字符串:
    val str = "abc"
    for (c in str) {
        println("c: $c")
    }
    println("c[1]: ${str[1]}")

    //可以用 + 操作符连接字符串。这也适用于连接字符串与其他类型的值， 只要表达式中的第一个元素是字符串：
    val s = "abc" + 2 + 2.9 + 'c'
    println("s: $s")



    //字符串字面值
    //Kotlin 有两种类型的字符串字面值: 转义字符串可以有转义字符，以及原始字符串可以包含换行和任意文本。转义字符串很像 Java 字符串:
    val ss = "hello world \n"
    //转义采用传统的反斜杠方式。


    //原始字符串 使用三个引号（ """ ）分界符括起来，内部没有转义并且可以包含换行和任何其他字符:
    val text = """
        for (c in "foo")
            print(c)
    """.trimIndent()

    val text2 = """
        |Tell me and I forget.
        |Teach me and I remember.
        |Involve me and I learn.
        |(Benjamin Franklin)
    """.trimMargin()

    println("text: $text \ntext2: $text2")



}