package co.iskv.crypto.widget.models


enum class Direction {
    FALL, UP;

    companion object {
        fun getDirection(percent: Double): Direction =
            if (percent >= percent) UP
            else FALL
    }

}
