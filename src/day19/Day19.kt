package day19

import day16.Registers
import java.io.File

val registers = Registers(6)
val ops = listOf(
    addr(), addi(), mulr(), muli(), banr(), bani(), borr(), bori(),
    setr(), seti(), gtir(), gtri(), gtrr(), eqir(), eqri(), eqrr()
)


fun main(args:Array<String>) {

    val list = File("/Users/sten/dev/adventofcode2018/src/day19/day19.txt").readLines()

    val instRegister = list[0][4].toString().toInt()
    println("Instruction Register: $instRegister")

    val instructions = list.subList(1, list.size)
    var ip = 0
    registers.set(0, 1)
    println(registers)

    //while (ip < instructions.size) {
    for (i in 0..50) {
        dump(ip)
        execute(instructions[ip])
        ip = registers.get(instRegister)
        ip++
        registers.set(instRegister, ip)
    }
    println(registers)
}

fun execute(line:String) {
    val split = line.split(" ")
    val op = findOp(split[0])
    val arg0 = split[1].toInt()
    val arg1 = split[2].toInt()
    val arg2 = split[3].toInt()

    op.apply(arg0, arg1, arg2)
    dump(op, arg0, arg1, arg2)
}

fun findOp(name:String):Op {
    val op = ops.find { it.toString() == name}
    if (op != null) {
        return op
    }
    println("<< Error missing Op $name >>")
    return ops[0]
}

fun dump(ip:Int) {
    print("ip=$ip $registers ")
}

fun dump(op:Op, arg0:Int, arg1:Int, arg2:Int) {
    println("$op $arg0 $arg1 $arg2 $registers")
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

    override fun toString(): String = "addr"
}

class addi : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) + b
        registers.set(c, result)
    }

    override fun toString(): String = "addi"
}

class mulr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) * registers.get(b)
        registers.set(c, result)
    }

    override fun toString(): String = "mulr"
}

class muli : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) * b
        registers.set(c, result)
    }

    override fun toString(): String = "muli"
}

class banr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) and registers.get(b)
        registers.set(c, result)
    }

    override fun toString(): String = "banr"
}

class bani : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) and b
        registers.set(c, result)
    }

    override fun toString(): String = "bani"
}

class borr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) or registers.get(b)
        registers.set(c, result)
    }

    override fun toString(): String = "borr"
}

class bori : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = registers.get(a) or b
        registers.set(c, result)
    }

    override fun toString(): String = "bori"
}

class setr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        registers.set(c, registers.get(a))
    }

    override fun toString(): String = "setr"
}

class seti : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        registers.set(c, a)
    }

    override fun toString(): String = "seti"
}

class gtir : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (a > registers.get(b)) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "gtir"
}

class gtri : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) > b) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "gtri"
}

class gtrr : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) > registers.get(b)) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "gtrr"
}

class eqir : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (a == registers.get(b)) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "eqir"
}

class eqri : Op() {

    override fun apply(a:Int, b:Int, c:Int) {
        val result = if (registers.get(a) == b) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "eqri"
}

class eqrr : Op() {

    override fun apply(a: Int, b: Int, c: Int) {
        val result = if (registers.get(a) == registers.get(b)) 1 else 0
        registers.set(c, result)
    }

    override fun toString(): String = "eqrr"
}