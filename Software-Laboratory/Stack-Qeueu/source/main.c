#include <stdio.h>
#include <string.h>
#include<stdlib.h>

// A structure to represent a empty seats stack
struct Stack{ 
    char *flightCode, *seatsStack; 
    int *seatsNumber, *addSeats;
	int status, top;
	int businessCount, economyCount, standardCount; 
	char **business, **economy, **standard;
}; 
typedef struct Stack Stack;

// A structure to represent a passenger queue 
struct Queue { 
	int *front, *rear, *size;
	char **businessQueue, **economyQueue, **veteranQueue, **diplomatQueue, **standardQueue;
	char *flightCodeQueue;
	int *classNumber;
}; 
typedef struct Queue Queue;

// A structure to represent a passenger
struct Passenger {
	char *passengerName;  
	char *wantedSeat;
	char *soldSeat;
	char *flightCode;
};
typedef struct Passenger Passenger;

int charNum(char *fileName){
	FILE* fp ;
	char ch ;
	int noChar = 0;
	fp = fopen(fileName, "r");
    while((ch = fgetc(fp)) != EOF){
    	if(ch == '\n'){
		}
		else{
			noChar++;
		}
	}
	fclose(fp);
 	return noChar;
	//dosyadaki karakter sayisini hesapliyor
}

int noCommands(int charNum, char *fileName){
	char *str;
	str = (char*)malloc(charNum* (sizeof(char*)));
	int len, commandNum = 0;
    FILE *file;
    file = fopen(fileName,"r");
 	while(!feof(file)) {
        fgets(str,charNum,file);
        len = strlen(str);
        if(len > 0 && str[0] != '\0'){
           commandNum ++;
		}
    }
    	free(str);
	fclose(file);
    return commandNum;
    //dosyadaki komut sayisini hesapliyor
}

void error(){
	printf("error\n");
}

int numberControl(char number[]){
	int k = 5;
	if(isalpha(number[0])){
		k = 0;
	}
	else if(isdigit(number[0]) && atoi(number) > 0){
		k = 1;
	}
	return k;
 //gonderilen parametrenin sayi olmasini kontrol ediyor
}

int flightControl(int *noFlight, char name[], Stack** flight){
	int i, j= *noFlight;
	for(i = 0; i < *noFlight; i++){
		if(!strcmp((*flight)[i].flightCode, name)){
			j--;
		}
	}
	if(j == *noFlight){
		return 1;
	}
	else{
		return 0;
	}
	//daha önce oluþturulmamýþsa 1 dönüyor.
}

int closeControl(int *noFlight, char name[], Stack** flight){
	int i, j = 0;
	for(i = 0; i < *noFlight; i++){
		if(!strcmp((*flight)[i].flightCode, name)){
			j = (*flight)[i].status;
			return j;
		}
	}
	return j;
	//statusu döndürüyor.
}

int passengerControl(int *noPassenger, char name[], Passenger** passengers){
	int i, j= *noPassenger;
	if(noPassenger >0){
		for(i = 0; i < *noPassenger; i++){
		if(!strcmp((*passengers)[i].passengerName, name)){
			j--;
		}
	}
	}
	if(j == *noPassenger){
		return 1;
	}
	else{
		return 0;
	}
	//daha önce oluþturulmamýþsa 1 dönüyor.
}

int findFlight(int *noFlight, char name[], Stack** flight){
	int i, j = 15;
	for(i = 0; i < *noFlight; i++){
		if(!strcmp((*flight)[i].flightCode, name)){
			j = i;
		}
	}
	return j;
//ayni isimdeki flighti bulup indexini return ediyor
}

int findQueue(int *noQueue, char name[], Queue** classQueues){
	int i, j= 15;
	for(i = 0; i < *noQueue; i++){
		if(!strcmp((*classQueues)[i].flightCodeQueue, name)){
			j = i;
		}
	}
	return j;
//ayni isimdeki classQueue yi bulup indexini return ediyor
}

int classNoFunc(char classname[]){
	int i = 15;
	if(! strcmp(classname, "business")){
		i = 0;
	} else if(! strcmp(classname, "economy")){
		i = 1;
	}else if(! strcmp(classname, "standard")){
		i = 2;
	}
	return i;
//classNo yu return ediyor
}

void createFlight(int *noFlight, char name[], Stack** flight, int commandNum){
	int j;
	(*flight)[*noFlight].top = -1; 
	(*flight)[*noFlight].seatsNumber = (int*)malloc(3*sizeof(int));
	(*flight)[*noFlight].addSeats = (int*)malloc(3*sizeof(int));
	(*flight)[*noFlight].seatsNumber[0] = (*flight)[*noFlight].seatsNumber[1] = (*flight)[*noFlight].seatsNumber[2] = 0;
	(*flight)[*noFlight].addSeats[0] = (*flight)[*noFlight].addSeats[1] = (*flight)[*noFlight].addSeats[2] = 0;
	(*flight)[*noFlight].status = 1;
	(*flight)[*noFlight].businessCount = (*flight)[*noFlight].economyCount = (*flight)[*noFlight].standardCount = 0;
	(*flight)[*noFlight].flightCode = (char*)malloc(strlen(name)*sizeof(char));
	strncpy((*flight)[*noFlight].flightCode, name, strlen(name));
	(*flight)[*noFlight].business = (char **)malloc(1* sizeof(char*));
	(*flight)[*noFlight].economy = (char **)malloc(1* sizeof(char*));
	(*flight)[*noFlight].standard = (char **)malloc(1* sizeof(char*));
	*noFlight = *noFlight +1;
}

void push(Stack* flight, char item){
	if((*flight).top == -1){
		(*flight).seatsStack = (char*)malloc((1)*sizeof(char));
	}
	else{
		(*flight).seatsStack = (char*)realloc((*flight).seatsStack, ((*flight).top + 2)*sizeof(char));
	}
    (*flight).seatsStack[++(*flight).top] = item; 
} 

char pop(Stack flight, int classNo){
	(flight).seatsNumber[classNo] = (flight).seatsNumber[classNo] -1;
	return (flight).seatsStack[(flight).top --];
}

void enqueue(Queue* classQueues, int classNo, char* item){
	int i;
	if(classNo == 0){
		if((*classQueues).rear[classNo] == -1){
			(*classQueues).businessQueue = (char**)malloc((1)* sizeof(char*));
		}
		else if((*classQueues).rear[classNo] != -1){
			(*classQueues).businessQueue = (char**)realloc((*classQueues).businessQueue, ((*classQueues).rear[classNo] +2)*sizeof(char*));
		}
		(*classQueues).businessQueue[(*classQueues).rear[classNo] +1] = (char*)malloc(strlen(item)*sizeof(char));
		strcpy((*classQueues).businessQueue[++(*classQueues).rear[classNo]], item);
	}
	else if(classNo == 1){
		if((*classQueues).rear[classNo] == -1){
			(*classQueues).economyQueue = (char**)malloc((1)* sizeof(char*));
		}
		else if((*classQueues).rear[classNo] != -1){
			(*classQueues).economyQueue = (char**)realloc((*classQueues).economyQueue, ((*classQueues).rear[classNo] +2)*sizeof(char*));
		}
		(*classQueues).economyQueue[(*classQueues).rear[classNo] +1] = (char*)malloc(strlen(item)*sizeof(char));
		strcpy((*classQueues).economyQueue[++(*classQueues).rear[classNo]], item);
	}
	else if(classNo == 2){
		if((*classQueues).rear[classNo] == -1){
			(*classQueues).standardQueue = (char**)malloc((1)* sizeof(char*));
		}
		else if((*classQueues).rear[classNo] != -1){
			(*classQueues).standardQueue = (char**)realloc((*classQueues).standardQueue, ((*classQueues).rear[classNo] +2)*sizeof(char*));	
		}
		(*classQueues).standardQueue[(*classQueues).rear[classNo] +1] = (char*)malloc(strlen(item)*sizeof(char));
		strcpy((*classQueues).standardQueue[++(*classQueues).rear[classNo]], item);
	}
	else if(classNo == 3){
		if((*classQueues).rear[classNo] == -1){
			(*classQueues).diplomatQueue = (char**)malloc((1)* sizeof(char*));
		}
		else if((*classQueues).rear[classNo] != -1){
			(*classQueues).diplomatQueue = (char**)realloc((*classQueues).diplomatQueue, ((*classQueues).rear[classNo] +2)*sizeof(char*));
		}
		(*classQueues).diplomatQueue[(*classQueues).rear[classNo] +1] = (char*)malloc(strlen(item)*sizeof(char));
		strcpy((*classQueues).diplomatQueue[++(*classQueues).rear[classNo]], item);
	}
	else if(classNo == 4){
		if((*classQueues).rear[classNo] == -1){
			(*classQueues).veteranQueue = (char**)malloc((1)* sizeof(char*));
		}
		else if((*classQueues).rear[classNo] != -1){
			(*classQueues).veteranQueue = (char**)realloc((*classQueues).veteranQueue, ((*classQueues).rear[classNo] +2)*sizeof(char*));
		}
		(*classQueues).veteranQueue[(*classQueues).rear[classNo] +1] = (char*)malloc(strlen(item)*sizeof(char));
		strcpy((*classQueues).veteranQueue[++(*classQueues).rear[classNo]], item);
	}
	(*classQueues).size[classNo]= (*classQueues).size[classNo] +1;
}

char* dequeue(Queue classQueues, int classNo){
	char *item;
	if(classNo == 0){
		item = (char*)malloc(strlen((classQueues).businessQueue[(classQueues).front[classNo]])*sizeof(char));
	    strcpy(item, (classQueues).businessQueue[(classQueues).front[classNo]]);
	}
	else if(classNo == 1){
		item = (char*)malloc(strlen((classQueues).economyQueue[(classQueues).front[classNo]])*sizeof(char));
	    strcpy(item, (classQueues).economyQueue[(classQueues).front[classNo]]);
	}
	else if(classNo == 2){
		item = (char*)malloc(strlen((classQueues).standardQueue[(classQueues).front[classNo]])*sizeof(char));
	    strcpy(item, (classQueues).standardQueue[(classQueues).front[classNo]]);
	}
	else if(classNo == 3){
		item = (char*)malloc(strlen((classQueues).diplomatQueue[(classQueues).front[classNo]])*sizeof(char));
	    strcpy(item, (classQueues).diplomatQueue[(classQueues).front[classNo]]);
	}
	else if(classNo == 4){
		item = (char*)malloc(strlen((classQueues).veteranQueue[(classQueues).front[classNo]])*sizeof(char));
	    strcpy(item, (classQueues).veteranQueue[(classQueues).front[classNo]]);
	}
	(classQueues).front[classNo] = (classQueues).front[classNo] +1;
	(classQueues).size[classNo] = (classQueues).size[classNo] - 1;
    (classQueues).classNumber[classNo] =  (classQueues).classNumber[classNo]  -1;
	return item;
}

void createQueue(int *noQueue, char name[], Queue** classQueues){
	(*classQueues)[*noQueue].rear = (int*)malloc(5*sizeof(int));
	(*classQueues)[*noQueue].front = (int*)malloc(5*sizeof(int));
	(*classQueues)[*noQueue].size = (int*)malloc(5*sizeof(int));
	(*classQueues)[*noQueue].rear[0] = (*classQueues)[*noQueue].rear[1] = (*classQueues)[*noQueue].rear[2] = (*classQueues)[*noQueue].rear[3] = (*classQueues)[*noQueue].rear[4] = -1;
	(*classQueues)[*noQueue].front[0] = (*classQueues)[*noQueue].front[1] = (*classQueues)[*noQueue].front[2] = (*classQueues)[*noQueue].front[3] = (*classQueues)[*noQueue].front[4] = 0;
	(*classQueues)[*noQueue].size[0] = (*classQueues)[*noQueue].size[1] = (*classQueues)[*noQueue].size[2] = (*classQueues)[*noQueue].size[3] = (*classQueues)[*noQueue].size[4] = 0;
	(*classQueues)[*noQueue].flightCodeQueue = (char*)malloc(strlen(name)*sizeof(char));
	strncpy((*classQueues)[*noQueue].flightCodeQueue, name, strlen(name));
	(*classQueues)[*noQueue].classNumber = (int*)malloc(5*sizeof(int));
	(*classQueues)[*noQueue].classNumber[0] = (*classQueues)[*noQueue].classNumber[1] = (*classQueues)[*noQueue].classNumber[2] = (*classQueues)[*noQueue].classNumber[3] = (*classQueues)[*noQueue].classNumber[4] = 0 ;
	*noQueue = *noQueue + 1;
}

void createPassenger(int *noPassenger, char name[], char flightName[], char wanted[], Passenger** passengers){
	(*passengers)[*noPassenger].passengerName = (char*)malloc(strlen(name)*sizeof(char));
	strcpy((*passengers)[*noPassenger].passengerName, name);
	(*passengers)[*noPassenger].flightCode = (char*)malloc(strlen(flightName)*sizeof(char));
	strcpy((*passengers)[*noPassenger].flightCode, flightName);
	(*passengers)[*noPassenger].wantedSeat = (char*)malloc(strlen(wanted)*sizeof(char));
	strcpy((*passengers)[*noPassenger].wantedSeat, wanted);
	(*passengers)[*noPassenger].soldSeat = (char*)malloc(strlen("business")*sizeof(char));
	strcpy((*passengers)[*noPassenger].soldSeat, "none");
	*noPassenger = *noPassenger + 1;
}

void soldSeat(int *noPassenger, char name[], int sold, Passenger** passengers){
	int i;
	for(i = 0; i < *noPassenger; i++){
		if(!strcmp((*passengers)[i].passengerName, name)){
			if(sold == 0){
				strcpy((*passengers)[i].soldSeat, "business");	
			}
			if(sold == 1){
				strcpy((*passengers)[i].soldSeat, "economy");	
			}
			if(sold == 2){
				strcpy((*passengers)[i].soldSeat, "standard");	
			}
		}
	}
}

int queueControl(int *noQueue, char name[], Queue** classQueues){
	int i, j= *noQueue;
	for(i = 0; i < *noQueue; i++){
		if(!strcmp((*classQueues)[i].flightCodeQueue, name)){
			j--;
		}
	}
	if(j == *noQueue){
		return 1;
	}
	else{
		return 0;
	}
	//daha önce oluþturulmamýþsa 1 dönüyor.
}

void addseat(int *noFlight, char name[], char flightClass[], int count, Stack** flight){
	int i , j;
	for(i = 0; i < *noFlight; i++){
		if(!strcmp((*flight)[i].flightCode, name)){
			char classType;
			if(!strcmp(flightClass,"business")){
				classType = 'b';
				for(j = 0; j < count; j++){
					push(&(*flight)[i], classType);
				}
				(*flight)[i].seatsNumber[0] = (*flight)[i].seatsNumber[0] + count;
				(*flight)[i].addSeats[0] = (*flight)[i].addSeats[0] + count;
			}
			else if(! strcmp(flightClass, "economy")){
				classType = 'e';
				for(j = 0; j < count; j++){
					push(&(*flight)[i], classType);
				}
				(*flight)[i].seatsNumber[1] = (*flight)[i].seatsNumber[1] + count;
			    (*flight)[i].addSeats[1] = (*flight)[i].addSeats[1] + count;
			}
			else if(! strcmp(flightClass, "standard")){
				classType = 's';
				for(j = 0; j < count; j++){
					push(&(*flight)[i], classType);
				}
				(*flight)[i].seatsNumber[2] = (*flight)[i].seatsNumber[2] + count;
				(*flight)[i].addSeats[2] = (*flight)[i].addSeats[2] + count;
			}
			printf("addseats %s %d %d %d\n", (*flight)[i].flightCode, (*flight)[i].addSeats[0], (*flight)[i].addSeats[1] ,(*flight)[i].addSeats[2]);
		}
	}
}

void enqueueFun(int *noQueue, char flightName[], char className[], char passenger_name[], Queue** classQueues, int commandNum, int classNo){
	int i , j;
	for(i = 0; i < *noQueue; i++){
		if(!strcmp((*classQueues)[i].flightCodeQueue, flightName)){
			(*classQueues)[i].classNumber[classNo] = (*classQueues)[i].classNumber[classNo] + 1;
			enqueue(&(*classQueues)[i], classNo, passenger_name);
			if(!strcmp(className,"business")){
				printf("queue %s %s %s %d\n", (*classQueues)[i].flightCodeQueue, passenger_name, className, (*classQueues)[i].classNumber[0] + (*classQueues)[i].classNumber[3]);
			}
			else if(!strcmp(className,"economy")){
				printf("queue %s %s %s %d\n", (*classQueues)[i].flightCodeQueue, passenger_name, className, (*classQueues)[i].classNumber[1] + (*classQueues)[i].classNumber[4]);				
			}
			else if (!strcmp(className,"standard")){
				printf("queue %s %s %s %d\n", (*classQueues)[i].flightCodeQueue, passenger_name, className, (*classQueues)[i].classNumber[2]);
			}
		}
	}
}

void sellTickets(Stack* flight, Queue* classQueues, int classNo, int priClassNo, int *noPassenger, Passenger passengers[]){
	int k, seatNumber = (*flight).seatsNumber[classNo];
	for(k = 0; k < seatNumber ; k++){
		if((*flight).seatsNumber[classNo] > 0 && (*classQueues).size[priClassNo] >0){
			pop(*flight, classNo);
			char *item = dequeue(*classQueues, priClassNo);
			if(classNo == 0){
				if((*flight).businessCount != 0){
					(*flight).business = (char**)realloc((*flight).business, ((*flight).businessCount +1)* sizeof(char*));
				}
				(*flight).business[(*flight).businessCount] = (char*)malloc(strlen(item)*sizeof(char));
				strcpy((*flight).business[(*flight).businessCount], item);
				(*flight).businessCount = (*flight).businessCount + 1 ;
			}
			else if(classNo == 1){
				if((*flight).economyCount != 0){
					(*flight).economy = (char**)realloc((*flight).economy, ((*flight).economyCount +1)* sizeof(char*));
				}
				(*flight).economy[(*flight).economyCount] = (char*)malloc(strlen(item)*sizeof(char));
				strcpy((*flight).economy[(*flight).economyCount], item);
				(*flight).economyCount = (*flight).economyCount + 1 ;
			}
			else if(classNo == 2){
				if((*flight).economyCount != 0){
					(*flight).standard = (char**)realloc((*flight).standard, ((*flight).standardCount +1)* sizeof(char*));
				}
				(*flight).standard[(*flight).standardCount] = (char*)malloc(strlen(item)*sizeof(char));
				strcpy((*flight).standard[(*flight).standardCount], item);
				(*flight).standardCount = (*flight).standardCount + 1 ;
			}
		soldSeat(noPassenger, item, classNo, &passengers);
		free(item);
		}
	}
}

void sell(int *noFlight, char flightName[], Stack** flight, int *noQueue, Queue** classQueues, int i, int j, int *noPassenger, Passenger passengers[]){
	if(j + 1 > *noQueue ){
		error();
	}
	else if(((*flight)[i].seatsNumber[0] == 0 && (*flight)[i].seatsNumber[1] == 0 && (*flight)[i].seatsNumber[2] == 0) || ((*classQueues)[j].size[0] == 0 && (*classQueues)[j].size[1] == 0 && (*classQueues)[j].size[2] == 0 && (*classQueues)[j].size[3] == 0 && (*classQueues)[j].size[4] == 0)){
		error();
	}
	else{
		if((*flight)[i].seatsNumber[0] > 0 && (*classQueues)[j].size[3] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 0, 3, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[0] > 0 && (*classQueues)[j].size[0] >0){
	 		sellTickets( &(*flight)[i], &(*classQueues)[j], 0, 0, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[1] > 0 && (*classQueues)[j].size[4] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 1, 4, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[1] > 0 && (*classQueues)[j].size[1] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 1, 1, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[2] > 0 && (*classQueues)[j].size[2] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 2, 2, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[2] > 0 && (*classQueues)[j].size[3] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 2, 3, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[2] > 0 && (*classQueues)[j].size[0] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 2, 0, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[2] > 0 && (*classQueues)[j].size[4] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 2, 4, noPassenger, passengers);
		}
		if((*flight)[i].seatsNumber[2] > 0 && (*classQueues)[j].size[1] >0){
			sellTickets( &(*flight)[i], &(*classQueues)[j], 2, 1, noPassenger, passengers);
		}
		printf("sold %s %d %d %d\n", flightName, (*flight)[i].businessCount, (*flight)[i].economyCount, (*flight)[i].standardCount);
	}
}

void close(int *noFlight, char flightName[], Stack** flight, int i, Queue** classQueues, int j, int *noQueue ){
	if(j + 1 > *noQueue){
		error();
	}
	else{
	(*flight)[i].status = 0 ;
	int total_tickets = (*flight)[i].businessCount + (*flight)[i].economyCount + (*flight)[i].standardCount, waiting_count = 0, k , m ;
	int size, front;
	for(k = 0; k < 5; k++){
		waiting_count = waiting_count + (*classQueues)[j].size[k];
	}
	printf("closed %s %d %d\n", flightName, total_tickets, waiting_count);
	int classNo[5]={3, 0, 4, 1, 2};
	if(waiting_count > 0){
		for(k = 0 ; k < 5; k ++){
			size = (*classQueues)[j].size[classNo[k]];
			front = (*classQueues)[j].front[classNo[k]];
			if(size > 0){
				for(m = 0; m < size ; m++){
					if(classNo[k] == 3){
						printf("waiting %s\n", (*classQueues)[j].diplomatQueue[front]);
						front ++;
					}
					if(classNo[k] == 0){
						printf("waiting %s\n", (*classQueues)[j].businessQueue[front]);
						front ++;
					}if(classNo[k] == 4){
						printf("waiting %s\n", (*classQueues)[j].veteranQueue[front]);
						front ++;
					}if(classNo[k] == 1){
						printf("waiting %s\n", (*classQueues)[j].economyQueue[front]);
						front ++;
					}if(classNo[k] == 2){
						printf("waiting %s\n", (*classQueues)[j].standardQueue[front]);
						front ++;
					}	
				}
			}
		}
	}
	}
}

void report(int *noFlight, char flightName[], Stack** flight, int i){
	int j;
	printf("report %s\n", flightName);
	printf("business %d\n", (*flight)[i].businessCount);
	if((*flight)[i].businessCount > 0){
		for(j = 0; j < (*flight)[i].businessCount ; j ++){
			printf("%s\n", (*flight)[i].business[j]);
		}
	}
	printf("economy %d\n", (*flight)[i].economyCount);
	if((*flight)[i].economyCount > 0){
		for(j = 0; j < (*flight)[i].economyCount ; j ++){
			printf("%s\n", (*flight)[i].economy[j]);
		}
	}
	printf("standard %d\n", (*flight)[i].standardCount);
	if((*flight)[i].standardCount > 0){
		for(j = 0; j < (*flight)[i].standardCount ; j ++){
			printf("%s\n", (*flight)[i].standard[j]);
		}
	}
	printf("end of report %s\n", flightName);
}

void info(int *noPassenger, Passenger** passengers, char name[]){
	int i;
	for(i = 0; i < *noPassenger; i++){
		if(!strcmp((*passengers)[i].passengerName, name)){
			printf("info %s %s %s %s\n", (*passengers)[i].passengerName, (*passengers)[i].flightCode, (*passengers)[i].wantedSeat, (*passengers)[i].soldSeat);
		}
	}
}

void openInputFile(char *fileName, char *outputFile, int charNum, int *noFlight, Stack flight[], Queue classQueues[], int *noQueue, int commandNum, Passenger passengers[], int *noPassenger){
	char *str;
	str = (char*)malloc(charNum* (sizeof(char)));
	char **splitStrings;
	splitStrings = (char**)malloc(charNum*(sizeof(char*)));
	int j, len, wordNum, i , cnt, p, a;
	for(j = 0; j < charNum; j++){
		splitStrings[j]= (char*)malloc(charNum * sizeof(char));
	}
    FILE *file, *f;
    file = fopen(fileName,"r");
    f = freopen(outputFile, "w", stdout);
 	while(fgets(str,charNum,file)){
    len = strlen(str);
    if (len >= 0 && str[len-1] == '\n') {
        str[--len] = '\0';
    }
 	j=0; cnt=0;
    for( i=0; i<= (strlen(str)); i++){
      	if(str[i] == ' ' || str[i] == '\0'){
      		if(str[i+1] == ' '){    			
			}
			else{
				splitStrings[cnt][j]= '\0';
           		cnt++;
           		j=0;
			}
    	}
       	else{
           	splitStrings[cnt][j] = str[i];
           	j++;
       	}
   	}
   	wordNum=0;
    for( p = 0; p < strlen(str); p++){
		if ( str[p] == ' '){
			if(str[p + 1] != ' ' && str[p + 1] != '\0' && str[p + 1] != '\n'){ //burasý patlayabilir.
				wordNum++;
			}	
		}
	}
	// WORDNUM SATIRDAKÝ KELÝME SAYISINI TUTUYOR.
	// i kaç kelime olduðunu tutuyor.
	if( splitStrings[0][0] != '\0' && splitStrings[0][0] != ' '){
		char *word1;
		word1 = (char*)malloc((strlen(splitStrings[0]) +1)* (sizeof(char)));
		strncpy( word1, splitStrings[0], strlen(splitStrings[0]));
		word1[strlen(splitStrings[0])] = '\0';
		if(!strncmp(word1, "addseat", strlen("addseat"))){
			int classNo =  classNoFunc( splitStrings[2]);
			if(wordNum == 3 && numberControl(splitStrings[3]) == 1 && classNo <= 2){
				if (*noFlight == 0){
					createFlight(noFlight, splitStrings[1], &flight, commandNum);
				}
				else if(*noFlight != 0 && flightControl(noFlight, splitStrings[1], &flight) == 1){
					createFlight(noFlight, splitStrings[1], &flight, commandNum);
				}
				if(closeControl(noFlight, splitStrings[1], &flight) == 1){
					addseat(noFlight, splitStrings[1], splitStrings[2], atoi(splitStrings[3]), &flight);
				}
				else if(closeControl(noFlight, splitStrings[1], &flight) == 0){
					error();
				}
			}
			else{
				error();
			}
		}
		else if( ! strncmp(word1, "enqueue", strlen("enqueue"))){
			int classNo =  classNoFunc( splitStrings[2]);
			if(wordNum == 3 && flightControl(noFlight, splitStrings[1], &flight) == 0 && classNo <= 2 && passengerControl(noPassenger, splitStrings[3], &passengers) == 1 && closeControl(noFlight, splitStrings[1], &flight) == 1){
				createPassenger(noPassenger, splitStrings[3], splitStrings[1], splitStrings[2], &passengers);
				if(*noQueue == 0){
					createQueue(noQueue, splitStrings[1], &classQueues);
				}
				else if(*noQueue != 0 && queueControl(noQueue, splitStrings[1], &classQueues) == 1){
					createQueue(noQueue, splitStrings[1], &classQueues);
				}
				enqueueFun(noQueue, splitStrings[1], splitStrings[2], splitStrings[3], &classQueues, commandNum, classNo);
			}
			else if(wordNum == 4 && flightControl(noFlight, splitStrings[1], &flight) == 0 && classNo <= 2 && passengerControl(noPassenger, splitStrings[3], &passengers) == 1){
				if(((!strcmp(splitStrings[2], "business")) && (! strcmp(splitStrings[4], "diplomat"))) || ((!strcmp(splitStrings[2], "economy")) && (! strcmp(splitStrings[4], "veteran")))){
				    createPassenger(noPassenger, splitStrings[3],splitStrings[1], splitStrings[2], &passengers);				
					if(*noQueue == 0){
					createQueue(noQueue, splitStrings[1], &classQueues);
					}
					else if(*noQueue != 0 && queueControl(noQueue, splitStrings[1], &classQueues) == 1){
						createQueue(noQueue, splitStrings[1], &classQueues);
					}			
					if((!strcmp(splitStrings[2], "business")) && (! strcmp(splitStrings[4], "diplomat"))){
						classNo = 3;
						enqueueFun(noQueue, splitStrings[1], splitStrings[2], splitStrings[3], &classQueues, commandNum, classNo);
					} else if((!strcmp(splitStrings[2], "economy")) && (! strcmp(splitStrings[4], "veteran"))){
						classNo = 4;
						enqueueFun(noQueue, splitStrings[1], splitStrings[2], splitStrings[3], &classQueues, commandNum, classNo);
					}
				}
				else{
					error();
				}
			}
			else{
				error();
			}
			
		}
		else if(!strncmp(word1, "sell", strlen("sell"))){
			if(wordNum == 1 && flightControl(noFlight, splitStrings[1], &flight) == 0 && closeControl(noFlight, splitStrings[1], &flight) == 1){
				int i = findFlight(noFlight, splitStrings[1], &flight), j = findQueue(noQueue, splitStrings[1], &classQueues);
				sell(noFlight, splitStrings[1], &flight, noQueue, &classQueues, i, j, noPassenger, passengers);
			}else{
				error();
			}
		}
		else if( ! strncmp(word1, "report", strlen("report"))){
			if(wordNum == 1 && flightControl(noFlight, splitStrings[1], &flight) == 0){
				int i = findFlight(noFlight, splitStrings[1], &flight);
				report(noFlight, splitStrings[1], &flight, i);
			}
			else{
				error();
			}
		}
		else if( ! strncmp(word1, "close", strlen("close"))){
			if(wordNum == 1 && flightControl(noFlight, splitStrings[1], &flight) == 0){
				int i = findFlight(noFlight, splitStrings[1], &flight), j = findQueue(noQueue, splitStrings[1], &classQueues);
				close(noFlight, splitStrings[1], &flight, i, &classQueues, j, noQueue);
			}
			else{
				error();
			}
		}
		else if( ! strncmp(word1, "info", strlen("info"))){
			if(wordNum == 1 && passengerControl(noPassenger, splitStrings[1], &passengers) == 0){
				info(noPassenger, &passengers, splitStrings[1]);
			}
			else{
				error();
			}
		}
		else{
				error();
		}
	free(word1);
	}
	}
	free(str);
	for(i = 0; i < charNum; i++){
		free(splitStrings[i]);
	}
	free(splitStrings);
	fclose(file);
	fclose(f);
}

void freeStack(int *noFlight, Stack** flight, int size){
	int i, j;
	for( i = 0; i < *noFlight; i++){
		for(j = 0; j < (*flight)[i].businessCount; j++){
		    free((*flight)[i].business[j]);
		}
		for(j = 0; j < (*flight)[i].economyCount; j++){
		    free((*flight)[i].economy[j]);
		}
		for(j = 0; j < (*flight)[i].standardCount; j++){
		    free((*flight)[i].standard[j]);
		}
		free((*flight)[i].business);
		free((*flight)[i].economy);
		free((*flight)[i].standard);
		free((*flight)[i].flightCode);
		free((*flight)[i].seatsStack);
		free((*flight)[i].seatsNumber);
		free((*flight)[i].addSeats);
	}
}

void freeQueue(int *noQueue, Queue** classQueues, int size){
	int i, j;
	for( i = 0; i < *noQueue; i++){
		if((*classQueues)[i].rear[0] == -1){
			free((*classQueues)[i].businessQueue);
		}
		else{
			for(j = 0; j < (*classQueues)[i].rear[0] + 1; j++){
		    	free((*classQueues)[i].businessQueue[j]);
			}
			free((*classQueues)[i].businessQueue);
		}
		if((*classQueues)[i].rear[1] == -1){
			free((*classQueues)[i].economyQueue);
		}
		else{
			for(j = 0; j < (*classQueues)[i].rear[1] + 1; j++){
		    	free((*classQueues)[i].economyQueue[j]);
			}
			free((*classQueues)[i].economyQueue);
		}
		if((*classQueues)[i].rear[2] == -1){
			free((*classQueues)[i].standardQueue);
		}
		else{
			for(j = 0; j < (*classQueues)[i].rear[2] + 1; j++){
		    	free((*classQueues)[i].standardQueue[j]);
			}
			free((*classQueues)[i].standardQueue);
		}
		if((*classQueues)[i].rear[3] == -1){
			free((*classQueues)[i].diplomatQueue);
		}
		else{
			for(j = 0; j < (*classQueues)[i].rear[3] + 1; j++){
		   		free((*classQueues)[i].diplomatQueue[j]);
			}
			free((*classQueues)[i].diplomatQueue);
		}
		if((*classQueues)[i].rear[4] == -1){
			free((*classQueues)[i].veteranQueue);
		}
		else{
			for(j = 0; j < (*classQueues)[i].rear[4] + 1; j++){
		    	free((*classQueues)[i].veteranQueue[j]);
			}
			free((*classQueues)[i].veteranQueue);
		}
		free((*classQueues)[i].classNumber);
		free((*classQueues)[i].flightCodeQueue);
		free((*classQueues)[i].size);
		free((*classQueues)[i].rear);
		free((*classQueues)[i].front);
	}
}

void freePassenger(int *noPassenger, Passenger** passengers, int size){
	int i;
	for( i = 0; i < *noPassenger; i++){
		free((*passengers)[i].passengerName);
		free((*passengers)[i].wantedSeat);
		free((*passengers)[i].soldSeat);
		free((*passengers)[i].flightCode);
	}
}
int main(int argc, char *argv[]){
	char *fileName = argv[1];
	char *output = argv[2];
	int noFlight = 0, noQueue = 0 , noPassenger= 0;
	int charNumber = charNum(fileName);
	int commandNum = noCommands(charNumber, fileName);
	Stack *flight = (Stack *) malloc(commandNum *sizeof(Stack));
	Queue *classQueues= (Queue *) malloc(commandNum *sizeof(Queue));
	Passenger *passengers = (Passenger *) malloc(commandNum *sizeof(Passenger));
	openInputFile(fileName, output, charNumber, &noFlight, flight, classQueues, &noQueue, commandNum, passengers, &noPassenger);
	freeStack(&noFlight, &flight, commandNum);
	freeQueue(&noQueue, &classQueues, commandNum);
	freePassenger(&noPassenger, &passengers, commandNum);
	free(flight);
	free(classQueues);
	free(passengers);
}

