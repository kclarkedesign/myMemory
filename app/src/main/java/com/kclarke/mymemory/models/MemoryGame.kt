package com.kclarke.mymemory.models

import com.kclarke.mymemory.utils.DEFAULT_ICONS

class MemoryGame (private val boardSize: BoardSize) {
    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        // Three cases:
        // 0 cards flipped=> restore cards + flip selected
        // 1 card flipped => flip over selected + check for match
        // 2 cards flipped => restore cards + flip selected
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 cards previously flipped
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // exactly 1 card previously flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier  != cards[position2].identifier) {
            return false
        }

        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true

    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}
