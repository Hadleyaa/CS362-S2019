CFLAGS= -Wall -fpic -coverage -lm -std=c99

rngs.o: rngs.h rngs.c
	gcc -c rngs.c -g  $(CFLAGS)

dominion.o: dominion.h dominion.c rngs.o
	gcc -c dominion.c -g  $(CFLAGS)

playdom: dominion.o playdom.c
	gcc -o playdom playdom.c -g dominion.o rngs.o $(CFLAGS)
#To run playdom you need to entere: ./playdom <any integer number> like ./playdom 10*/
testDrawCard: testDrawCard.c dominion.o rngs.o
	gcc  -o testDrawCard -g  testDrawCard.c dominion.o rngs.o $(CFLAGS)


#########

badTestDrawCard: badTestDrawCard.c dominion.o rngs.o
	gcc -o badTestDrawCard -g  badTestDrawCard.c dominion.o rngs.o $(CFLAGS)

testBuyCard: testDrawCard.c dominion.o rngs.o
	gcc -o testDrawCard -g  testDrawCard.c dominion.o rngs.o $(CFLAGS)

<<<<<<< Updated upstream
=======
testSmithy: unittest1.c dominion.o rngs.o
	gcc -o testSmithy -g unittest1.c dominion.o rngs.o $(CFLAGS)

testAdventurer: unittest2.c dominion.o rngs.o
	gcc -o testAdventurer -g  unittest2.c dominion.o rngs.o $(CFLAGS)

testSeahag: unittest3.c dominion.o rngs.o
	gcc -o testSeahag -g  unittest3.c dominion.o rngs.o $(CFLAGS)

testCutpurse: unittest4.c dominion.o rngs.o
	gcc -o testCutpurse -g  unittest4.c dominion.o rngs.o $(CFLAGS)
	
cardSmithy: cardtest1.c dominion.o rngs.o
	gcc -o cardSmithy -g cardtest1.c dominion.o rngs.o $(CFLAGS)

cardVillage: cardtest2.c dominion.o rngs.o
	gcc -o cardVillage -g  cardtest2.c dominion.o rngs.o $(CFLAGS)

cardOutpost: cardtest3.c dominion.o rngs.o
	gcc -o cardOutpost -g  cardtest3.c dominion.o rngs.o $(CFLAGS)

cardAdventurer: cardtest4.c dominion.o rngs.o
	gcc -o cardAdventurer -g  cardtest4.c dominion.o rngs.o $(CFLAGS)

randomtestcard1: randomtestcard1.c dominion.o rngs.o
	gcc -o randomtestcard1 -g  randomtestcard1.c dominion.o rngs.o $(CFLAGS)

randomtestcard2: randomtestcard2.c dominion.o rngs.o
	gcc -o randomtestcard2 -g  randomtestcard2.c dominion.o rngs.o $(CFLAGS)

randomtestcard3: randomtestcard3.c dominion.o rngs.o
	gcc -o randomtestcard3 -g  randomtestcard3.c dominion.o rngs.o $(CFLAGS)

randomtestcard3b: randomtestcard3b.c dominion.o rngs.o
	gcc -o randomtestcard3b -g  randomtestcard3b.c dominion.o rngs.o $(CFLAGS)

>>>>>>> Stashed changes
testAll: dominion.o testSuite.c
	gcc -o testSuite testSuite.c -g  dominion.o rngs.o $(CFLAGS)

interface.o: interface.h interface.c
	gcc -c interface.c -g  $(CFLAGS)

<<<<<<< Updated upstream
=======
randomtestresultsb: randomtestcard1 randomtestcard2 randomtestcard3b

	./randomtestcard1 >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

	./randomtestcard2 >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

	./randomtestcard3b >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

randomtestresults: randomtestcard1 randomtestcard2 randomtestcard3

	./randomtestcard1 >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

	./randomtestcard2 >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

	./randomtestcard3 >> randomtestresults.out
	gcov -f -b dominion.c >> randomtestresults.out
	cat dominion.c.gcov >> randomtestresults.out

unittestresults: testSmithy testAdventurer testSeahag testCutpurse cardSmithy cardVillage cardOutpost cardAdventurer
    
	./testSmithy >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./testAdventurer >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./testSeahag >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./testCutpurse >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./cardSmithy >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./cardVillage >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out
    
	./cardOutpost >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out

	./cardAdventurer >> unittestresults.out
	gcov -f -b dominion.c >> unittestresults.out
	cat dominion.c.gcov >> unittestresults.out

>>>>>>> Stashed changes
runtests: testDrawCard 
	./testDrawCard &> unittestresult.out
	gcov dominion.c >> unittestresult.out
	cat dominion.c.gcov >> unittestresult.out


player: player.c interface.o
	gcc -o player player.c -g  dominion.o rngs.o interface.o $(CFLAGS)

all: playdom player 

clean:
<<<<<<< Updated upstream
	rm -f *.o playdom.exe playdom player player.exe  *.gcov *.gcda *.gcno *.so *.out testDrawCard testDrawCard.exe
=======
	rm -f *.o playdom.exe playdom player player.exe  *.gcov *.gcda *.gcno *.so *.out testSmithy testSmithy.exe testRemodel testRemodel.exe testSeahag testSeahad.exe testCutpurse testCutpurse.exe randomtestcard1 randomtestcard1.exe randomtestcard2 randomtestcard2.exe randomtestcard3 randomtestcard3.exe randomtestcard3b randomtestcard3b.exe
	rm -r *dSYM
>>>>>>> Stashed changes
