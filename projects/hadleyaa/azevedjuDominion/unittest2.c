/* -----------------------------------------------------------------------------------
 *
 *   Unit Test 2 : unit tests for the adventurerEffect function
 *    Include the following lines in your makefile:
 *
 *    testAdventurer:    unittest2.c dominion.o rngs.o
 *        gcc -o testAdventurer -g unittest2.c dominion.o rngs.o $(CFLAGS)
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

void testAdventurer(){
    
    printf("TESTING adventurer - Positive Test:\n");
    
    int seed = 1000;
    int numPlayer = 4;
    int r;
    int bonus = 0;
    int k[10] = {adventurer, council_room , feast, gardens, mine, remodel, smithy, outpost, baron, great_hall};
    struct gameState G;
    
    int c1, c2, c3;
    int cards[] = {0, 1, 2, copper, silver, gold};
    int treasure[] = {copper, silver, gold};
    int types = 6;
    int startDeckSize;
    int expDeckSize;
    
    //Initialize hands
    // first card inserted
    for (c1 = 0; c1 < types; c1++){
        // second card inserted
        for (c2 = 0; c2 < types; c2++){
            // third card inserted
            for (c3 = 0; c3 < types; c3++){
                
                memset(&G, 23, sizeof(struct gameState));
                r = initializeGame(numPlayer, k, seed, &G);
                
                G.deckCount[0] = 3;
                G.deck[0][0] = cards[c1];
                G.deck[0][1] = cards[c2];
                G.deck[0][2] = cards[c3];
                
                //Determine Expected values
                startDeckSize = G.deckCount[0];
                
                int searched = 0;
                int count = 0;
                for (int n = G.deckCount[0] - 1; n >= 0; n--)
                {
                    searched++;
                    if (G.deck[0][n] == treasure[0] || G.deck[0][n] == treasure[1] || G.deck[0][n] == treasure[2])
                    {
                        count ++;
                    }
                    if (count >= 2)
                    {
                        break;
                    }
                }
                expDeckSize = startDeckSize - searched;
                
                //Assert and output
                printf("INPUT searched = %d, treasure cards = %d, sequence = ", searched, count);
                for (int i = 0; i < G.deckCount[0]; i++){
                    printf("%d ", G.deck[0][i]);
                }
                printf("\n\n");
                
                cardEffect(k[0], 0, 1, 2, &G, G.handCount[0], &bonus);
                assertTrue(G.deckCount[0] == expDeckSize);
                printf("\tExpected DeckCount:\t%d, New DeckCount:\t%d\n\n", expDeckSize, G.deckCount[0]);
                
            }
        }
    } 
}


int main(){
    
    testAdventurer();
    return 0;
    
}
