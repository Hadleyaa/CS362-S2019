/* -----------------------------------------------------------------------------------
*
*   Unit Test 1 : unit tests for the smithEffect function
*	Include the following lines in your makefile:
*
*	testSmithy:	unittest1.c dominion.o rngs.o
*		gcc -o testSmithy -g unittest1.c dominion.o rngs.o $(CFLAGS)		
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

void testSmithy(){

	printf("TESTING smithy:\n\n");
    
    int seed = 1000;
	int numPlayer = 2;
	int maxBonus = 2;
	int p, r, handCount;
	int bonus;
	int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, great_hall};
	struct gameState G;
	int maxHandCount = 5;


    for (p = 0; p < numPlayer; p++)
	{
		for (handCount = 1; handCount <= maxHandCount; handCount++)
		{
			for (bonus = 0; bonus <= maxBonus; bonus++)
			{
				memset(&G, 23, sizeof(struct gameState));
				r = initializeGame(numPlayer, k, seed, &G);
                G.whoseTurn = p;
				G.handCount[p] = handCount;
				cardEffect(k[6], 0, 1, 2, &G, handCount, &bonus);
                assertTrue(G.handCount[p] == handCount + 2);
                
                printf(" - INPUT player = %d, cards = %d, bonus = %d.\t", p, handCount, bonus);
				printf(" RESULTS New HandCount = %d, Expected HandCount = %d\n", G.handCount[p], handCount + 2);
                
			}
		}
	} 
}


int main(){

	testSmithy();
    return 0;

}
