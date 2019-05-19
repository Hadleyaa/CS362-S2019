/* -----------------------------------------------------------------------------------
 *
 *   Random Test Card 3  : Random tests for the Adventurer Card function
 *    Include the following lines in your makefile:
 *
 *   randomtestcard3: randomtestcard3.c dominion.o rngs.o
 *      gcc -o randomtestcard3 -g  randomtestcard3.c dominion.o rngs.o $(CFLAGS)
 *
 ----------------------------------------------------------------------------------- */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "dominion.h"
#include "dominion_helpers.h"
#include "rngs.h"

void assertTrue(int result){
    if (result == 1){
        printf("\tPASSED");
    }
    else{
        printf("\tFAILED");
    }
}

void randomTestAdventurer(){
    
    printf("TESTING Adventurer - Random Test:\n\n");

    int numTests = 10;
    int seed = 1000;
    int maxPlayer = 4;
    int r;
    int bonus = 0;
    int k[10] = {adventurer, council_room , feast, gardens, mine, remodel, smithy, outpost, baron, great_hall};
    struct gameState G;

    for (int t = 0; t < numTests; t++){

        int np = 2 + (rand() % (maxPlayer - 1));
        int p = rand() % np;
        
        memset(&G, 23, sizeof(struct gameState));
        r = initializeGame(np, k, seed, &G);
        G.whoseTurn = p;
        
        for(int i = 0; i < np; i++)
        {
            for(int j = 0; j < G.deckCount[i]; j++)
            {
                G.deck[i][j] = rand() % 10;
                G.discard[i][j] = 4;
            }
        }
        
        int dSize = G.deckCount[p];
        int eSize;
        int searched = 0;
        int cnt = 0;
        for (int n = G.deckCount[p] - 1; n >= 0; n--)
        {
            searched++;
            if (G.deck[p][n] == 4 || G.deck[p][n] == 5 || G.deck[p][n] == 6 )
            {
                cnt ++;
            }
            if (cnt >= 2)
            {
                break;
            }
        }
        eSize = dSize - searched;

        
        printf("INPUT numPlayers = %d, curPlayer = %d, sequence = ", np, p);
        for (int v = G.deckCount[p] - 1; v >= 0; v--)
        {
            printf("%d ", G.deck[p][v]);
        }
        printf("\n\n");
        
        cardEffect(k[0], 0, 1, 2, &G, G.handCount[p], &bonus);
        assertTrue(G.deckCount[p] == eSize);
        printf("\tExpected DeckCount:\t%d, New DeckCount:\t%d\n\n", eSize, G.deckCount[p]);  

    }         
}

int main(){
    
    srand(time(NULL));
    randomTestAdventurer();
    return 0; 
}
