/* -------------------------------------------------------------------------------------
-
-   Comamand: make run
-   To compile and run program
-
-   Command: make out
-   To compile and run program with output to a file called results.out
-
-   Command: make cov
-   To execute gcov measurement and ouput results to a file called coverage.out
-   Condition: must execute run or out before using cov.
-
------------------------------------------------------------------------------------- */ 

#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<time.h>

char inputChar()
{
    int offset = ' ';        //first printable ascii character
    int last = '~';          //last printable ascii character
    int range = last - offset + 1;

    return offset + (rand() % range);
}

char *inputString()
{
    int maxLength = 5;
    char str[maxLength + 1];
    char *s = str;
    
    // All lower case from e - t
    int offset = 'e';        //first lowercase letter in range
    int last = 't';          //last lowercase letter in range
    int range = last - offset + 1;
    
    for (int i = 0; i < maxLength; i++){
        s[i] = offset + (rand() % range);
    }
    s[maxLength] = '\0';
    return s;
}

void testme()
{
  int tcCount = 0;
  char *s;
  char c;
  int state = 0;
  while (1)
  {
    tcCount++;
    c = inputChar();
    //s = inputString(s);
    s = inputString();
    printf("Iteration %d: c = %c, s = %s, state = %d\n", tcCount, c, s, state);

    if (c == '[' && state == 0) state = 1;
    if (c == '(' && state == 1) state = 2;
    if (c == '{' && state == 2) state = 3;
    if (c == ' '&& state == 3) state = 4;
    if (c == 'a' && state == 4) state = 5;
    if (c == 'x' && state == 5) state = 6;
    if (c == '}' && state == 6) state = 7;
    if (c == ')' && state == 7) state = 8;
    if (c == ']' && state == 8) state = 9;
    if (s[0] == 'r' && s[1] == 'e'
       && s[2] == 's' && s[3] == 'e'
       && s[4] == 't' && s[5] == '\0'
       && state == 9)
    {
      printf("error ");
      exit(200);
    }
  }
}


int main(int argc, char *argv[])
{
    srand(time(NULL));
    testme();
    return 0;
}
