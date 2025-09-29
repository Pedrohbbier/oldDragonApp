package com.olddragon.app.models.items

class Armor(
    name: String,
    price: Int,
    val defense: Int,
): Item(name, price)