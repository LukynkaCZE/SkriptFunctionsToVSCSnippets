fun convertFunctionToSnippets(function: SkriptFunction): String {
    return buildString {
        var functionName = function.name
        if(function.returnType != "") functionName += " @ ${function.returnType}"
        appendLine("    \"$functionName\":{")
        appendLine("        \"prefix\":[\"${function.body}\"],")
        appendLine("        \"body\":\"${getBodyWithInserts(function.body)}\"")
        appendLine("    },")
    }
}

fun getBodyWithInserts(body: String): String {

    val splitBracket = body.split("(")
    val splitCommas = splitBracket[1].split(", ")

    var splitCommasCount = 0
    splitCommas.forEach {
        if(it.contains("=")) {
            return@forEach
        } else {
            splitCommasCount++
        }
    }

    var out = "${splitBracket[0]}("
    splitCommas.forEachIndexed() {index, arg ->
        if(arg.contains("=")) return@forEachIndexed
        out += "\${${index+1}:${arg.split(": ")[1].replace(")", "")}}"
        if(splitCommasCount != (index + 1)) {
            out += ", "
        }
    }
    out += ")"
    return out
}