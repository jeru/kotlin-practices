// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: J. Xorderable Array
// https://codeforces.com/problemset/problem/2045/J
//
// Too slow... C++ near-equivalent translation works...

import kotlin.math.max

fun sum2(n: Int): Long = n.toLong() * (n - 1).toLong() / 2

fun hasDupBit(a: MutableList<Int>, bit: Int): Boolean {
    if (a.size < 2) return false
    val n = a.size
    val v = (a[0] xor a[1]) and (a[n - 2] xor a[n - 1])
    return (v and (1 shl bit)) == 0
}

fun MutableList<Int>.splitByBit(bit: Int): Pair<MutableList<Int>, MutableList<Int>> {
    var i = 0
    while (i < this.size && (this[i] shr bit) == 0) ++i
    val r = this.subList(i, this.size)
    for ((j, v) in r.withIndex()) r[j] = v and (1 shl bit).inv()
    return Pair(this.subList(0, i), r)
}

fun countDiffPairs(ds: MutableList<Int>,
                   xps: MutableList<Pair<MutableList<Int>, MutableList<Int>>>,
                   bit: Int): Long {
    if (ds.size == 0 || bit < 0) {
        return xps.map{it.first.size.toLong() * it.second.size.toLong()}.sum()
    }
    if (xps.size == 0) return 0
    if (ds.any{(it shr bit) == 0}) {
        val dsNew = ds.filter{(it shr bit) == 0}.toMutableList()
        val xpsNew = ArrayList<Pair<MutableList<Int>, MutableList<Int>>>(xps.size)
        for (xp in xps) {
            val (xL0, xL1) = xp.first.splitByBit(bit)
            val (xR0, xR1) = xp.second.splitByBit(bit)
            if (xL0.size > 0 && xR0.size > 0) xpsNew.add(Pair(xL0, xR0))
            if (xL1.size > 0 && xR1.size > 0) xpsNew.add(Pair(xL1, xR1))
        }
        return countDiffPairs(dsNew, xpsNew, bit - 1)
    }

    for ((i, d) in ds.withIndex()) ds[i] = d and (1 shl bit).inv()
    var ans = 0L
    val xpsNew = ArrayList<Pair<MutableList<Int>, MutableList<Int>>>(xps.size)
    for (xp in xps) {
        val (xL0, xL1) = xp.first.splitByBit(bit)
        val (xR0, xR1) = xp.second.splitByBit(bit)
        ans += xL0.size.toLong() * xR0.size.toLong()
        ans += xL1.size.toLong() * xR1.size.toLong()
        if (xL0.size > 0 && xR1.size > 0) xpsNew.add(Pair(xL0, xR1))
        if (xL1.size > 0 && xR0.size > 0) xpsNew.add(Pair(xL1, xR0))
    }
    return ans + countDiffPairs(ds, xpsNew, bit - 1)
}

fun countPairs(as1: MutableList<MutableList<Int>>,
               xs: MutableList<MutableList<Int>>,
               bit: Int): Long {
    if (as1.size == 0 || bit < 0) return xs.map{sum2(it.size)}.sum()
    if (as1.any{hasDupBit(it, bit)}) {
        val asNew = ArrayList<MutableList<Int>>(as1.size)
        val xsNew = ArrayList<MutableList<Int>>(xs.size)
        for (a in as1) {
            val (a0, a1) = a.splitByBit(bit)
            if (a0.size >= 2) asNew.add(a0)
            if (a1.size >= 2) asNew.add(a1)
        }
        for (x in xs) {
            val (x0, x1) = x.splitByBit(bit)
            if (x0.size >= 2) xsNew.add(x0)
            if (x1.size >= 2) xsNew.add(x1)
        }
        return countPairs(asNew, xsNew, bit - 1)
    }

    var ans = 0L
    val xps = ArrayList<Pair<MutableList<Int>, MutableList<Int>>>(xs.size)
    for (x in xs) {
        val (x0, x1) = x.splitByBit(bit)
        ans += sum2(x0.size) + sum2(x1.size)
        if (x0.size > 0 && x1.size > 0) xps.add(Pair(x0, x1))
    }
    val ds = as1.filter{it.size == 2}.map{(it[0] xor it[1]) and (1 shl bit).inv()}.toMutableList()
    return ans + countDiffPairs(ds, xps, bit - 1)
}

fun main() {
    readln()
    val a = readln().split(" ").map{it.toInt()}.sorted().toMutableList()
    val x = readln().split(" ").map{it.toInt()}.sorted().toMutableList()

    val maxVal = max(a.last(), x.last())
    var bit = 0
    while ((maxVal shr bit) != 0) ++bit

    val ans = countPairs(mutableListOf(a), mutableListOf(x), bit)
    println(ans)
}
