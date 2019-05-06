/* -----------------------------------------------------------------------------------
*
*   Unit Test 3 : unit tests for the seahagEffect function
*	Include the following lines in your makefile:
*
*	testSeahag:	unittest3.c dominion.o rngs.o
*		gcc -o testSeahag -g unittest3.c dominion.o rngs.o $(CFLAGS)		
*
----------------------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include "dominion.h"
#include "dominion_helpers.h"
#include "rngs.h"

void assertTrue(int result){
    if (result == 1)
    {
        printf("\tPASSED");
    }
    else
    {
        printf("\tFAILED");
    }
}

void testSeahag(){

	printf("TESTING seahag - Positive Test:\n");
    
    int i;
    int seed = 1000;
    int numPlayer = 4;
    int p, r;
    int bonus;
    int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, sea_hag};
    struct gameState G;
    int startFirstCard[numPlayer];
    int startDeckSize[numPlayer];
    
    for (p = 0; p < numPlayer; p++)
    {
        memset(&G, 23, sizeof(struct gameState));
        r = initializeGame(numPlayer, k, seed, &G);
        
        for (i = 0; i < numPlayer; i++)
        {
            startDeckSize[i] = G.deckCount[i];
            startFirstCard[i] = G.deck[i][G.deckCount[i] - 1];
        }
        
        G.whoseTurn = p;
        cardEffect(k[9], 0, 1, 2, &G, 5, &bonus);
        
        printf("Current Player = %d\n", p);

        for (i = 0; i < numPlayer; i++)
        {
            if (p == i){
                assertTrue((G.deck[i][G.deckCount[i] + G.discardCount[i] - 1] == startFirstCard[i]));
            }
            else
            {
                assertTrue((G.deck[i][G.deckCount[i] + G.discardCount[i] + 1] == startFirstCard[i]) && (G.deck[i][G.deckCount[i] + 1] == 0));
            }
            
            printf(" - player %d: original top card = %d\t", i, startFirstCard[i]);
            
            if (p == i){
                printf("new top card = %d\n", G.deck[i][G.deckCount[i] - 1]);
            }
            else{
                printf("new top card = %d\n", G.deck[i][G.deckCount[i] + 1]);
            }
        }
    }
}


int main(){

	testSeahag();
    return 0;

}
