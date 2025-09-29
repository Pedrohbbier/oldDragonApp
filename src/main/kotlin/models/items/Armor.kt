package models.items

class Armor(
    name: String,
    price: Int,
    defense: Int,
): Item(name, price) {}