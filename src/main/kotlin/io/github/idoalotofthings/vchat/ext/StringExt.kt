package io.github.idoalotofthings.vchat.ext

fun String.digitCount(): Int {
    var count = 0
    for(element in this) {
        if(element.isDigit()) ++count
    }
    return count
}