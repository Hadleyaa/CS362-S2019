/* -----------------------------------------------------------------------------------
*
*   Card Test 1 : unit tests for the Smithy Card function
*	Include the following lines in your makefile:
*
*	testSmithy:	cardtest1.c dominion.o rngs.o
*		gcc -o testSmithy -g cardtest1.c dominion.o rngs.o $(CFLAGS)		
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

void testSmithy(){
    
    printf("TESTING smithy - Boundary Test:\n\n");
    
    int seed = 1000;
    int numPlayer = 2;
    int p, r, handCount;
    int bonus = 0;
    int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, great_hall};
    struct gameState G;
    int maxHandCount = 5;
    int hands[] = {-1, 0, 1, maxHandCount + 1};
    int numTests = 5;
    int bonusVals[] = {-1, 0, 1, 3};
    int numBonus = 4;
    
    for (p = -1; p < numPlayer; p++)
    {
        for (handCount = 0; handCount < numTests; handCount++)
        {
            for (bonus = 0; bonus < numBonus; bonus++)
            {
                memset(&G, 23, sizeof(struct gameState));
                r = initializeGame(numPlayer, k, seed, &G);
                G.whoseTurn = p;
                G.handCount[p] = hands[handCount];
                cardEffect(k[6], 0, 1, 2, &G, handCount, &bonusVals[bonus]);
                assertTrue(G.handCount[p] == hands[handCount] + 2);
                
                printf(" - INPUT player = %d, cards = %d, bonus = %d.\t", p, hands[handCount], bonusVals[bonus]);
                printf(" RESULTS New HandCount = %d, Expected HandCount = %d\n", G.handCount[p], hands[handCount] + 2);
            }
        }
    }
}


int main(){

	testSmithy();
    return 0;

}
