/* -----------------------------------------------------------------------------------
*
*   Card Test 2 : unit tests for the Village Card function
*	Include the following lines in your makefile:
*
*	testVillage: cardtest2.c dominion.o rngs.o
*		gcc -o testVillage -g cardtest2.c dominion.o rngs.o $(CFLAGS)		
*
----------------------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "dominion.h"
#include "dominion_helpers.h"
#include "rngs.h"

void assertTrue(int result){
    if (result == 1){
        printf("PASSED");
    }
    else{
        printf("FAILED");
    }
}

void testVillage(){
    
    printf("TESTING village - Positive Test:\n\n");
    
    int seed = 1000;
    int numPlayer = 4;
    int p, r, handCount;
    int bonus = 0;
    int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, great_hall};
    struct gameState G;
    int maxHandCount = 5;
    int action;
    int maxActions = 3;

    
    for (p = 0; p < numPlayer; p++)
    {
        for (handCount = 0; handCount < maxHandCount; handCount++)
        {
            for (action = 0; action < maxActions; action++)
            {
                memset(&G, 23, sizeof(struct gameState));
                r = initializeGame(numPlayer, k, seed, &G);
                G.handCount[p] = handCount;
                G.numActions = action;
                int origHandCount = G.handCount[p];
                int origActCount = G.numActions;
                G.whoseTurn = p;
                cardEffect(k[7], 0, 1, 2, &G, handCount, &bonus);
                
                printf("INPUT player = %d, cards = %d, actions = %d.\n\n", p, origHandCount, origActCount);
                assertTrue(G.handCount[p] == origHandCount);
                printf("\tExpected HandCount:\t%d, New HandCount:\t%d\n", origHandCount, G.handCount[p]);
                assertTrue(G.numActions == origActCount + 2);
                printf("\tExpected Actions:\t%d, New Actions:\t%d\n\n", origActCount + 2, G.numActions);
            }
        }
    }
}


int main(){

	testVillage();
    return 0;

}
