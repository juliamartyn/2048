# 2048
Game 2048 - Java swing 
# Requirements
 * "Non-greedy" movement. The tiles that were created by combining other tiles should not be combined again during the same turn (move). That is to say that moving the tile row of [2][2][2][2] to the right should result in ......[4][4] and not .........[8]
 * "Move direction priority". If more than one variant of combining is possible, move direction shows one that will take effect. For example, moving the tile row of ...[2][2][2] to the right should result in ......[2][4] and not ......[4][2]
 * Adding a new tile on a blank space. Most of the time new "2" is to be added and occasionally (10% of the time) - "4"
 * Check for valid moves. The player shouldn't be able to skip their turn by trying a move that doesn't change the board.
 * Win condition. The player wins if there is [2048] tile
 * Lose condition. The player loses if there are no valid moves
 # Screenshot
![Jave 2048 swing](https://github.com/juliamartyn/2048/master/2048Screenshot.JPG)
