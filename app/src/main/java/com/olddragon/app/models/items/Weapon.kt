package com.olddragon.app.models.items

class Weapon(
    name: String,
    price: Int,
    val type: String,
    val damage: Int,
): Item(name, price)