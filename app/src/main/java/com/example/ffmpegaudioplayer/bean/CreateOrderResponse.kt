data class CreateOrderResponse(
    val `data`: Data,
    val status: Boolean
)

data class Data(
    val orderId: Long,
    val prepayData: PrepayData
)

data class PrepayData(
    val appId: String,
    val nonceStr: String,
    val `package`: String,
    val paySign: String,
    val signType: String,
    val timeStamp: String
)
