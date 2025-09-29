package models.charClass

interface icharClass {

    fun stdAttack()

    fun effectAttack()

    fun specialAttack()

    fun ultraAttack()

    /*

    A quem for implementar o sistema de classes:

        Use o padrão strategy, (cada classe de personagem herda esta interface) que por sua vez é implementada
        na classe Character.kt.

        Para facilitar, cada personagem vai ter:
            um ataque genérico, que dá pouco dano,
            uma habilidade que adiciona um efeito negativo no inimigo,
            uma habilidade especial fraca,
            uma "ultimate", que é muito forte
            (talvez implementar uma barra de super, para usar os últimos dois ataques mencionados)

    */
}