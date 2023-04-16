import java.io.File

fun main() {
    val file = File("E:/Coding/BlockWars/SkriptFunctionsParser/test.sk")
    val lines = mutableListOf<String>()
    file.useLines { fileLines -> fileLines.forEach { lines.add(it) }}

    val functions = getFunctions(lines)
    println("Found ${functions.count()} functions!")
    functions.forEach {
        println(convertFunctionToSnippets(it))
    }
}

fun getFunctions(lines: MutableList<String>): MutableList<SkriptFunction> {
    val out = mutableListOf<SkriptFunction>()

    var readingFunction = false
    var currentFunctionName = ""
    var currentFunctionBody = ""
    var currentFunctionReturnType = ""
    lines.forEachIndexed lineLoop@{index, line ->
        if(!readingFunction) {
            if(line.startsWith("function")) {
                readingFunction = true
                val split = line.split(" ")
                currentFunctionName = getFunctionName(split[1])
                currentFunctionBody = getFunctionBody(line).replace(") ", ")")
                currentFunctionReturnType = getFunctionReturnType(line)
            }
        } else {
            if(line.isBlank() || line.isEmpty() || line == "" || lines.count() == (index + 1) || line.replace(" ", "").startsWith("return")) {
                out.add(SkriptFunction(currentFunctionName, currentFunctionBody, currentFunctionReturnType))
                readingFunction = false
                return@lineLoop
            }
        }
    }
    return out
}

fun getFunctionReturnType(body: String): String {
    if (!body.contains("::")) return ""

    val split = body.split(" :: ")
    return split[1].replace(":", "").replace(") ", ")")
}

fun getFunctionBody(body: String): String {
    val out: String = if(body.contains("::")) {
        val split = body.split("::")
        split[0].replace("function ", "").replace(" :: ", "")
    } else {
        body.replace("function ", "").replace(") ", ")")
    }
    return out
}

fun getFunctionName(body: String): String {
    var out = ""
    val name = body.split("(")[0].replace("(", "").replace("function ", "").replace(")", "")

    name.forEachIndexed charLoop@{ index, loopChar ->
        var char = loopChar
        if(index == 0) {
            if(char.isLowerCase()) {
                char = char.uppercaseChar()
            }
        }
        if(char.isUpperCase()) {
            if(index == 0) {
                out += char
                return@charLoop
            }
            out += ' '
            out += char
        } else {
            out += char
        }
    }
    return out
}

data class SkriptFunction(
    var name: String,
    var body: String,
    var returnType: String
)