package fr.mpek29.zethydex

//creation of a classical model of data
class ZEthyModel(
    val id: Int?,
    //we use ByteArray for save the picture
    var image: ByteArray,
    var name: String = "",
    var description: String = "",
    var apartment: String = "",
    var favoriteDrink: String = ""
)