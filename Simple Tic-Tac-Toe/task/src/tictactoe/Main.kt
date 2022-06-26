package tictactoe

import kotlin.math.abs

// Enter the size of the board
const val BOARD_ROWS = 3
const val BOARD_COLUMNS = 3

const val PIECES_IN_A_ROW_NEEDED = 3

fun getBoard(gameState: String): MutableList<MutableList<String>> {

    // Replace underscores with spaces
    val properGameState = gameState.replace("_", " ")

    val board = mutableListOf<MutableList<String>>()

    // Iterate through the number of rows
    for (i in 0 until BOARD_ROWS) {

        // Add a new list for this row
        board.add(mutableListOf())

        // Iterate through the number of columns
        for (j in 0 until BOARD_COLUMNS) {

            board[i].add("${properGameState[i * BOARD_ROWS + j]}")
        }
    }

    return board
}

fun printBoard(gameBoard: MutableList<MutableList<String>>) {
    // Get the board

    println("---------")

    // Iterate through the number of rows
    for (i in 0 until BOARD_ROWS) {

        print("|")

        // Iterate through the number of columns
        for (j in 0 until BOARD_COLUMNS) {

            print(" ${gameBoard[i][j]}")
        }

        print(" |\n")
    }

    println("---------")
}

// Checks for diagonals from left-to-right
fun checkForDiagonalWinLeftToRight(
    gameBoard: MutableList<MutableList<String>>,
    playerName: String,
    startRowIndex: Int,
    startColumnIndex: Int
): Boolean {

    // Holds whether a diagonal win was found
    // No win is assumed by default
    var winFound = false

    // Iterate through the number of pieces in a row to win
    for (i in 0 until PIECES_IN_A_ROW_NEEDED) {
        // If this row/column is within the number of available rows/columns
        if (startRowIndex + i in 0 until BOARD_ROWS && startColumnIndex + i in 0 until BOARD_COLUMNS) {
            // Get the next piece to check
            val nextPieceToCheck = gameBoard[startRowIndex + i][startColumnIndex + i]

            // If this piece does not match the player name
            if (nextPieceToCheck != playerName) break

            // If this is the last iteration
            if (i == PIECES_IN_A_ROW_NEEDED - 1) winFound = true
        }
    }

    // No diagonal win was found
    return winFound
}

fun checkForDiagonalWinRightToLeft(
    gameBoard: MutableList<MutableList<String>>,
    playerName: String,
    startRowIndex: Int,
    startColumnIndex: Int
): Boolean {

    // Holds whether a diagonal win was found
    // No win is assumed by default
    var winFound = false

    // Checks for diagonals from left-to-right
    // Iterate through the number of pieces in a row to win
    for (i in 0 until PIECES_IN_A_ROW_NEEDED) {

        // If this row/column is within the number of available rows/columns
        if (startRowIndex + i in 0 until BOARD_ROWS && startColumnIndex - i in 0 until BOARD_COLUMNS) {

            // Get the next piece to check
            val nextPieceToCheck = gameBoard[startRowIndex + i][startColumnIndex - i]

            // If this piece does not match the player name
            if (nextPieceToCheck != playerName) break

            // If this is the last iteration
            if (i == PIECES_IN_A_ROW_NEEDED - 1) winFound = true
        }
    }

    // No diagonal win was found
    return winFound
}

fun checkForColumnWin(gameBoard: MutableList<MutableList<String>>, playerName: String): Boolean {

    // Create the winning string
    val winningString = playerName.repeat(PIECES_IN_A_ROW_NEEDED)

    val currentColumns = MutableList(BOARD_COLUMNS) { "" }

    // Iterate through the number of rows
    for (i in 0 until BOARD_ROWS) {

        // Iterate through the number of columns
        for (j in 0 until BOARD_COLUMNS) {

            // Add the piece at the current column
            currentColumns[j] += gameBoard[i][j]
        }
    }

    // Return true the current player has 3 pieces in any column
    return currentColumns.any { i -> i.contains(winningString) }
}

fun checkForRowWin(gameBoard: MutableList<MutableList<String>>, playerName: String): Boolean {

    // Create the winning string
    val winningString = playerName.repeat(PIECES_IN_A_ROW_NEEDED)

    // Iterate through the number of rows
    for (i in 0 until BOARD_ROWS) {

        // Set the row to an empty string
        var currentRow = ""

        // Iterate through the number of columns
        for (j in 0 until BOARD_COLUMNS) {

            // Add the piece to the current row
            currentRow += gameBoard[i][j]
        }

        // If the current player has 3 pieces in a row
        if (currentRow.contains(winningString)) return true
    }

    // Return false since no row contained a winning string
    return false
}

fun hasWon(gameBoard: MutableList<MutableList<String>>, playerName: String): Boolean {

    // If the player has won via a row combination, then return true
    if (checkForRowWin(gameBoard, playerName)) return true

    // If the player has won via a column  combination, then return true
    if (checkForColumnWin(gameBoard, playerName)) return true

    // Iterate through the number of rows
    for (i in 0 until BOARD_ROWS) {

        // Iterate through the number of columns
        for (j in 0 until BOARD_COLUMNS) {

            // If the player has won via a diagonal left-to-right combination, then return true
            if (checkForDiagonalWinLeftToRight(gameBoard, playerName, i, j)) return true

            // If the player has won via a diagonal right-to-left combination, then return true
            if (checkForDiagonalWinRightToLeft(gameBoard, playerName, i, j)) return true
        }
    }

    // Return false if no win condition was found
    return false
}

fun getStats(gameBoard: MutableList<MutableList<String>>): String {
    // Count the number of Xs/Os and underscores
    val countX = gameBoard.flatten().count { c -> c == "X" }
    val countO = gameBoard.flatten().count { c -> c == "O" }
    val countUnderscores = gameBoard.flatten().count { c -> c == " " }

    // Query who has won
    val hasWonX = hasWon(gameBoard, "X")
    val hasWonO = hasWon(gameBoard, "O")

    val gameResult: String

    // If the game is in an impossible state, return here
    if ((hasWonX && hasWonO) || abs(countO - countX) >= 2) gameResult = "Impossible"
    // If neither player has won and no positions are left, exit with a draw
    else if (!hasWonX && !hasWonO && countUnderscores == 0) gameResult = "Draw"
    else if (hasWonX) gameResult = "X wins"
    else if (hasWonO) gameResult = "O wins"
    else gameResult = "Game not finished"

    return gameResult
}

fun getUserInput() {

    // Get an empty game board
    val gameBoard = getBoard(" ".repeat(BOARD_COLUMNS * BOARD_ROWS))

    var currentPlayer = "X"

    while (true) {

        print("Enter the coordinates: ")
        val coordinates = readln()

        try {

            val (row, column) = coordinates.split(" ").map { el -> el.toInt() }

            // If the coordinates are not in 1 to 3
            if (row !in 1..BOARD_ROWS || column !in 1..BOARD_COLUMNS)
                println("Coordinates should be from 1 to $BOARD_COLUMNS!")
            // If the coordinates are occupied
            else if (gameBoard[row - 1][column - 1] != " ") println("This cell is occupied! Choose another one!")
            // If the move was valid
            else {

                // Update the game board to reflect a new position has been taken
                gameBoard[row - 1][column - 1] = currentPlayer
                printBoard(gameBoard)

                // Change the player
                currentPlayer = if (currentPlayer == "X") "O" else "X"

                // Get the game result
                val gameResult = getStats(gameBoard)
                if (gameResult in mutableListOf("X wins", "O wins", "Draw")) {

                    // Print the game result and exit the loop
                    println(gameResult)
                    break
                }
            }
        } catch (ignored: java.lang.NumberFormatException) {
            println("You should enter numbers!")
        }
    }
}

fun main() {

    // Start accepting user input
    getUserInput()
}
