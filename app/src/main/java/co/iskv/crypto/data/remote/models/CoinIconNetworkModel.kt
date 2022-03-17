package co.iskv.crypto.data.remote.models

import com.google.gson.annotations.SerializedName

data class CoinIconNetworkModel(
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("url")
    val url: String
)
