package models.items

class Weapon(
    name: String,
    price: Int,
    type: String,
    damage: Int,
): Item(name,price) {}