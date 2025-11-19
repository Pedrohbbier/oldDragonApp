package com.olddragon.app.models.battle

/**
 * Lista de inimigos pré-definidos baseados no Old Dragon
 */
object Enemies {

    fun getDefaultEnemies(): List<Enemy> {
        return listOf(
            // Nível 1
            Enemy(
                name = "Goblin",
                level = 1,
                maxHp = 5,
                currentHp = 5,
                armorClass = 12,
                attackBonus = 2,
                damageRoll = "1d6",
                xpReward = 50
            ),
            Enemy(
                name = "Rato Gigante",
                level = 1,
                maxHp = 4,
                currentHp = 4,
                armorClass = 11,
                attackBonus = 1,
                damageRoll = "1d4",
                xpReward = 25
            ),
            Enemy(
                name = "Kobold",
                level = 1,
                maxHp = 6,
                currentHp = 6,
                armorClass = 13,
                attackBonus = 2,
                damageRoll = "1d6",
                xpReward = 50
            ),

            // Nível 2
            Enemy(
                name = "Orc",
                level = 2,
                maxHp = 12,
                currentHp = 12,
                armorClass = 14,
                attackBonus = 3,
                damageRoll = "1d8+1",
                xpReward = 100
            ),
            Enemy(
                name = "Esqueleto",
                level = 2,
                maxHp = 10,
                currentHp = 10,
                armorClass = 13,
                attackBonus = 2,
                damageRoll = "1d6",
                xpReward = 75
            ),
            Enemy(
                name = "Zumbi",
                level = 2,
                maxHp = 14,
                currentHp = 14,
                armorClass = 12,
                attackBonus = 2,
                damageRoll = "1d8",
                xpReward = 100
            ),

            // Nível 3
            Enemy(
                name = "Gnoll",
                level = 3,
                maxHp = 18,
                currentHp = 18,
                armorClass = 15,
                attackBonus = 4,
                damageRoll = "1d8+2",
                xpReward = 150
            ),
            Enemy(
                name = "Hobgoblin",
                level = 3,
                maxHp = 16,
                currentHp = 16,
                armorClass = 16,
                attackBonus = 4,
                damageRoll = "1d8+1",
                xpReward = 150
            ),
            Enemy(
                name = "Lobo Atroz",
                level = 3,
                maxHp = 15,
                currentHp = 15,
                armorClass = 14,
                attackBonus = 3,
                damageRoll = "2d6",
                xpReward = 125
            ),

            // Nível 4
            Enemy(
                name = "Ogro",
                level = 4,
                maxHp = 28,
                currentHp = 28,
                armorClass = 15,
                attackBonus = 5,
                damageRoll = "2d6+3",
                xpReward = 250
            ),
            Enemy(
                name = "Ghoul",
                level = 4,
                maxHp = 22,
                currentHp = 22,
                armorClass = 14,
                attackBonus = 4,
                damageRoll = "1d8+2",
                xpReward = 200
            ),
            Enemy(
                name = "Owlbear",
                level = 4,
                maxHp = 30,
                currentHp = 30,
                armorClass = 15,
                attackBonus = 5,
                damageRoll = "2d6+2",
                xpReward = 275
            ),

            // Nível 5
            Enemy(
                name = "Troll",
                level = 5,
                maxHp = 35,
                currentHp = 35,
                armorClass = 16,
                attackBonus = 6,
                damageRoll = "2d8+3",
                xpReward = 400
            ),
            Enemy(
                name = "Wight",
                level = 5,
                maxHp = 32,
                currentHp = 32,
                armorClass = 16,
                attackBonus = 5,
                damageRoll = "1d10+3",
                xpReward = 350
            ),
            Enemy(
                name = "Mantícora",
                level = 5,
                maxHp = 38,
                currentHp = 38,
                armorClass = 17,
                attackBonus = 6,
                damageRoll = "2d6+4",
                xpReward = 450
            ),

            // Boss de nível baixo
            Enemy(
                name = "Dragão Jovem",
                level = 6,
                maxHp = 50,
                currentHp = 50,
                armorClass = 18,
                attackBonus = 7,
                damageRoll = "3d6+4",
                xpReward = 600
            )
        )
    }
}
