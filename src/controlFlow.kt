

fun main(args: Array<String>) {

    //控制流：if、when、for、while


    //If 表达式

    val a = 2
    val b = 3
    var max = a
    if (a < b) max = b

    //with else
    if (a > b) {
        max = a
    } else {
        max = b
    }

    //作为表达式
    max = if (a > b) a else b


    //if 的分支可以是代码块，最后的表达式作为该块的值：
    max = if (a > b) {
        a
    }
    else {
        b
    }


    //When 表达式
    when (a) {
        1 -> print(a)
        2 -> println(a)
        else -> {
            println(a)
        }
    }

    //如果很多分支需要用相同的方式处理，则可以把多个分支条件放在一起，用逗号分隔：

    when (b) {
        2, 3, 4 -> println(b)
        else -> println("another b: $b")
    }


    //我们可以用任意表达式（而不只是常量）作为分支条件

    fun isLargeThanZero(a: Int) = a

    when (a) {
        isLargeThanZero(a) -> println("a: $a")
        else -> {

        }
    }


    //我们也可以检测一个值在（ in ）或者不在（ !in ）一个区间或者集合中：
    when (a) {
        in 1..10 -> println("a is in the rage")
        !in 30..20 -> println("x is outside the range")
    }


    //另一种可能性是检测一个值是（ is ）或者不是（ !is ）一个特定类型的值。注意： 由于智能转换，你可以访问该类型的方法和属性而无需任何额外的检测。

    fun hasPrefix(x: Any) = when(x) {
        is String -> x.startsWith("prefix")
        else -> false
    }


    //when 也可以用来取代 if - else if 链。 如果不提供参数，所有的分支条件都是简单的布尔表达式，而当一个分支的条件为真时则执行该分支：
    /*
    when {
        x.isOdd() -> print("x is odd")
        x.isEven() -> print("x is even")
        else -> print("x is funny")
    }
    *
    */




    //For 循环

    //for 可以循环遍历任何提供了迭代器的对象。即：
    //有一个成员函数或者扩展函数 iterator() ，它的返回类型
    //有一个成员函数或者扩展函数 next() ，并且
    //有一个成员函数或者扩展函数 hasNext() 返回 Boolean 。


    for (i in 6 downTo 0 step 2) println("for i: $i")


    //对区间或者数组的 for 循环会被编译为并不创建迭代器的基于索引的循环。

    //如果你想要通过索引遍历一个数组或者一个 list，你可以这么做：
    val array = arrayOf("a", "b", "c")

    for (i in array.indices) {
        println(array[i])
    }


    //或者你可以用库函数 withIndex ：

    for ((index, value) in array.withIndex()) {
        println("the element at $index is $value")
    }



    //我们用一个匿名函数替代 lambda 表达式。 匿名函数内部的 return 语句将从该匿名
    //函数自身返回
    array.forEach {
        if (it == "a") return@forEach
    }

    array.forEach(fun (value: String) {
        if (value == "a") return // 局部返回到匿名函数的调用者，即 forEach 循环
    })

    run loop@ {
        array.forEach {
            if (it == "b") return@loop
        }
    }

    println("done with the nested loop")

}