/* -----------------------------------------------------------------------------------
*
*   Random Test Card 2 : Random tests for the cutpurseEffect function
*	Include the following lines in your makefile:
*
*	randomtestcard2: randomtestcard2.c dominion.o rngs.o
*       gcc -o randomtestcard2 -g  randomtestcard2.c dominion.o rngs.o $(CFLAGS)		
*
----------------------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <time.h>
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

void getCoppers(struct gameState *state, int arr[]){
    
    for (int j = 0; j < state->numPlayers; j++)
    {
        int count = 0;
        for (int i = 0; i < state->handCount[j]; i++)
        {
            if (state->hand[j][i] == 4)
            {
                count++;
            }
        }
        arr[j] = count;
    }
}

void randomTestCutpurse(){

	printf("TESTING cutpurse - Random Test:\n\n");

    int i, j;
    int numTests = 100000;
    int seed = 1000;
    int numPlayer = 4;
    int maxPlayer = 4;
    int p, np, r;
    int bonus;
    int k[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, cutpurse};
    struct gameState G;
    int hc;
    int startCoins;
    
    for (int t = 0; t < numTests; t++)
    {
        np = 2 + (rand() % (maxPlayer - 1));
        p = rand() % np;
        
        int startCoppers[np];
        int endCoppers[np];

        memset(&G, 23, sizeof(struct gameState));
        r = initializeGame(numPlayer, k, seed, &G);
        
        for (j = 0; j < np; j++)
        {
            hc = rand() % 10 + 1;
            for (i = 0; i < hc; i++)
            {
                G.hand[j][i] = rand() % 10;
            }
            G.handCount[j] = hc;
        }
        
        G.whoseTurn = p;
        updateCoins(p, &G, 0);
        
        startCoins = G.coins;
        getCoppers(&G, startCoppers);
    
        cardEffect(k[9], 0, 1, 2, &G, 5, &bonus);
        
        getCoppers(&G, endCoppers);
        
        printf("INPUT players = %d, current player = %d, player coins = %d\n", np, p + 1, startCoins);
        
        for (j = 0; j < np; j++)
        {
            if (j == p){
                assertTrue(G.coins == startCoins + 2);
                printf(" - player = %d, coins = %d, start coppers = %d, end coppers = %d\n", j + 1, G.coins, startCoppers[j], endCoppers[j]);
            }
            else
            {
                if (startCoppers[j] > 0){
                    assertTrue(startCoppers[j] == endCoppers[j] + 1);
                }
                else{
                    assertTrue(startCoppers[j] == 0);
                }
                
                printf(" - player = %d, start coppers = %d, end coppers = %d\n", j + 1, startCoppers[j], endCoppers[j]);
            }
        }
        
        printf("\n");
    }
}

int main(){

    srand(time(NULL));
	randomTestCutpurse();
    return 0;

}
