package com.example.model

object KeyboardLayouts {

    // Number Row (0-9) that sits above the QWERTY row if enabled
    val numberRow = listOf(
        KeyModel("1"), KeyModel("2"), KeyModel("3"), KeyModel("4"), KeyModel("5"),
        KeyModel("6"), KeyModel("7"), KeyModel("8"), KeyModel("9"), KeyModel("0")
    )

    // QWERTY Layout Rows
    val qwertyRow1 = listOf(
        KeyModel("Q", secondaryText = "1"),
        KeyModel("W", secondaryText = "2"),
        KeyModel("E", secondaryText = "3"),
        KeyModel("R", secondaryText = "4"),
        KeyModel("T", secondaryText = "5"),
        KeyModel("Y", secondaryText = "6"),
        KeyModel("U", secondaryText = "7"),
        KeyModel("I", secondaryText = "8"),
        KeyModel("O", secondaryText = "9"),
        KeyModel("P", secondaryText = "0")
    )

    val qwertyRow2 = listOf(
        KeyModel("A", secondaryText = "@"),
        KeyModel("S", secondaryText = "#"),
        KeyModel("D", secondaryText = "₹"),
        KeyModel("F", secondaryText = "%"),
        KeyModel("G", secondaryText = "&"),
        KeyModel("H", secondaryText = "-"),
        KeyModel("J", secondaryText = "+"),
        KeyModel("K", secondaryText = "("),
        KeyModel("L", secondaryText = ")")
    )

    val qwertyRow3 = listOf(
        KeyModel(mainText = "Shift", type = KeyType.SHIFT, weight = 1.3f),
        KeyModel("Z", secondaryText = "*"),
        KeyModel("X", secondaryText = "\""),
        KeyModel("C", secondaryText = "'"),
        KeyModel("V", secondaryText = ":"),
        KeyModel("B", secondaryText = ";"),
        KeyModel("N", secondaryText = "!"),
        KeyModel("M", secondaryText = "?"),
        KeyModel(mainText = "Delete", type = KeyType.BACKSPACE, weight = 1.3f)
    )

    // Hindi Layout Rows (Inscript / Varnamala)
    val hindiRow1 = listOf(
        KeyModel("ौ", secondaryText = "१"),
        KeyModel("ै", secondaryText = "२"),
        KeyModel("ा", secondaryText = "३"),
        KeyModel("ी", secondaryText = "४"),
        KeyModel("ू", secondaryText = "५"),
        KeyModel("ब", secondaryText = "६"),
        KeyModel("ह", secondaryText = "७"),
        KeyModel("ग", secondaryText = "८"),
        KeyModel("द", secondaryText = "९"),
        KeyModel("ज", secondaryText = "०")
    )

    val hindiRow2 = listOf(
        KeyModel("ो"), KeyModel("े"), KeyModel("्"), KeyModel("ि"), KeyModel("ु"),
        KeyModel("प"), KeyModel("र"), KeyModel("क"), KeyModel("त"), KeyModel("च"), KeyModel("ट")
    )

    val hindiRow3 = listOf(
        KeyModel(mainText = "Shift", type = KeyType.SHIFT, weight = 1.2f),
        KeyModel("ं"), KeyModel("म"), KeyModel("न"), KeyModel("व"), KeyModel("ल"),
        KeyModel("स"), KeyModel("य"),
        KeyModel(mainText = "Delete", type = KeyType.BACKSPACE, weight = 1.2f)
    )

    // Numbers Layout Rows (with Indian Rupee ₹ from Image 2)
    val numbersRow1 = listOf(
        KeyModel("1"), KeyModel("2"), KeyModel("3"), KeyModel("4"), KeyModel("5"),
        KeyModel("6"), KeyModel("7"), KeyModel("8"), KeyModel("9"), KeyModel("0")
    )

    val numbersRow2 = listOf(
        KeyModel("@"), KeyModel("#"), KeyModel("₹"), KeyModel("_"), KeyModel("&"),
        KeyModel("-"), KeyModel("+"), KeyModel("("), KeyModel(")"), KeyModel("/")
    )

    val numbersRow3 = listOf(
        KeyModel(mainText = "1/2", type = KeyType.SWITCH_LAYOUT, weight = 1.3f),
        KeyModel("*"), KeyModel("\""), KeyModel("'"), KeyModel(":"), KeyModel(";"),
        KeyModel("!"), KeyModel("?"),
        KeyModel(mainText = "Delete", type = KeyType.BACKSPACE, weight = 1.3f)
    )

    // Symbols Layout Rows
    val symbolsRow1 = listOf(
        KeyModel("~"), KeyModel("`"), KeyModel("|"), KeyModel("•"), KeyModel("√"),
        KeyModel("π"), KeyModel("÷"), KeyModel("×"), KeyModel("¶"), KeyModel("∆")
    )

    val symbolsRow2 = listOf(
        KeyModel("₹"), KeyModel("$"), KeyModel("€"), KeyModel("£"), KeyModel("¥"),
        KeyModel("¢"), KeyModel("^"), KeyModel("°"), KeyModel("="), KeyModel("\\")
    )

    val symbolsRow3 = listOf(
        KeyModel(mainText = "?123", type = KeyType.SWITCH_LAYOUT, weight = 1.3f),
        KeyModel("%"), KeyModel("©"), KeyModel("®"), KeyModel("™"), KeyModel("✓"),
        KeyModel("{"), KeyModel("}"),
        KeyModel(mainText = "Delete", type = KeyType.BACKSPACE, weight = 1.3f)
    )

    // Character Accent Variations for Long-Press (Image 2: Long Press Actions)
    val accentsMap: Map<String, List<String>> = mapOf(
        "a" to listOf("a", "á", "à", "â", "ä", "ã", "å"),
        "A" to listOf("A", "Á", "À", "Â", "Ä", "Ã", "Å"),
        "e" to listOf("e", "é", "è", "ê", "ë", "ē"),
        "E" to listOf("E", "É", "È", "Ê", "Ë", "Ē"),
        "i" to listOf("i", "í", "ì", "î", "ï", "ī"),
        "I" to listOf("I", "Í", "Ì", "Î", "Ï", "Ī"),
        "o" to listOf("o", "ó", "ò", "ô", "ö", "õ"),
        "O" to listOf("O", "Ó", "Ò", "Ô", "Ö", "Õ"),
        "u" to listOf("u", "ú", "ù", "û", "ü", "ū"),
        "U" to listOf("U", "Ú", "Ù", "Û", "Ü", "Ū"),
        "c" to listOf("c", "ç", "ć", "č"),
        "C" to listOf("C", "Ç", "Ć", "Č"),
        "n" to listOf("n", "ñ", "ń"),
        "N" to listOf("N", "Ñ", "Ń"),
        "s" to listOf("s", "ß", "ś", "š", "$"),
        "S" to listOf("S", "ẞ", "Ś", "Š", "$"),
        "r" to listOf("r", "₹", "®"),
        "R" to listOf("R", "₹", "®"),
        "d" to listOf("d", "₹", "ð"),
        "D" to listOf("D", "₹", "Ð"),
        "$" to listOf("$", "₹", "€", "£", "¥", "¢")
    )

    // Emoji Collections
    val emojiSmileys = listOf(
        "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇",
        "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😋", "😛", "😜",
        "🤪", "🤓", "😎", "🥳", "🤩", "🤗", "🤔", "🤫", "🤭", "🤐",
        "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "😮‍💨", "🤥", "😌"
    )

    val emojiGestures = listOf(
        "👍", "👎", "👏", "🙌", "🤝", "✌️", "🤞", "🤟", "🤘", "🤙",
        "👈", "👉", "👆", "👇", "☝️", "✋", "🤚", "🖐️", "🖖", "👋",
        "✍️", "🙏", "💪", "🦾", "🤳", "💅", "👊", "✊", "🤛", "🤜"
    )

    val emojiHearts = listOf(
        "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔",
        "❣️", "💕", "💞", "💓", "💗", "💖", "💘", "💝", "💟", "🔥",
        "✨", "🌟", "⭐", "💫", "💥", "💯", "🎉", "🎊", "🎁", "🎈"
    )

    val emojiTech = listOf(
        "📱", "💻", "⌨️", "🖥️", "🔒", "🛡️", "⚡", "💡", "🚀", "🎯",
        "🏆", "🎨", "🎙️", "📋", "💬", "🌐", "🔑", "⚙️", "🔋", "📶",
        "🤖", "🎮", "📸", "🎧", "🕹️", "💾", "💿", "📡", "🛰️", "💎"
    )

    // Reaction Stickers & Kaomoji for GIF toolbar button
    val reactionStickers = listOf(
        "(⁠◕⁠ᴗ⁠◕⁠✿)", "(⁠づ⁠｡⁠◕⁠‿⁠‿⁠◕⁠｡⁠)⁠づ", "(⁠｡⁠•̀⁠ᴗ⁠-⁠)⁠✧", "¯\\_(ツ)_/¯",
        "(⁠╯⁠°⁠□⁠°⁠）⁠╯⁠︵⁠ ⁠┻⁠━⁠┻", "(⁠*⁠´⁠ω⁠｀⁠*⁠)", "(⁠人⁠ ⁠•͈⁠ᴗ⁠•͈⁠)", "(⁠◠⁠‿⁠◕⁠)",
        "🎉 Great Job!", "🚀 Super Fast", "🔥 Fire!", "💜 Privacy First",
        "🛡️ 100% Offline", "✨ Awesome!", "💯 Perfect", "👍 Top Quality"
    )
}
