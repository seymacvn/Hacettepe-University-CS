#include<stdio.h> 
#include<stdlib.h> 
#include <time.h> 
#include <string.h>

/////////////////////////////////////////////////////////////////////////
////(1) <alg.> ::= if(<cond>) { }                                    ////
////(2) <cond> ::= (<cond><set-op><cond>) | (<expr><rel-op><expr>)   ////
////(3) <expr> ::= (<expr><op><expr>) | <pre-op>(<expr>) | <var> *   ////
/////////////////////////////////////////////////////////////////////////

struct threeChildren {
    char data[10];
    void *parent;
    void *first;
    void *second;
    void *third;
    int noChild;
};
typedef struct threeChildren threeChildren;
//represents the tree structure with 3 children

struct twoChildren {
    char data[10];
    void *parent;
    void *first;
    void *second;
    int noChild;
};
typedef struct twoChildren twoChildren;
//represents the tree structure with 2 children

struct oneChildren {
    char data[10];
    void *parent;
    void *first;
    int noChild;
};
typedef struct oneChildren oneChildren;
//represents the tree structure with 1 children

struct childless {
    char data[10];
    void *parent;
    int noChild;
};
typedef struct childless childless;
//represents the structure of a childless tree

void *createNode(void *, char[]); //creating tree in this function
void printTree(void *,char **,char **,char **,char **,char **, int ,int ,int ,int ,int ); //this function prints the generated tree
void printChildless(char *,char **,char **,char **,char **,char **, int ,int ,int ,int ,int); //printing childless data
int randoms(int, int, int); //returns random numbers for tree creation
int lineNum(char[]); //return the number of elements in the terminal files
char ** createArrays(char[]); //creates arrays from elements in terminal files
int childNo = 0;
int countChild[1000];
//countchild holds the number of children created each time the createNode function is called

int main(int argc, char *argv[]){
	char **op, ** pre_op, **rel_op, **set_op, **var;
	srand(time(0));
	int lineOp = lineNum("op");
	int linePre_op = lineNum("pre_op");
	int lineRel_op = lineNum("rel_op");
	int lineSet_op = lineNum("set_op");
	int lineVar = lineNum("var");
	op = createArrays("op");
	pre_op = createArrays("pre_op");
	rel_op = createArrays("rel_op");
	set_op = createArrays("set_op");
	var = createArrays("var");
	//all of the above functions allow me to access the data in the terminal files and create an array from the contents
	void *root;
	root = createNode(&root, "cond"); //Syncs root pointer to tree created in createNode
	struct threeChildren * a = root;
	printf("if ");
	printTree(root, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
	printf(" { }\n");
}

void * createNode(void *root, char name[]){
	///////////////////////////////////////////////////////////////////////////
	char alg[1][10] = {"cond"};
	char cond[6][10] = {"cond", "set-op", "cond", "expr", "rel-op", "expr"};
	char expr[6][10] = {"expr", "op", "expr", "pre-op", "expr", "var"};
	///////////////////////////////////////////////////////////////////////////
	
	if (strcmp(name,"cond") == 0){
		int x = randoms(0, 1, 1);
		threeChildren *temp = malloc(sizeof(threeChildren)); //Since cond is known to have 3 children in all cases, a "threeChildren" type temp is created.
		strcpy(temp->data, name);
      	temp->noChild = 3;
      	countChild[childNo ++] = 3;
      	temp->parent = root;
		if(x == 0){
			temp->first = createNode(temp, cond[0]); //If the random number is 0, the first child of temp will be the 0th element of the cond array.
			temp->second = createNode(temp, cond[1]); //If the random number is 0, the first child of temp will be the 1th element of the cond array.
			temp->third = createNode(temp, cond[2]); //If the random number is 0, the first child of temp will be the 2th element of the cond array.
		}
		else if (x == 1){
			int i = 3;
			temp->first = createNode(temp, cond[3]); //If the random number is 1, the first child of temp will be the 3th element of the cond array.
			temp->second = createNode(temp, cond[4]); //If the random number is 1, the first child of temp will be the 4th element of the cond array.
			temp->third = createNode(temp, cond[5]); //If the random number is 1, the first child of temp will be the 5th element of the cond array.
		}
		return temp;
	}
	else if (strcmp(name,"expr") == 0){
		int x = randoms(0, 2, 1);
		if (x == 0){
			int i = 0;
			threeChildren *temp = malloc(sizeof(threeChildren));
			strcpy(temp->data, name);
        	temp->noChild = 3; 
        	countChild[childNo ++] = 3;
	      	temp->parent = root;
			temp->first = createNode(temp, expr[0]); //If the random number is 0, the first child of temp will be the 0th element of the expr array.
			temp->second = createNode(temp, expr[1]); //If the random number is 0, the first child of temp will be the 1th element of the expr array.
			temp->third = createNode(temp, expr[2]); //If the random number is 0, the first child of temp will be the 2th element of the expr array.
			return temp;
		}
		else if (x == 1){
			int i = 3;
			twoChildren *temp = malloc(sizeof(twoChildren));
			strcpy(temp->data, name);
   			temp->noChild = 2;
   			countChild[childNo ++] = 2;
			temp->parent = root;
			temp->first = createNode(temp, expr[3]); //If the random number is 1, the first child of temp will be the 3th element of the expr array.
			temp->second = createNode(temp, expr[4]); //If the random number is 1, the first child of temp will be the 4th element of the expr array.
			return temp;
		}
		else if (x == 2){
			int i = 5;
			oneChildren *temp = malloc(sizeof(oneChildren));
			strcpy(temp->data, name);
			temp->noChild = 1;
			countChild[childNo ++] = 1;
			temp->parent = root;
			temp->first = createNode(temp, expr[5]); //If the random number is 2, the first child of temp will be the 5th element of the expr array.
			return temp;
		}
	}
	else if ((strcmp(name,"op") == 0) || (strcmp(name,"pre-op") == 0) || (strcmp(name,"rel-op") == 0) || (strcmp(name,"set-op") == 0) || (strcmp(name,"var") == 0)){
		//because the terminals are childless, the temp is "childless" type
		childless *temp = malloc(sizeof(childless));
		temp->noChild = 0;
		countChild[childNo ++] = 0;
		strcpy(temp->data, name);
		return temp;
	}
}

int i = -1;
void printTree(void *root, char **op,char ** pre_op,char **rel_op,char **set_op,char **var, int lineOp,int linePre_op,int lineRel_op,int lineSet_op,int lineVar){
	i ++;
	//recursively prints the tree, based on the number of children of the tree created
	if(countChild[i] == 3){
		printf("(");
		struct threeChildren *a = root;
		printTree(a->first, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printTree(a->second, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printTree(a->third, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printf(")");
	}
	else if(countChild[i] == 2){
		printf("(");
		struct twoChildren * a = root;
		printTree(a->first, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printTree(a->second, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printf(")");
	}
	else if(countChild[i] == 1){
		printf("(");
		struct oneChildren * a = root;
		printTree(a->first, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
		printf(")");
	}
	else if(countChild[i] == 0){
		struct childless * a = root;
		printChildless(a->data, op, pre_op, rel_op, set_op, var, lineOp, linePre_op, lineRel_op, lineSet_op, lineVar);
	}
}

void printChildless(char *FolderName, char **op,char ** pre_op,char **rel_op,char **set_op,char **var, int lineOp,int linePre_op,int lineRel_op,int lineSet_op,int lineVar){
	if(!strncmp(FolderName, "op", strlen("op"))){
		int x = randoms(0, lineOp-1, 1);
		printf("%s",op[x]);
	}
 	else if(!strncmp(FolderName, "pre-op", strlen("pre-op"))){
		int x = randoms(0, linePre_op-1, 1);		
		printf("%s",pre_op[x]);
	}
	else if(!strncmp(FolderName, "rel-op", strlen("rel-op"))){
		int x = randoms(0, lineRel_op-1, 1);
		printf("%s",rel_op[x]);
	}
	else if(!strncmp(FolderName, "set-op", strlen("set-op"))){
		int x = randoms(0, lineSet_op-1, 1);
		printf("%s",set_op[x]);
	}
	else if(!strncmp(FolderName, "var", strlen("var"))){
		int x = randoms(0, lineVar-1, 1);
		printf("%s",var[x]);
	}
}

int randoms(int lower, int upper, int count){ 
    int i;
    for (i = 0; i < count; i++) { 
        int num = (rand() % (upper - lower + 1)) + lower; 
        return num;
    } 
}

int lineNum(char name[]){
	FILE* fp ;
	char ch ;
	int noLine = 0;
	fp = fopen(name, "r");
    while((ch = fgetc(fp)) != EOF){
    	if(ch == '\n'){
    		noLine ++;
		}
	}
	fclose(fp);
 	return noLine;
}

char ** createArrays(char name[]){
	char *str;
	char **splitStrings;
	str = (char*)malloc(15* (sizeof(char)));
	splitStrings = (char**)malloc(15*(sizeof(char*)));
	int j, i, len, cnt;
	for(j = 0; j < 15; j++){
		splitStrings[j]= (char*)malloc(15 * sizeof(char));
	}
    FILE *file, *f;
    file = fopen(name,"r");
    j = 0;
 	while(fgets(str,15,file)){
    	len = strlen(str);
    	if (len >= 0 && str[len-1] == '\n') {
        	str[--len] = '\0';
    	}
       	i = 0;
    	for(i = 0; i < strlen(str); i++){
    		if(i == strlen(str) -1){
    			splitStrings[j][i] =  str[i];
    			splitStrings[j][i + 1] = '\0';
			}
			else{
				splitStrings[j][i] =  str[i];
			}
		}
 		j ++;
    }
    return splitStrings;
}
