package co.iskv.crypto.widget.models

data class CoinModel(
    val name: String,
    val percent: PercentModel,
    val min: Double,
    val max: Double,
    val period: Period,
    val legend: List<String>,
    val values: List<PointDetailsModel>
) {

    fun getCurrentPrice(): PointDetailsModel = values.last()

}