/* -----------------------------------------------------------------------------------
 *
 *   Card Test 4 : unit tests for the Adventurer Card function
 *    Include the following lines in your makefile:
 *
 *    cardAdventurer: cardtest4.c dominion.o rngs.o
 *        gcc -o cardAdventurer -g cardtest4.c dominion.o rngs.o $(CFLAGS)
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
        printf("\tPASSED");
    }
    else{
        printf("\tFAILED");
    }
}

void cardAdventurer(){
    
    printf("TESTING Adventurer - Boundary Test:\n\n");

    int i, j, m;
    int seed = 1000;
    int numPlayer = 4;
    int r;
    int bonus = 0;
    int k[10] = {adventurer, council_room , feast, gardens, mine, remodel, smithy, outpost, baron, great_hall};
    struct gameState G;

    int treasure[] = {copper, silver, gold};
    int treasureTypes = 3;
    int c;
    
    for (i = 0; i < treasureTypes; i++)
    {
        c = 0;
        for (j = 0; j < 6; j++)
        {
            memset(&G, 23, sizeof(struct gameState));
            r = initializeGame(numPlayer, k, seed, &G);
            for(m = 0; m < G.deckCount[0]; m++)
            {
                if (m < c){
                    G.deck[0][m] = treasure[i];
                }
                else{
                    G.deck[0][m] = 0;
                }
            }
            
            int dSize = G.deckCount[0];
            int eSize;
            int searched = 0;
            int cnt = 0;
            for (int n = G.deckCount[0] - 1; n >= 0; n--)
            {
                searched++;
                if (G.deck[0][n] == treasure[i])
                {
                    cnt ++;
                }
                if (cnt >= 2)
                {
                    break;
                }
            }
            eSize = dSize - searched;
            
            

            printf("INPUT treasure type = %d, treasure cards = %d, sequence = ", treasure[i], c);
            for (int v = 0; v < G.deckCount[0]; v++){
                printf("%d ", G.deck[0][v]);
            }
            printf("\n\n");
            
            cardEffect(k[0], 0, 1, 2, &G, G.handCount[0], &bonus);
            assertTrue(G.deckCount[0] == eSize);
            printf("\tExpected DeckCount:\t%d, New DeckCount:\t%d\n\n", eSize, G.deckCount[0]);
            
            c++;
        }
        
        c = 0;
        for (j = 5; j >= 0; j--)
        {
            memset(&G, 23, sizeof(struct gameState));
            r = initializeGame(numPlayer, k, seed, &G);
            for(m = G.deckCount[0] - 1; m >= 0; m--)
            {
                if (m >= c){
                    G.deck[0][m] = treasure[i];
                }
                else{
                    G.deck[0][m] = 0;
                }
            }
            
            int dSize = G.deckCount[0];
            int eSize;
            int searched = 0;
            int cnt = 0;
            for (int n = G.deckCount[0] - 1; n >= 0; n--)
            {
                searched++;
                if (G.deck[0][n] == treasure[i])
                {
                    cnt ++;
                }
                if (cnt >= 2)
                {
                    break;
                }
            }
            eSize = dSize - searched;
            
            printf("INPUT treasure type = %d, treasure cards = %d, sequence = ", treasure[i], c);
            for (int v = 0; v < G.deckCount[0]; v++){
                printf("%d ", G.deck[0][v]);
            }
            printf("\n\n");
            
            cardEffect(k[0], 0, 1, 2, &G, G.handCount[0], &bonus);
            assertTrue(G.deckCount[0] == eSize);
            printf("\tExpected DeckCount:\t%d, New DeckCount:\t%d\n\n", eSize, G.deckCount[0]);
            c++;
        }
    }
}


int main(){
    
    cardAdventurer();
    return 0; 
}
