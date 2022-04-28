#include<stdio.h> 
#include<stdlib.h> 
#include <string.h>
#include <math.h>

struct Node{ 
	int data; 
	struct Node* next; 
	struct Node* prev; 
};
typedef struct Node Node;

struct population{
	struct Node* chromosome;
	struct population* next;
	struct population* prev;
}; 
typedef struct population population;

int charNum(char name[]);
void selectionFile(int charNum, struct population** selection); //reads selection file
void createSelection(char satir[], struct population** population, char* a); //splits spaces and colon and creates selection linked list
void xoverFile(int charNum, struct population** xover); //reads xover file and creates xover linked list with indexes
void mutateFile(int charNum, struct population** mutate); //reads mutate file and creates mutate linked list with indexes
void populationFile(int charNum, struct population** population); //this function reads population file and creates a population in which genes and chromosomes are involved
void createChromosome(char satir[], struct population** population, char* a);
void appendGene(struct Node** head_ref, int new_data);
void appendChromosome(struct population** head_ref, struct Node* new_data);
void selectionFunc(struct population* population, struct population* selection, struct population* xover, struct population* mutate, int *noGeneration, int size); //chromosome matching by selection file
struct Node* GetNth(struct population* head, int index); //Finds the Nth chromosome
int GetGene(Node* head, int index); // //Finds the Nth gene
void xoverFunc(struct population* population, int sel1, int sel2, int xover1, int xover2); //generate  gene exchange between two chromosomes according to xover file
void changeGene(Node* current, int index, int newNumber);
void mutateFunc(struct population* population, int mutate,  int size);//replaces the index gene given by mutate file with a complement
void swap(struct population *a, struct population *b);
void bubbleSort(struct population *start, int prob_size);//sorting according to the fitness values of chromosomes.
int calculateFitness(struct Node* node, int prob_size); //calculates binary values of chromosomes and equals fitness values
void printGene(struct Node* node); //printing genes
void printChromosome(struct population* node, int *noGeneration, int prob_size); //printing generation
struct Node *copy(Node *list);
struct Node *bestFunc(Node *list, Node *pop, int prob_size); //Fitness finds the smallest chromosome and compares it to the already existing best chromosome.
void printBest(Node *list, int prob_size); //printing best chromosome

int main(int argc, char *argv[]){
	char *a = argv[1];
	char *b = argv[2];
	char *c = argv[3];
	int prob_size= atoi(a), pop_size  = atoi(b), max_gen = atoi(c);
	int charNumber = charNum("population"), noChromosome = 0, noGeneration = 0;
	struct population* population = NULL;
	struct population* selection = NULL;
	struct population* xover = NULL;
	struct population* mutate = NULL;
	struct Node* best = NULL;
	selectionFile(charNumber, &selection);
	xoverFile(charNumber, &xover);
	mutateFile(charNumber, &mutate);
	populationFile(charNumber, &population);
    bubbleSort(population, prob_size);
    best = copy(GetNth(population, 0));
	printChromosome(population, &noGeneration, prob_size);
	printBest(best, prob_size);
	int i;
	for(i = 0; i < max_gen; i ++){
		selectionFunc(population, selection, xover, mutate, &noGeneration,  pop_size);
		bubbleSort(population, prob_size);
		noGeneration = noGeneration + 1;
		printChromosome(population, &noGeneration, prob_size);
		best = bestFunc(best, GetNth(population, 0), prob_size);
		printBest(best, prob_size);
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int charNum(char name[]){
	FILE* fp ;
	char ch ;
	int noChar = 0;
	fp = fopen(name, "r");
    while((ch = fgetc(fp)) != EOF){
    	if(ch == '\n'){
		}
		else{
			noChar++;
		}
	}
	fclose(fp);
 	return noChar;
}

void appendGene(struct Node** head_ref, int new_data){ 
	struct Node* new_node = (struct Node*)malloc(sizeof(struct Node)); 
	struct Node* last = *head_ref;
	new_node->data = new_data; 
	new_node->next = NULL; 
	if (*head_ref == NULL) { 
		//new_node->prev = NULL; 
		*head_ref = new_node; 
		return; 
	} 
	while (last->next != NULL) 
		last = last->next; 
	last->next = new_node; 
	//new_node->prev = last; 
	return; 
}

int calculateFitness(struct Node* node, int prob_size){
	int total = 0, counter = prob_size - 1;
	struct Node* last; 
	while (node != NULL){
		total = total + pow(2, counter)*node->data; 
		last = node;
		counter = counter - 1;
		node = node->next; 
	}
	return total;
}

void appendChromosome(struct population** head_ref, struct Node* new_data){ 
	struct population* new_node = (struct population*)malloc(sizeof(struct population)); 
	struct population* last = *head_ref;
	new_node->chromosome = new_data;
	new_node->next = NULL; 
	if (*head_ref == NULL) { 
		//new_node->prev = NULL; 
		*head_ref = new_node; 
		return; 
	} 
	while (last->next != NULL) 
		last = last->next; 
		last->next = new_node; 
		//new_node->prev = last; 
		return; 
} 

void printGene(struct Node* node) {
	struct Node* last; 
	while (node != NULL) { 
		printf("%d", node->data); 
		last = node; 
		node = node->next;
		if(node != NULL){
			printf(":");
		}
	}
}

void printChromosome(struct population* node, int *noGeneration, int prob_size) {
	struct population* last;
	printf("GENERATION: %d\n", *noGeneration);
	while (node != NULL) {
		printGene(node->chromosome);
		int fitness = calculateFitness(node->chromosome, prob_size);
		printf(" -> %d\n", fitness);
		last = node; 
		node = node->next; 
	}
}

void swap(struct population *a, struct population *b){ 
    Node* temp = a->chromosome; 
    a->chromosome = b->chromosome; 
    b->chromosome = temp; 
} 

void bubbleSort(struct population *start, int prob_size) { 
    int swapped, i; 
    struct population *ptr1; 
    struct population *lptr = NULL; 
    if (start == NULL) 
        return; 
    do{ 
        swapped = 0; 
        ptr1 = start; 
        while (ptr1->next != lptr) { 
            if (calculateFitness(ptr1->chromosome, prob_size) > calculateFitness(ptr1->next->chromosome, prob_size)) {  
                swap(ptr1, ptr1->next); 
                swapped = 1; 
            } 
            ptr1 = ptr1->next; 
        } 
        lptr = ptr1; 
    } 
    while (swapped); 
} 
  
void createChromosome(char satir[], struct population** population, char* a){
    char s[2];
    strcpy(s, a);
    char *token;
    token = strtok(satir, s);
	struct Node* list = NULL;
   	while( token !=  NULL ){
    	int b = atoi(token);
    	appendGene(&list, b); 
    	token = strtok(NULL, s);
	}
	appendChromosome(population, list);
}

void createSelection(char satir[], struct population** population, char* a){
    char s[2];
    strcpy(s, a);
    char *token;
    token = strtok(satir, s);
	struct Node* list = NULL;
   	while( token !=  NULL ){
   		int first, second;
		sscanf( token, "%d:%d",&first, &second);
    	appendGene(&list, first);
    	appendGene(&list, second); 
    	token = strtok(NULL, s);
	}
	appendChromosome(population, list);
}

void populationFile(int charNum, struct population** population){
	char *str;
	int len;
	str = (char*)malloc(charNum* (sizeof(char)));
    FILE *file;
    file = fopen("population","r");
 	while(fgets(str,charNum,file)){
    	len = strlen(str);
    	if (len >= 0 && str[len-1] == '\n') {
    	    str[--len] = '\0';
    	}
    	createChromosome(str, population, ":");
	}
	fclose(file);
}

void selectionFile(int charNum, struct population** selection){
	char *str;
	int len;
	str = (char*)malloc(charNum* (sizeof(char)));
    FILE *file;
    file = fopen("selection","r");
 	while(fgets(str,charNum,file)){
    	len = strlen(str);
    	if (len >= 0 && str[len-1] == '\n') {
    	    str[--len] = '\0';
    	}
    	createSelection(str, selection, " ");
	}
	fclose(file);
}

void xoverFile(int charNum, struct population** xover){
	char *str;
	int len;
	str = (char*)malloc(charNum* (sizeof(char)));
    FILE *file;
    file = fopen("xover","r");
 	while(fgets(str,charNum,file)){
    	len = strlen(str);
    	if (len >= 0 && str[len-1] == '\n') {
    	    str[--len] = '\0';
    	}
    	createChromosome(str, xover, ":");
	}
	fclose(file);
}

void mutateFile(int charNum, struct population** mutate){
	char *str;
	int len;
	str = (char*)malloc(charNum* (sizeof(char)));
    FILE *file;
    file = fopen("mutate","r");
 	while(fgets(str,charNum,file)){
    	len = strlen(str);
    	if (len >= 0 && str[len-1] == '\n') {
    	    str[--len] = '\0';
    	}
    	createChromosome(str, mutate,":");
	}
	fclose(file);
}

struct Node* GetNth(struct population* head, int index){ 
    struct population* current = head; 
    int count = 0; 
    while (current != NULL){ 
        if (count == index) 
            return(current->chromosome); 
        count++; 
        current = current->next; 
    }
}

int GetGene(Node* head, int index) {    
    Node* current = head;  
    int count = 0;  
    while (current != NULL)  
    {  
        if (count == index)  
            return(current->data);  
        count++;  
        current = current->next;  
    } 
}

void changeGene(Node* current, int index, int newNumber) {    
    int count = 0;  
    while (current != NULL){  
        if (count == index){
        	current->data = newNumber ;
        	return;
		}
        count++;  
        current = current->next;  
    } 
} 

void xoverFunc(struct population* population, int sel1, int sel2, int xover1, int xover2){
	int i;
	for(i = xover1 ; i <= xover2; i ++){
		int first = GetGene(GetNth(population, sel1- 1), i - 1);
		int second = GetGene(GetNth(population, sel2 -1), i - 1);
		changeGene(GetNth(population, sel1 - 1), i - 1, second);
		changeGene(GetNth(population, sel2 - 1), i - 1, first);
	}
}

void mutateFunc(struct population* population, int mutate,  int size){
	int i , second;
	for(i = 0 ; i < size ; i ++){
		int first = GetGene(GetNth(population, i), mutate);
		if(first == 0){
			second = 1;
		}
		else if( first == 1){
			second = 0;
		}
		changeGene(GetNth(population, i), mutate, second);
	}
}

void selectionFunc(struct population* population, struct population* selection, struct population* xover, struct population* mutate, int *noGeneration, int size){
	int i;
	int xover1 = GetGene(GetNth(xover, *noGeneration), 0);
	int xover2 = GetGene(GetNth(xover, *noGeneration), 1);
	for(i = 0; i < size; i = i+2){
		int first = GetGene(GetNth(selection, *noGeneration), i);
		int second = GetGene(GetNth(selection, *noGeneration), i + 1);
		xoverFunc(population, first, second, xover1, xover2);
	}
	int mutateNum = GetGene(GetNth(mutate, *noGeneration), 0);
	mutateFunc(population, mutateNum - 1 , size);
}

struct Node *copy(Node *list) {
    Node *newList = NULL;
    Node **newIt = &newList;
    while(list!=NULL){
        *newIt = malloc(sizeof(Node));
        memcpy(*newIt,list,sizeof(Node));
        list = list->next;
        newIt = &((*newIt)->next);
    }
    return newList;
}

struct Node *bestFunc(Node *list, Node *pop, int prob_size){
	int fitness = calculateFitness(list, prob_size);
	int firstPop = calculateFitness(pop, prob_size);
	if(fitness > firstPop){
		return copy(pop);
	}
	return list;
}

void printBest(Node *list, int prob_size){
	printf("Best chromosome found so far: ");
	printGene(list);
	int fitness = calculateFitness(list, prob_size);
	printf(" -> %d\n", fitness);
}