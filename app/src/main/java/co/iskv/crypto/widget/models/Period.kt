package co.iskv.crypto.widget.models

enum class Period() {
    DAY {
        override fun getValue(): String = "1DAY"
    },
    WEEK {
        override fun getValue(): String = "7DAY"
    },
    MONTH {
        override fun getValue(): String = "1MTH"
    },
    HALF_YEAR {
        override fun getValue(): String = "6MTH"
    },
    YEAR {
        override fun getValue(): String = "1YRS"
    };

    abstract fun getValue(): String

}