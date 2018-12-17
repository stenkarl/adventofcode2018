package day16

import java.io.File

val registers = Registers()
val ops = listOf(addr(), addi(), mulr(), muli(), banr(), bani(), borr(), bori(),
                setr(), seti(), gtir(), gtri(), gtrr(), eqir(), eqri(), eqrr())

val opsMap = mutableMapOf<Op, MutableSet<Int>>()

val codeToOps = mutableMapOf<Int, Op>()

fun main(args:Array<String>) {
    initOpsMap()

    val list = File("/Users/sten/dev/adventofcode2018/src/day16/day16.txt").readLines()

    //println("ops size: ${ops.size}")
    val input = listOf(3, 2, 1, 1)
    val output = listOf(3, 2, 2, 1)

    //val cases = listOf(TestCase(input, output, 2, 1, 2))
    val cases = createCases(list)
    compare(cases)

    while (assignCodes()) {}
    populateCodeToOps()

    partTwo(File("/Users/sten/dev/adventofcode2018/src/day16/day16part2.txt").readLines())
}

fun populateCodeToOps() {
    ops.forEach {
        codeToOps.put(it.code, it)
    }
}

fun partTwo(commands:List<String>) {
    registers.set(0, 0, 0, 0)
    commands.forEach {
        val command = it.split(" ").map { c -> c.toInt()}
        val op = codeToOps[command[0]]
        val a = command[1]
        val b = command[2]
        val c = command[3]

        //println("$op $a $b $c")
        op!!.apply(a, b, c)
    }
    println(registers)
}

fun initOpsMap() {
    ops.forEach {
        opsMap.put(it, mutableSetOf())
    }
}

fun assignCodes():Boolean {
    val singleMap = opsMap.filter { (k, v) -> v.size == 1}
    if (singleMap.isEmpty()) return false
    singleMap.forEach { (k, v) ->
        val code = v.first()
        println("$k must be $code")
        k.code = code
        opsMap.remove(k)
        removeCode(code)
    }
    return true
}

fun removeCode(code:Int) {
    opsMap.forEach { (k, v) ->
        if (v.contains(code)) {
            v.remove(code)
        }
    }
}

fun createCases(list:List<String>):List<TestCase> {
    val cases = mutableListOf<TestCase>()

    for (i in 0 until list.size step 4) {
        val first = list[i]
        val input = listOf(first[9].toString().toInt(), first[12].toString().toInt(),
                            first[15].toString().toInt(), first[18].toString().toInt())

        val second = list[i + 1].split(" ")
        val opCode = second[0].toInt()
        val a = second[1].toInt()
        val b = second[2].toInt()
        val c = second[3].toInt()

        val third = list[i + 2]
        val output = listOf(third[9].toString().toInt(), third[12].toString().toInt(),
            third[15].toString().toInt(), third[18].toString().toInt())

        cases.add(TestCase(input, output, opCode, a, b, c))
    }

    return cases
}

fun compare(cases:List<TestCase>) {
    println("test cases size ${cases.size}")
    var count = 0
    //compare(cases[0])

    cases.forEach { case ->
        if (compare(case) >= 3) {
            count++
        }
    }

    println(count)
}

fun compare(c:TestCase):Int {
    var matching = 0
    ops.forEach { op ->
        registers.set(c.input)
        op.apply(c.a, c.b, c.c)
        if (registers.matches(c.output)) {
            //println("$op $registers matches $c")
            addToMap(op, c.op)
            matching++
        } else {
            //println("$op $registers does not match $c")
        }
    }
    return matching
}

fun addToMap(op:Op, code:Int) {
    val set = opsMap[op]
    if (!set!!.contains(code)) {
        set.add(code)
    }
}

data class TestCase(val input:List<Int>, val output:List<Int>, val op:Int, val a:Int, val b:Int, val c:Int)

class Registers {

    private var list = MutableList(4) {0}

    fun set(zero:Int, one:Int, two:Int, three:Int) {
        list[0] = zero
        list[1] = one
        list[2] = two
        list[3] = three
    }

    fun set(reg:List<Int>) {
        list[0] = reg[0]
        list[1] = reg[1]
        list[2] = reg[2]
        list[3] = reg[3]
    }

    fun set(which:Int, to:Int) {
        list[which] = to
    }

    fun get(index:Int):Int = list[index]

    fun get():List<Int> = list

    fun matches(other:List<Int>):Boolean {
        for (i in 0 until list.size) {
            if (list[i] != other[i]) return false
        }
        return true
    }

    override fun toString(): String = list.toString()
}

abstract class Op {

    var code:Int = -1

    abstract fun apply(a:Int, b:Int, c:Int)
}

class addr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) + registers.get(b)
        registers.set(c, result)
    }
}

class addi : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) + b
        registers.set(c, result)
    }
}

class mulr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) * registers.get(b)
        registers.set(c, result)
    }
}

class muli : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) * b
        registers.set(c, result)
    }
}

class banr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) and registers.get(b)
        registers.set(c, result)
    }
}

class bani : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) and b
        registers.set(c, result)
    }
}

class borr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) or registers.get(b)
        registers.set(c, result)
    }
}

class bori : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) or b
        registers.set(c, result)
    }
}

class setr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        registers.set(c, registers.get(a))
    }
}

class seti : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        registers.set(c, a)
    }
}

class gtir : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (a > registers.get(b)) 1 else 0
        registers.set(c, result)
    }
}

class gtri : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) > b) 1 else 0
        registers.set(c, result)
    }
}

class gtrr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) > registers.get(b)) 1 else 0
        registers.set(c, result)
    }
}

class eqir : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (a == registers.get(b)) 1 else 0
        registers.set(c, result)
    }
}

class eqri : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) == b) 1 else 0
        registers.set(c, result)
    }
}

class eqrr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) == registers.get(b)) 1 else 0
        registers.set(c, result)
    }
}