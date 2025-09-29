package models.character.hability

object Habilities {
    val rowdy = Hability(
        "Rebeldia",
        "Humanos nunca estão satisfeitos com o estado das coisas, " +
                "sua rebeldia natural os fazem notórios encrenqueiros. " +
                "<+2 em BA>",
        "atribute",
        "ba",
        2
    )
    val curiosity = Hability(
        "Curiosidade",
        "A curiosidade humana fez com que eles, " +
                "mesmo não tendo uma origem tão bela, " +
                "ainda sim possam prosperar nesta terra." +
                "<+1 int mod>",
        "skill",
        "int",
        1
    )
    val agility = Hability(
        "Agilidade",
        "Elfos são conhecidos por sua agilidade sobrenatural. " +
                "<+2 em JP>",
        "atribute",
        "jp",
        2
    )
    val longLives = Hability(
        "Centenários",
        "Todas as raças acumulam sabedoria durante suas vidas, " +
                "a diferença é que os elfos pode chegar fácilmente até os 1000 anos. " +
                "<+1 wis mod>",
        "skill",
        "wis",
        1
    )
    val hardShell = Hability(
        "Casca-grossa",
        "Não há trabalho bruto demais para um anão. <+2 em CA>",
        "atribute",
        "ca",
        2
    )
    val pubBrother = Hability(
        "Amigo de bar",
        "Depois de tanto trabalho, " +
                "anões sabem como aproveitar um merecido descanso. " +
                "<+1 cha mod>",
        "skill",
        "cha",
        1
    )
}