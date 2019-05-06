/* -----------------------------------------------------------------------------------
 *
 *   Card Test 3 : unit tests for the Outpost Card function
 *    Include the following lines in your makefile:
 *
 *    cardOutpost: cardtest3.c dominion.o rngs.o
 *        gcc -o cardOutpost -g cardtest3.c dominion.o rngs.o $(CFLAGS)
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

void cardOutpost(){
    
    printf("TESTING outpost - Positive Test:\n\n");
    
    int seed = 1000;
    int numPlayer = 4;
    int p, r, handCount;
    int bonus = 0;
    int k[10] = {adventurer, council_room , feast, gardens, mine, remodel, smithy, outpost, baron, great_hall};
    struct gameState G;
    int maxHandCount = 5;
    int outpost;
    int maxOutpost = 3;
    
    
    for (p = 0; p < numPlayer; p++)
    {
        for (handCount = 0; handCount < maxHandCount; handCount++)
        {
            for (outpost = 0; outpost < maxOutpost; outpost++)
            {
                memset(&G, 23, sizeof(struct gameState));
                r = initializeGame(numPlayer, k, seed, &G);
                G.handCount[p] = handCount;
                G.outpostPlayed = outpost;
                G.whoseTurn = p;
                int origHandCount = G.handCount[p];
                int origOutpostCount = G.outpostPlayed;
                cardEffect(k[7], 0, 1, 2, &G, handCount, &bonus);
                
                printf("INPUT player = %d, cards = %d, actions = %d.\n\n", p, origHandCount, origOutpostCount);
                assertTrue(G.handCount[p] == origHandCount - 1);
                printf("\tExpected HandCount:\t%d, New HandCount:\t%d\n", origHandCount, G.handCount[p]);
                assertTrue(G.outpostPlayed == origOutpostCount + 1);
                printf("\tExpected Actions:\t%d, New Actions:\t%d\n\n", origOutpostCount + 1, G.outpostPlayed);
            }
        }
    }
}


int main(){
    
    cardOutpost();
    return 0;
    
}
