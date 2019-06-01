/* -----------------------------------------------------------------------------------
*
*   Unit Test 4 : unit tests for the cutpurseEffect function
*	Include the following lines in your makefile:
*
*	testCutpurse:	unittest4.c dominion.o rngs.o
*		gcc -o testCutpurse -g unittest4.c dominion.o rngs.o $(CFLAGS)		
*
----------------------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "dominion.h"
#include "dominion_helpers.h"
#include "rngs.h"

void assertTrue(int result){
    if (result == 1)
    {
        printf("PASSED");
    }
    else
    {
        printf("FAILED");
    }
}

void testCutpurse(){

	printf("TESTING cutpurse - Boundary Test:\n\n");

    int card[] = {curse, copper, gold, -1};
    int seed = 1000;
    int numPlayer = 4;
    int p, r;
    int bonus;
    int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, cutpurse};
    struct gameState G;
    
    
    for (p = 0; p < numPlayer; p++)
    {
        memset(&G, 23, sizeof(struct gameState));
        r = initializeGame(numPlayer, k, seed, &G);
        
        for (int i = 0; i < 5; i++)
        {
            G.hand[p][i] = card[p];
        }
        G.handCount[p] = 5;
        updateCoins(p, &G, 0);
        int startCoins = G.coins;
        
        G.whoseTurn = p;
        cardEffect(k[9], 0, 1, 2, &G, 5, &bonus);
        assertTrue(G.coins == startCoins + 2);
        printf(" - INPUT player = %d, coins = %d\t", p, startCoins);
        printf("RESULTS New Coins = %d, Expected Coins = %d\n", G.coins, startCoins + 2);
    }
}

int main(){

	testCutpurse();
    return 0;

}
