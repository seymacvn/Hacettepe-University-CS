#include <stdio.h>
#include <string.h>
#include<stdlib.h>
#include <dirent.h>
struct Arrays {
   int width;
   int length;
   int **arr;
   char *type;
   char *name;
};
typedef struct Arrays Arrays;

int noItem(char *folderName){
	DIR *dpdf;
	struct dirent *epdf;
	dpdf = opendir(folderName);
	int count =0;
	if (dpdf != NULL){
  	 while (epdf = readdir(dpdf)){
  	 	if (!strcmp (epdf->d_name, "."))
            continue;
        if (!strcmp (epdf->d_name, ".."))    
            continue;
    	count++;
   		}
	}
	closedir(dpdf);	
	return count;
	//calculates the total vec and matte matrices to be generated
}

int charNum(char *commandsFile){
	FILE* fp ;
	char ch ;
	int noChar = 0;
	fp = fopen(commandsFile, "r");
    while((ch = fgetc(fp)) != EOF){
       	if(ch == '\n'){
		}
		else{
			noChar++;
		}
	}
	fclose(fp);
 	return noChar;
 	//calculates the number of char in the commands file
}

int noCommands(int charNum, char *commandsFile ){
	char *str;
	str = (char*)malloc(charNum* (sizeof(char*)));
	int len, commandNum = 0;
    FILE *file ;
    file = fopen(commandsFile,"r");
 		while(!feof(file)) {
        fgets(str,charNum,file);
        len = strlen(str);
        if (len >= 0 && str[len-1] == '\n') {
            str[--len] = '\0';
        }
        if(str[0] != '\0'){
           commandNum ++;
		}
    }
    free(str);
	fclose(file);
    return commandNum;
    //calculates the number of commands in the commands file
}

int createFileArray(int a, int k, int **arr, char *folderName){
	int count=0 ;
	DIR *dpdf;
	char *path;
	struct dirent *epdf;
	dpdf = opendir(folderName);
	FILE *file;
	char ch, con= '\n', eb='.';
	if (dpdf != NULL){
  	 while (epdf = readdir(dpdf)){
  	 	if (!strcmp (epdf->d_name, "."))
            continue;
        if (!strcmp (epdf->d_name, ".."))    
            continue;

		int len = strlen(epdf->d_name), i;
    	char *name;
    	name = (char *) malloc((len)*(sizeof(char)));
		for(i = 0 ; i <= len ; i ++){
			name[i] = epdf->d_name[i];
		}
		char folder[strlen(folderName)];
		strcpy(folder, folderName);
		path= (char*)malloc((strlen(folder) + strlen(name)+ 1)*sizeof(char));
		strcpy(path, folder);
		strcat(path, "/");
		strcat(path, name);
    	file = fopen(path,"r");
    	free(name);
    	free(path);
    	int row=0, column=0, num, a;
       	while((ch = fgetc(file)) != EOF){
       		if(ch == '\n'){
				row++;
			}
			else if(row == 0 && ch == ' '){
				column++;
			}
			else if(ch == ' '){
			}
			else{
				if(ch == '-'){
					a=1;
					continue;
				}
				if(a ==1){
					num = atoi(&ch);
					num = -num;
					a=0;
				}
				else{
					num = atoi(&ch);
				}
			}
			eb=ch;
		   }
		   if(con != eb){
				row = row + 1;
		   }
		   column = column + 1;
		   arr[count][0] = row;
		   arr[count][1] = column;
		   count++;
		   //calculating row column numbers
		   fclose(file);
   		}
	}
	closedir(dpdf);
	return **arr;
	
}

void rowFunc(int countArray, int k, int **arr, Arrays matrixes[]){
	int i, arrNum = 0;
	if( arrNum < countArray){
	  for(i = 0 ; i < countArray ; i++ ){
	  	    //contains dimensions of matrices and vectors.
			matrixes[arrNum].length = arr[i][0];
			matrixes[arrNum].width = arr[i][1];
			arrNum ++;
		}
	}
}

void openFile(int a, int k, int **arr, Arrays matrixes[], char *folderName){
	DIR *dpdf;
	struct dirent *epdf;
	dpdf = opendir(folderName);
	FILE *file;
	char *path;
	int fileNumber = 0, i, len, type_len;
	if (dpdf != NULL){
  	 while (epdf = readdir(dpdf)){
  	 	if (!strcmp (epdf->d_name, "."))
            continue;
        if (!strcmp (epdf->d_name, ".."))    
            continue;
    
		int len1 = strlen(epdf->d_name), i;
    	char name1[len1 +1];
		for(i = 0 ; i <= len1 ; i ++){
			name1[i] = epdf->d_name[i];
			if(i == len1){
				name1[i] = '\0';
			}
		}
		char folder[strlen(folderName)];
		strcpy(folder, folderName);
		path= (char*)malloc((strlen(folder) + strlen(name1)+ 1)*sizeof(char));
		strcpy(path, folder);
		strcat(path, "/");
		strcat(path, name1);  
      	len = strlen(epdf->d_name) -4;
    	type_len = strlen(".mat");
    	char type[type_len], name[len];
		for(i = 0 ; i < type_len ; i ++){
			type[i] = epdf->d_name[strlen(epdf->d_name) - 4 + i];
		}
		type[type_len] = '\0';
		if(! strncmp(type, ".mat", strlen(type))){
			matrixes[fileNumber].type = (char *) malloc(strlen(type)*(sizeof(char)));
    		strcpy(matrixes[fileNumber].type , type);
		}
		else if(! strncmp(type, ".vec", strlen(type))){
			matrixes[fileNumber].type = (char *) malloc(strlen(type)*(sizeof(char)));
    		strcpy(matrixes[fileNumber].type , type);	
		}
		
		for(i = 0 ; i <len ; i ++){
			name[i] = epdf->d_name[i];
		}
		name[len] = '\0';
    	matrixes[fileNumber].name = (char *) malloc(strlen(name)*(sizeof(char)));
    	strcpy(matrixes[fileNumber].name, name);
    	file = fopen(path,"r");
    	free(path);
    	int j = 0, a, b;
		int row = matrixes[fileNumber].length;
		int column = matrixes[fileNumber].width ;
		int deneme[row][column];
		fscanf (file, "%d", &i);
  		while (!feof (file)){ 
		  a = j / matrixes[fileNumber].width;
	 	  b = j % matrixes[fileNumber].width;
	 	  deneme[a][b] = i;
    	  j ++;
    	  fscanf (file, "%d", &i);
   		 }
   		 if (j == matrixes[fileNumber].length * matrixes[fileNumber].width -1){
			deneme[matrixes[fileNumber].length- 1][matrixes[fileNumber].width -1] = i;
		 }
         matrixes[fileNumber].arr= (int**)malloc(matrixes[fileNumber].length* (sizeof(int*)));
         for(j = 0; j < matrixes[fileNumber].length; j++){
         	matrixes[fileNumber].arr[j]= (int*)malloc(matrixes[fileNumber].width * sizeof(int));
		 }
		 
		 for(a = 0; a < matrixes[fileNumber].length; a ++){
   	    	 for(b = 0; b < matrixes[fileNumber].width; b ++){
   	    	 	 matrixes[fileNumber].arr[a][b] = deneme[a][b];
			}
		 }  
  	  		fclose (file);
  	  		fileNumber ++;
		 }
  	}
 	closedir(dpdf);	
}

int matNum(Arrays matrixes[], int countArray){
	int i, no = 0;
	for(i = 0; i < countArray; i++){
		if(! strcmp(matrixes[i].type, ".mat")){
			no ++;
		}
	}
	return no;	
}

int vecNum(Arrays matrixes[], int countArray){
	int i, no = 0;
	for(i = 0; i < countArray; i++){
		if(! strcmp(matrixes[i].type, ".vec")){
			no ++;
		}
	}
	return no;	
}

void veczeros(Arrays matrixes[], int *countMatrixes, char **splitStrings, int *vec_num ){
	int length, i, j = 0, a, b, m ;
	int no = *countMatrixes;
	char *name;
	name = (char *) malloc((strlen(splitStrings[1]) +1)*(sizeof(char)));
	strncpy(name, splitStrings[1], strlen(splitStrings[1]));
	name[strlen(splitStrings[1])] = '\0';
	matrixes[no].name = (char *) malloc(strlen(name)*(sizeof(char)));
	strcpy(matrixes[no].name, name);
	matrixes[no].length = 1;
	matrixes[no].width = atoi(splitStrings[2]);
	matrixes[no].type = (char *) malloc(strlen(".vec")*(sizeof(char)));
    strncpy(matrixes[no].type , ".vec", strlen(".vec"));
   	matrixes[no].arr= (int**)malloc(matrixes[no].length* (sizeof(int*)));
    for(i = 0; i < matrixes[no].length; i++){
        matrixes[no].arr[i]= (int*)malloc(matrixes[no].width * sizeof(int));
	}
	for(a = 0; a < matrixes[no].length; a ++){
   	    for(b = 0; b < matrixes[no].width; b ++){
   	    	matrixes[no].arr[a][b] = 0;
	   }
	}
	printf("created vector %s %d\n", matrixes[no].name, matrixes[no].width );
	for(j = 0; j < matrixes[no].length; j++ ){
		for(m = 0; m < matrixes[no].width; m++ ){
			printf("%d ", matrixes[no].arr[j][m]);
		}
		printf("\n");
	}
    *countMatrixes = *countMatrixes +1;
    *vec_num = *vec_num +1;
    free(name);
}

void matzeros(Arrays matrixes[], int *countMatrixes, char **splitStrings, int *mat_num){
	int length, i, j = 0, a, b, m ;
	int no = *countMatrixes;
	char *name;
	name = (char *) malloc((strlen(splitStrings[1]) +1)*(sizeof(char)));
	strncpy(name, splitStrings[1], strlen(splitStrings[1]));
	name[strlen(splitStrings[1])] = '\0';
    matrixes[no].name = (char *) malloc(strlen(name)*(sizeof(char)));
	strcpy(matrixes[no].name, name);
	matrixes[no].length = atoi(splitStrings[2]);
	matrixes[no].width = atoi(splitStrings[3]);
	matrixes[no].type = (char *) malloc(strlen(".mat")*(sizeof(char)));
    strncpy(matrixes[no].type , ".mat", strlen(".mat"));
    matrixes[no].arr= (int**)malloc(matrixes[no].length* (sizeof(int*)));
    for(i = 0; i < matrixes[no].length; i++){
    	matrixes[no].arr[i]= (int*)malloc(matrixes[no].width * sizeof(int));
	}
	for(a = 0; a < matrixes[no].length; a ++){
   	    for(b = 0; b < matrixes[no].width; b ++){
   	        matrixes[no].arr[a][b] = 0;
		}
	}
	printf("created matrix %s %d %d\n", matrixes[no].name, matrixes[no].length, matrixes[no].width );
	for(j = 0; j < matrixes[no].length; j++ ){
		for(m = 0; m < matrixes[no].width; m++ ){
			printf("%d ", matrixes[no].arr[j][m]);
		}
		printf("\n");
	}
    *countMatrixes = *countMatrixes +1;
    *mat_num = *mat_num +1;
    free(name);
}

void vecread(Arrays matrixes[], char **splitStrings, int *vec_num, int *countMatrixes){
	int i, j= 0, m;
	int len = strlen(splitStrings[1]) - 4;
	char *name;
	name = (char *) malloc(len*(sizeof(char)));
	for(i = 0; i < len ; i++){
		name[i] = splitStrings[1][i];
	}
    name[len] = '\0';
	for( i = 0; i < *countMatrixes; i++){
		if((! strncmp(matrixes[i].type, ".vec", strlen(".vec"))) && ( !strcmp(name, matrixes[i].name))){
			printf("read vector %s %d\n", splitStrings[1], matrixes[i].width );
			for(j = 0; j < matrixes[i].length; j++ ){
				for(m = 0; m < matrixes[i].width; m++ ){
					printf("%d ", matrixes[i].arr[j][m]);
				}
				printf("\n");
			}
		}
	}
	free(name);
}

void matread(Arrays matrixes[], char **splitStrings, int *mat_num, int *countMatrixes){
	int i, j= 0, m;
	int len = strlen(splitStrings[1]) - 4;
	char *name;
	name = (char *) malloc(len*(sizeof(char)));
	for(i = 0; i < len ; i++){
		name[i] = splitStrings[1][i];
	}
    name[len] = '\0';
	for( i = 0; i < *countMatrixes; i++){
		if((! strncmp(matrixes[i].type, ".mat", strlen(".mat"))) && ( !strcmp(name, matrixes[i].name))){
			printf("read matrix %s %d %d\n", splitStrings[1], matrixes[i].length, matrixes[i].width );
			for(j = 0; j < matrixes[i].length; j++ ){
				for(m = 0; m < matrixes[i].width; m++ ){
					printf("%d ", matrixes[i].arr[j][m]);
				}
				printf("\n");
			}
		}
	}
	free(name);
}

void vecstack(Arrays matrixes[], char **splitStrings, int *vec_num, int *countMatrixes, int *mat_num, int width1, int width2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0, no1, no2, column1 =0, column2= 0, m;
	int *content1, *content2;
	char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
	for( i = 0; i < *countMatrixes; i++){
		if((! strncmp(matrixes[i].type, ".vec", strlen(".vec"))) && (!strncmp(name1, matrixes[i].name, strlen(name1)))){
			column1 = matrixes[i].width;
			content1 = (int*)malloc(column1* (sizeof(int*)));
			for(k = 0; k < column1; k++){
				content1[k] = matrixes[i].arr[0][k];
			}
		}
	}
	for( i = 0; i < *countMatrixes; i++){
		if((! strncmp(matrixes[i].type, ".vec", strlen(".vec"))) && (!strncmp(name2, matrixes[i].name, strlen(name2)))){
			column2 = matrixes[i].width;
			content2 = (int*)malloc(column2* (sizeof(int*)));
			for(k = 0; k < column2; k++){
				content2[k] = matrixes[i].arr[0][k];
			}
		}
	}
	if(column1 == column2){
		if(! strncmp(splitStrings[3], "row", strlen("row"))){
			int len = strlen(splitStrings[4]) +1;
			char name[len];
			for(i = 0; i < len ; i++){
				name[i] = splitStrings[4][i];
			}
  			name[len] = '\0';
			int no = *countMatrixes;
		    matrixes[no].name = (char *) malloc(strlen(name)*(sizeof(char)));
		    strcpy(matrixes[no].name, name);
		    matrixes[no].length = 2;
		    matrixes[no].width = column1;
		    matrixes[no].type = (char *) malloc(strlen(".mat")*(sizeof(char)));
    		strncpy(matrixes[no].type , ".mat", strlen(".mat"));
    		matrixes[no].arr= (int**)malloc(matrixes[no].length* (sizeof(int*)));
     		for(i = 0; i < matrixes[no].length; i++){
     		    matrixes[no].arr[i]= (int*)malloc(matrixes[no].width * sizeof(int));
			}
			for(a = 0; a < matrixes[no].length; a ++){
   	 		    for(b = 0; b < matrixes[no].width; b ++){
   	 			    if(a == 0){
   	 			        matrixes[no].arr[a][b] = content1[b];
					}
					else if(a == 1){
   	 			     	matrixes[no].arr[a][b] = content2[b];
					}	
			    }
			}
			printf("vectors concatenated %s %d %d\n", matrixes[no].name, matrixes[no].length, matrixes[no].width);
			for(j = 0; j < matrixes[no].length; j++ ){
				for(m = 0; m < matrixes[no].width; m++ ){
					printf("%d ", matrixes[no].arr[j][m]);
				}
			printf("\n");
			}
			*countMatrixes = *countMatrixes +1;
            *mat_num = *mat_num +1;
	   }
	   else if(! strncmp(splitStrings[3], "column", strlen("column"))){
			int len = strlen(splitStrings[4]) +1;
			char name[len];
			for(i = 0; i < len ; i++){
				name[i] = splitStrings[4][i];
			}
  			name[len] = '\0';
			int no = *countMatrixes;
		    matrixes[no].name = (char *) malloc(strlen(name)*(sizeof(char)));
			strcpy(matrixes[no].name, name);
		    matrixes[no].length = column1;
			matrixes[no].width = 2;
		    matrixes[no].type = (char *) malloc(strlen(".mat")*(sizeof(char)));
    	    strncpy(matrixes[no].type , ".mat", strlen(".mat"));
   		    matrixes[no].arr= (int**)malloc(matrixes[no].length* (sizeof(int*)));
    		for(i = 0; i < matrixes[no].length; i++){
     		    matrixes[no].arr[i]= (int*)malloc(matrixes[no].width * sizeof(int));
			}
			for(a = 0; a < matrixes[no].length; a ++){
   	 			for(b = 0; b < matrixes[no].width; b ++){
   	 			   	if(b == 0){
   	 			    	matrixes[no].arr[a][b] = content1[a];
					}
					else if(b == 1){
   	 			     	matrixes[no].arr[a][b] = content2[a];
					}	
			    }
			}
			printf("vectors concatenated %s %d %d\n", matrixes[no].name, matrixes[no].length, matrixes[no].width);
			for(j = 0; j < matrixes[no].length; j++ ){
				for(m = 0; m < matrixes[no].width; m++ ){
					printf("%d ", matrixes[no].arr[j][m]);
				}
				printf("\n");
			}
			*countMatrixes = *countMatrixes +1;
        	*mat_num = *mat_num +1;	
			}
			else{
				printf("error\n");
			}
	}
	free(content1);
	free(content2);
	free(name1);
	free(name2);
}

void matstack(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length1, int length2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0, length, width ;
	int **content1, **content2;
	char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name2, (*matrixes)[i].name, strlen(name2))){
			length =  (*matrixes)[i].length;
			width =  (*matrixes)[i].width;
			content2 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
			for(j = 0; j <  (*matrixes)[i].length; j++){
         		content2[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
			}
			for(a = 0; a <  (*matrixes)[i].length; a ++){
      			for(b = 0; b <  (*matrixes)[i].width; b ++){
  	    	 	 	content2[a][b] =  (*matrixes)[i].arr[a][b];
				}
			}
		}
	}
	if(!strncmp(splitStrings[3], "r", strlen("r"))){
		for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
				if(length != (*matrixes)[i].length){
					printf("error\n");
				}
				else if(length == (*matrixes)[i].length){
					content1 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
					for(j = 0; j < (*matrixes)[i].length; j++){
         				content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
					}
						for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    					for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    	 			 		content1[a][b] = (*matrixes)[i].arr[a][b];
							}
		 				}
		 				for(a = 0; a < (*matrixes)[i].length; a++){
		 						free((*matrixes)[i].arr[a]);
						}
						free((*matrixes)[i].arr);
						(*matrixes)[i].width = (*matrixes)[i].width + width;
						(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
						for(j = 0; j < (*matrixes)[i].length; j++){
         					(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
						}
						
						int cnt = 0;
						int n = (*matrixes)[i].width - width;
						for(a = 0; a < (*matrixes)[i].length; a ++){
   	    				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    				 		if(b < n){
									(*matrixes)[i].arr[a][b] = content1[a][b];
									cnt = 0;
								}
   	    				 	    else if(b >= n){
   	    				 	    	(*matrixes)[i].arr[a][b] = content2[a][cnt];
   	    				 	    	cnt++;
								}
							}
		 				}
		 				printf("matrices concatenated %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width);
		 				for(a = 0; a < (*matrixes)[i].length; a ++){
   	    				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    						printf("%d ",(*matrixes)[i].arr[a][b]);
							}
							printf("\n");
		 				}
					for(a = 0; a < length1; a++){
					free(content1[a]);
					}
					free(content1);	
					}
			  	}
			}
		}
		else if(!strncmp(splitStrings[3], "d", strlen("d"))){
			for( i = 0; i < *countMatrixes; i++){
				if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
					if(width != (*matrixes)[i].width){
						printf("error\n");
					}
					else if(width == (*matrixes)[i].width){
						content1 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
						for(j = 0; j < (*matrixes)[i].length; j++){
         					content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
						}
						for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    					for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    	 			 		content1[a][b] = (*matrixes)[i].arr[a][b];
							}
		 				}
		 				for(a = 0; a < (*matrixes)[i].length; a++){
		 						free((*matrixes)[i].arr[a]);
						}
						free((*matrixes)[i].arr);
						
						(*matrixes)[i].length = (*matrixes)[i].length + length;
						(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
						for(j = 0; j < (*matrixes)[i].length; j++){
         					(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
						}
						
						int n = (*matrixes)[i].length - length;
						for(a = 0; a < (*matrixes)[i].length; a ++){
   	    				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    				 		if(a < n){
									(*matrixes)[i].arr[a][b] = content1[a][b];
								}
   	    				 	    else if(a >= n){
   	    				 	    	(*matrixes)[i].arr[a][b] = content2[a-width][b];
								}
							}
		 				}
		 				printf("matrices concatenated %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width);
		 				for(a = 0; a < (*matrixes)[i].length; a ++){
   	    				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    						printf("%d ",(*matrixes)[i].arr[a][b]);
							}
							printf("\n");
		 				}
		 			for(a = 0; a < length1; a++){
					free(content1[a]);
					}
					free(content1);
					}
				}
			}
		}
		else{
			printf("error\n");
		}
	for(a = 0; a < length2; a++){
		free(content2[a]);
	}
	free(content2);
	free(name1);
	free(name2);
}

void mvstack(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int *vec_num,int length1, int length2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0 ;
	int **content1, **content2;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name2, (*matrixes)[i].name, strlen(name2))){
			length =  (*matrixes)[i].length;
			width =  (*matrixes)[i].width;
			content2 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
			for(j = 0; j <  (*matrixes)[i].length; j++){
         		content2[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
			}
			for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    		for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    	  		content2[a][b] =  (*matrixes)[i].arr[a][b];
					}
		 		}
			}
	}
	if(!strncmp(splitStrings[3], "d", strlen("d"))){
		for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
			    if(width != (*matrixes)[i].width){
					printf("error\n");
				}
				else if(width == (*matrixes)[i].width){
					content1 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
					for(j = 0; j < (*matrixes)[i].length; j++){
         				content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
					}
					for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   					for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   	 			 		content1[a][b] = (*matrixes)[i].arr[a][b];
						}
		 			}
		 			for(a = 0; a < (*matrixes)[i].length; a++){
		 				free((*matrixes)[i].arr[a]);
					}
					free((*matrixes)[i].arr);
										
					(*matrixes)[i].length = (*matrixes)[i].length + length;
					(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
					for(j = 0; j < (*matrixes)[i].length; j++){
         				(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
					}
					int n = (*matrixes)[i].length - length;
					int cnt= 0;
					for(a = 0; a < (*matrixes)[i].length; a ++){
					   	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    			 		if(a < n){
								(*matrixes)[i].arr[a][b] = content1[a][b];
								cnt = 0;
							}
   	   				 	    else if(a >= n){
   	   				 	    	(*matrixes)[i].arr[a][b] = content2[cnt][b];
							}
						}
		 			}
		 			printf("matrix and vector concatenated %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width);
		 			for(a = 0; a < (*matrixes)[i].length; a ++){
   	   				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	   						printf("%d ",(*matrixes)[i].arr[a][b]);
						}
						printf("\n");
		 			}
		 			for(a = 0; a < length1; a++){
						free(content1[a]);
					}
					free(content1);	
				}
			}
		}
	}
	else if(!strncmp(splitStrings[3], "r", strlen("r"))){
		for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
			  	if(width != (*matrixes)[i].length){		
					printf("error\n");
				}
				else if(width == (*matrixes)[i].length){
					content1 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
					for(j = 0; j < (*matrixes)[i].length; j++){
         				content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
					}
					for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    				for(b = 0; b <  (*matrixes)[i].width; b ++){
   	     			 		content1[a][b] = (*matrixes)[i].arr[a][b];
						}
					}
	 				for(a = 0; a < (*matrixes)[i].length; a++){
						free((*matrixes)[i].arr[a]);
					}
					free((*matrixes)[i].arr);
						
					(*matrixes)[i].width = (*matrixes)[i].width + length;
					(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
					for(j = 0; j < (*matrixes)[i].length; j++){
       					(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
					}
					int cnt = 0 ,cnt1= 0;
					int n = (*matrixes)[i].width - length;
					for(a = 0; a < (*matrixes)[i].length; a ++){
   	    				 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    				 		if(b < n){
									(*matrixes)[i].arr[a][b] = content1[a][b];
								}
   	    				 	    else if(b >= n){
   	    				 	    	(*matrixes)[i].arr[a][b] = content2[cnt][cnt1];
   	    				 	    	cnt1++;
								}
							}
		 			}
		 			printf("matrix and vector concatenated %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width);
		 			for(a = 0; a < (*matrixes)[i].length; a ++){
   	    			 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    					printf("%d ",(*matrixes)[i].arr[a][b]);
						}
						printf("\n");
		 			}
		 			for(a = 0; a < length1; a++){
						free(content1[a]);
					}
					free(content1);
					}
			   	}
			}
		}
		else{
			printf("error\n");
		}
		for(a = 0; a < length2; a++){
		free(content2[a]);
		}
		free(content2);
		free(name1);
		free(name2);
}

void pad(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length1){
	int a = strlen(splitStrings[1]), i, j= 0, k= 0, b, r, c ;
	int *min, *max, *min1, *max1;
	int **content1;
	char *name1;
	name1 = (char *) malloc(a*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
    name1[a] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
	if(!strncmp(name1, (*matrixes)[i].name, strlen(name1)) && !strncmp((*matrixes)[i].type, ".mat", strlen("mat"))){
		length =  (*matrixes)[i].length;
		width =  (*matrixes)[i].width;
		min = (int*)malloc(length*(sizeof(int*)));
		max = (int*)malloc(length*(sizeof(int*)));
		min1 = (int*)malloc(width*(sizeof(int*)));
		max1 = (int*)malloc(width*(sizeof(int*)));
		content1 = (int**)malloc((*matrixes)[i].length*(sizeof(int*)));
		for(j = 0; j <  (*matrixes)[i].length; j++){
         	content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
		}
		for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    	for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    	 	content1[a][b] =  (*matrixes)[i].arr[a][b];
			}
    	}
		int minn= 0, maxx= 0;
    	for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    	for(b = 0; b <  (*matrixes)[i].width; b ++){
				if(b == 0){
					minn = content1[a][b];
					maxx = content1[a][b];
				}
				else{
					if(minn > content1[a][b]){
						minn = content1[a][b];
					}
					if(maxx < content1[a][b]){
						maxx = content1[a][b];
					}	
				}
			}
			min[a] = minn;
			max[a] = maxx;
		    }
		    int minn1= 0, maxx1= 0;
		    for(b = 0; b <  (*matrixes)[i].width; b ++){
		    for(a = 0; a <  (*matrixes)[i].length; a ++){
				if(a == 0){
					minn1 = content1[a][b];
					maxx1 = content1[a][b];
				}
				else{
					if(minn1 > content1[a][b]){
						minn1 = content1[a][b];
					}
					if(maxx1 < content1[a][b]){
						maxx1 = content1[a][b];
					}	
				}
			}
			min1[b] = minn1;
			max1[b] = maxx1;
		    }
		   	for(a = 0; a < (*matrixes)[i].length; a++){
				free((*matrixes)[i].arr[a]);
			}
			free((*matrixes)[i].arr);
				
			r = atoi(splitStrings[2]);
		 	c = atoi(splitStrings[3]);
			if(r >= 0 && c >= 0){
		 		(*matrixes)[i].width = (*matrixes)[i].width + c;
		 		(*matrixes)[i].length = (*matrixes)[i].length + r;
				(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
				for(j = 0; j < (*matrixes)[i].length; j++){
         			(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
				}
				if(!strncmp(splitStrings[4], "minimum", strlen("minimum"))){
					for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    				for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   						if(a < length && b < width){
								(*matrixes)[i].arr[a][b] = content1[a][b];
							}
							else if(a < length && b >= width){
								(*matrixes)[i].arr[a][b] = min[a];
							}
							else if(a >= length && b < width){
								(*matrixes)[i].arr[a][b] = min1[b];
							}
							else{
								int min2 = 0;
								for(j = 0; j < length ; j ++){
									if(j == 0){
										min2 = min[0];
									}
									if(min2 > min[j]){
										min2 = min[j];
									}
								}
								(*matrixes)[i].arr[a][b] = min2;
							}
   	    				}
   	   				}
   	   				printf("matrix paded %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width );
   	   				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   					for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    					printf("%d ", (*matrixes)[i].arr[a][b]);
						}
						printf("\n");
					}
				}
				else if(!strncmp(splitStrings[4], "maximum", strlen("maximum"))){
					for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    				for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    					if(a < length && b < width){
								(*matrixes)[i].arr[a][b] = content1[a][b];
							}
							else if(a < length && b >= width){
								(*matrixes)[i].arr[a][b] = max[a];
							}
							else if(a >= length && b < width){
								(*matrixes)[i].arr[a][b] = max1[b];
							}
							else{
								int max2 = 0;
								for(j = 0; j < length ; j ++){
									if(j == 0){
										max2 = max[0];
									}
									if(max2 < max[j]){
										max2 = max[j];
									}
								}
								(*matrixes)[i].arr[a][b] = max2;
							}
   	    				}
   	    			}					
   	    			printf("matrix paded %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width );
					for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    				for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   						printf("%d ", (*matrixes)[i].arr[a][b]);
						}
					printf("\n");
					}
				}
				else{
					printf("error\n");
				}			  	
			}
			free(min);
	        free(max);
			free(min1);
			free(max1);
			for(a = 0; a < length1; a++){
				free(content1[a]);
			}
			free(content1);
		}
		}
	free(name1);
}

void padval(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length1){
	int a = strlen(splitStrings[1]), i, j= 0, k= 0, b, r, c ;
	int **content1;
	char *name1;
	name1 = (char *) malloc(a*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
    name1[a] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1, (*matrixes)[i].name, strlen(name1)) && !strncmp((*matrixes)[i].type, ".mat", strlen("mat"))){
				length =  (*matrixes)[i].length;
				width =  (*matrixes)[i].width;
				content1 = (int**)malloc((*matrixes)[i].length*(sizeof(int*)));
				for(j = 0; j <  (*matrixes)[i].length; j++){
         			content1[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
				}
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    			for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    	 			content1[a][b] =  (*matrixes)[i].arr[a][b];
					}
		    	}
		    	
		    	for(a = 0; a < (*matrixes)[i].length; a++){
		 			free((*matrixes)[i].arr[a]);
				}
				free((*matrixes)[i].arr);
				
				r = atoi(splitStrings[2]);
			 	c = atoi(splitStrings[3]);
			 	(*matrixes)[i].width = (*matrixes)[i].width + c;
			 	(*matrixes)[i].length = (*matrixes)[i].length + r;
				(*matrixes)[i].arr = (int**)malloc((*matrixes)[i].length* (sizeof(int*)));
				for(j = 0; j < (*matrixes)[i].length; j++){
       				(*matrixes)[i].arr[j]= (int*)malloc((*matrixes)[i].width * sizeof(int));
				}
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    			for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    					if(a < length && b < width){
								(*matrixes)[i].arr[a][b] = content1[a][b];
							}
							else{
								(*matrixes)[i].arr[a][b] = atoi(splitStrings[4]);
							}
   	    				}
   	        	}
   	    		printf("matrix paded %s %d %d\n", (*matrixes)[i].name, (*matrixes)[i].length, (*matrixes)[i].width );
   	    		for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    				for(b = 0; b <  (*matrixes)[i].width; b ++){
   	    					printf("%d ", (*matrixes)[i].arr[a][b]);
						}
						printf("\n");
				}
				for(a = 0; a < length1; a++){
		    		free(content1[a]);
				}
				free(content1);
			}
		}
	free(name1);
}

void add(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0 ;
	int **content2;
	char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name2, (*matrixes)[i].name, strlen(name2))){
			length =  (*matrixes)[i].length;
			width =  (*matrixes)[i].width;
			content2 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
			for(j = 0; j <  (*matrixes)[i].length; j++){
       			content2[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
			}
			for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   			for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   	 	 		content2[a][b] =  (*matrixes)[i].arr[a][b];
				}
			}
		}
	}
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
			if(width != (*matrixes)[i].width || length != (*matrixes)[i].length){
				printf("error\n");
			}
			else if(width == (*matrixes)[i].width && length == (*matrixes)[i].length){
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    			for(b = 0; b <  (*matrixes)[i].width; b ++){
						(*matrixes)[i].arr[a][b] = (*matrixes)[i].arr[a][b] + content2[a][b];
					}
	 			}
	 			printf("add %s %s\n", splitStrings[1], splitStrings[2]);
		 		for(a = 0; a < (*matrixes)[i].length; a ++){
   	    			for(b = 0; b < (*matrixes)[i].width; b ++){
   	    				printf("%d ",(*matrixes)[i].arr[a][b]);
					}
					printf("\n");
		 		}
			}
		}
	}
	for(a = 0; a < length2; a++){
	   free(content2[a]);
	}
	free(content2);
	free(name1);
	free(name2);
}

void multiply(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0 ;
	int **content2;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name2, (*matrixes)[i].name, strlen(name2))){
			length =  (*matrixes)[i].length;
			width =  (*matrixes)[i].width;
			content2 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
			for(j = 0; j <  (*matrixes)[i].length; j++){
         		content2[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
			}
			for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   			for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   	 	 		content2[a][b] =  (*matrixes)[i].arr[a][b];
				}
		 	}
		}
	}
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
		 	if(width != (*matrixes)[i].width || length != (*matrixes)[i].length){
				printf("error\n");
			}
			else if(width == (*matrixes)[i].width && length == (*matrixes)[i].length){
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    			for(b = 0; b <  (*matrixes)[i].width; b ++){
						(*matrixes)[i].arr[a][b] = (*matrixes)[i].arr[a][b] * content2[a][b];
					}
				}
			printf("multiply %s %s\n", splitStrings[1], splitStrings[2]);
			for(a = 0; a < (*matrixes)[i].length; a ++){
   	    	 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	    			printf("%d ",(*matrixes)[i].arr[a][b]);
				}
				printf("\n");
			}
			}
		}
	}
	for(a = 0; a < length2; a++){
		free(content2[a]);
	}
	free(content2);
	free(name1);
	free(name2);
}

void subtract(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length2){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[2]), i, j= 0, k= 0 ;
	int **content2;
	char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[2]);
    name1[a] = '\0';
    name2[b] = '\0';
    int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name2, (*matrixes)[i].name, strlen(name2))){
			length =  (*matrixes)[i].length;
			width =  (*matrixes)[i].width;
			content2 = (int**)malloc( (*matrixes)[i].length*(sizeof(int*)));
			for(j = 0; j <  (*matrixes)[i].length; j++){
			    content2[j]= (int*)malloc( (*matrixes)[i].width * sizeof(int));
			}
			for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   			for(b = 0; b <  (*matrixes)[i].width; b ++){
   	   	 	 		content2[a][b] =  (*matrixes)[i].arr[a][b];
				}
			}
		}
	}
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name1,  (*matrixes)[i].name, strlen(name1))){
			if(width != (*matrixes)[i].width || length != (*matrixes)[i].length){
				printf("error\n");
			}
			else if(width == (*matrixes)[i].width && length == (*matrixes)[i].length){
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	    			for(b = 0; b <  (*matrixes)[i].width; b ++){
						(*matrixes)[i].arr[a][b] = (*matrixes)[i].arr[a][b] - content2[a][b];
					}
		 		}
			printf("subtract %s %s\n", splitStrings[1], splitStrings[2]);
	 		for(a = 0; a < (*matrixes)[i].length; a ++){
   	   		 	for(b = 0; b < (*matrixes)[i].width; b ++){
   	  				printf("%d ",(*matrixes)[i].arr[a][b]);
				}
				printf("\n");
			}
			}
		}
	}
	for(a = 0; a < length2; a++){
		free(content2[a]);
	}
	free(content2);
	free(name1);
	free(name2);
}

void vecslice(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *vec_num){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[4]), i, j= 0, k= 0 , r, c = 0 ;
	int *content1;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	
	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[4]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1, (*matrixes)[i].name, strlen(name1))){
				length =  (*matrixes)[i].length;
				width =  (*matrixes)[i].width;
				int start = atoi(splitStrings[2]);
				int stop = atoi(splitStrings[3]);
				int boy;
				boy = stop - start;
				content1 = (int*)malloc( boy*(sizeof(int*)));
				for(a = 0; a <  (*matrixes)[i].length; a ++){
   	   				for(b = start; b < stop ; b ++){
   	   	 	 			content1[c] =  (*matrixes)[i].arr[a][b];
   	   	 	 			c ++;
					}
				}
				if(k ==1){
					
				}
				else if ( k != 1){
					int no = *countMatrixes;
					(*matrixes)[no].name = (char *) malloc(strlen(name2)*(sizeof(char)));
					strcpy((*matrixes)[no].name, name2);
					(*matrixes)[no].length = 1;
					(*matrixes)[no].width = boy;
					(*matrixes)[no].type = (char *) malloc(strlen(".vec")*(sizeof(char)));
    				strncpy((*matrixes)[no].type , ".vec", strlen(".vec"));
    				(*matrixes)[no].arr= (int**)malloc((*matrixes)[no].length* (sizeof(int*)));
     				for(j = 0; j < (*matrixes)[no].length; j++){
     					(*matrixes)[no].arr[j]= (int*)malloc((*matrixes)[no].width * sizeof(int));
					}
					c = 0;
					for(a = 0; a < (*matrixes)[no].length; a ++){
   	 					for(b = 0; b < (*matrixes)[no].width; b ++){
   	 						(*matrixes)[no].arr[a][b] = content1[c];
   	 						c ++;
			    		}
			    	}
			    	printf("vector sliced %s %d\n", (*matrixes)[no].name, (*matrixes)[no].width);
			    	for(a = 0; a < (*matrixes)[no].length; a ++){
   	 					for(b = 0; b < (*matrixes)[no].width; b ++){
							printf("%d ",(*matrixes)[no].arr[a][b]);
			    		}
			    		printf("\n");
			    	}
			    	*countMatrixes = *countMatrixes +1;
          		   	*vec_num = *vec_num +1;
        			break;
				}
			}
		}
	free(content1);
	free(name1);
	free(name2);
}

void matslicecol(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *vec_num, int *mat_num){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[5]), i, j= 0, k= 0 , r, c = 0 ;
	int *content1;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[5]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name1, (*matrixes)[i].name, strlen(name1))){
				length =  (*matrixes)[i].length;
				width =  (*matrixes)[i].width;
				int index = atoi(splitStrings[2]);
				int start = atoi(splitStrings[3]);
				int stop = atoi(splitStrings[4]);
				int boy;
				if(index >= 0 && index < (*matrixes)[i].width){
					if(start >= 0 && stop <= (*matrixes)[i].length){
						boy = stop - start;
						content1 = (int*)malloc( boy*(sizeof(int*)));
						c = 0;
   	    				for(b = start; b < stop ; b ++){
   	    	 	 			content1[c] =  (*matrixes)[i].arr[b][index];
   	    	 	 			c ++;
		 				}
					}
					if(k ==1){
				
					}
					else if ( k != 1){
						int no = *countMatrixes;
						(*matrixes)[no].name = (char *) malloc(strlen(name2)*(sizeof(char)));
						strcpy((*matrixes)[no].name, name2);
						(*matrixes)[no].length = 1;
						(*matrixes)[no].width = boy;
						(*matrixes)[no].type = (char *) malloc(strlen(".vec")*(sizeof(char)));
    					strncpy((*matrixes)[no].type , ".vec", strlen(".vec"));
	    				(*matrixes)[no].arr= (int**)malloc((*matrixes)[no].length* (sizeof(int*)));
 	    				for(j = 0; j < (*matrixes)[no].length; j++){
    	 					(*matrixes)[no].arr[j]= (int*)malloc((*matrixes)[no].width * sizeof(int));
						}
						c = 0;
						for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
   	 							(*matrixes)[no].arr[a][b] = content1[c];
   	 							c ++;
			    			}
			    		}
				    	printf("vector sliced %s %d\n", (*matrixes)[no].name, (*matrixes)[no].width);
				    	for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
								printf("%d ",(*matrixes)[no].arr[a][b]);
			    			}
			    			printf("\n");
			    		}	
			    		*countMatrixes = *countMatrixes +1;
  	        		   	*vec_num = *vec_num +1;
    	    			break;
						}
					}
			}
		}
	free(content1);
	free(name1);
	free(name2);
}

void matslicerow(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *vec_num, int *mat_num){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[5]), i, j= 0, k= 0 , r, c = 0 ;
	int *content1;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[5]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
			if(!strncmp(name1, (*matrixes)[i].name, strlen(name1))){
				length =  (*matrixes)[i].length;
				width =  (*matrixes)[i].width;
				int index = atoi(splitStrings[2]);
				int start = atoi(splitStrings[3]);
				int stop = atoi(splitStrings[4]);
				int boy;
				if(index >= 0 && index < (*matrixes)[i].length){
					if(start >= 0 && stop <= (*matrixes)[i].width){
						boy = stop - start;
						content1 = (int*)malloc( boy*(sizeof(int*)));
						c = 0;
   	    				for(b = start; b < stop ; b ++){
   	    	 	 			content1[c] =  (*matrixes)[i].arr[index][b];
   	    	 	 			c ++;
		 				}
					}
					if(k ==1){
				
					}
					else if ( k != 1){
						int no = *countMatrixes;
						(*matrixes)[no].name = (char *) malloc(strlen(name2)*(sizeof(char)));
						strcpy((*matrixes)[no].name, name2);
						(*matrixes)[no].length = 1;
						(*matrixes)[no].width = boy;
						(*matrixes)[no].type = (char *) malloc(strlen(".vec")*(sizeof(char)));
    					strncpy((*matrixes)[no].type , ".vec", strlen(".vec"));
	    				(*matrixes)[no].arr= (int**)malloc((*matrixes)[no].length* (sizeof(int*)));
 	    				for(j = 0; j < (*matrixes)[no].length; j++){
    	 					(*matrixes)[no].arr[j]= (int*)malloc((*matrixes)[no].width * sizeof(int));
						}
						c = 0;
						for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
   	 							(*matrixes)[no].arr[a][b] = content1[c];
   	 							c ++;
			    			}
			    		}
				    	printf("vector sliced %s %d\n", (*matrixes)[no].name, (*matrixes)[no].width);
				    	for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
								printf("%d ",(*matrixes)[no].arr[a][b]);
			    			}
			    			printf("\n");
			    		}	
			    		*countMatrixes = *countMatrixes +1;
  	        		   	*vec_num = *vec_num +1;
    	    			break;
					}
				free(content1);
				}
			}
		}
	free(name1);
	free(name2);
}

void matslice(Arrays** matrixes, char **splitStrings, int *countMatrixes, int *mat_num, int length1){
	int a = strlen(splitStrings[1]), b = strlen(splitStrings[6]), i, j= 0, k= 0 , r, c = 0 ;
	int **content1;
    char *name1, *name2;
	name1 = (char *) malloc(a*(sizeof(char)));
	name2 = (char *) malloc(b*(sizeof(char)));	strcpy(name1, splitStrings[1]);
	strcpy(name2, splitStrings[6]);
    name1[a] = '\0';
    name2[b] = '\0';
	int length, width;
	for( i = 0; i < *countMatrixes; i++){
		if(!strncmp(name1, (*matrixes)[i].name, strlen(name1))){
				length =  (*matrixes)[i].length;
				width =  (*matrixes)[i].width;
				int y1 = atoi(splitStrings[2]);
				int y2 = atoi(splitStrings[3]);
				int x1 = atoi(splitStrings[4]);
				int x2 = atoi(splitStrings[5]);
				int row, column;
				if(y2> y1 && x2> x1 && x1 >=0 && x2 >= 0 && y1 >= 0 && y2 >= 0){
					if(y2 <= (*matrixes)[i].width && x2 <= (*matrixes)[i].length){
						row = x2 - x1;
						column = y2 - y1;
						content1 = (int**)malloc( row*(sizeof(int*)));
						for(j = 0; j < row ; j++){
         					content1[j]= (int*)malloc( column * sizeof(int));
						}						
						c = 0, r =0 ;
						for( a = x1 ; a < x2; a ++){
							for(b = y1; b < y2 ; b ++){
   	    	 	 				content1[r][c] =  (*matrixes)[i].arr[a][b];
   	    	 	 				c ++;
		 					}
		 				    r ++;
		 				    c = 0;
						}
					}
					
					if(k ==1){
			
					}
					else if ( k != 1){
						int no = *countMatrixes;
						(*matrixes)[no].name = (char *) malloc(strlen(name2)*(sizeof(char)));
						strcpy((*matrixes)[no].name, name2);
						(*matrixes)[no].length = row;
						(*matrixes)[no].width = column;
						(*matrixes)[no].type = (char *) malloc(strlen(".mat")*(sizeof(char)));
    					strncpy((*matrixes)[no].type , ".mat", strlen(".mat"));
	    				(*matrixes)[no].arr= (int**)malloc((*matrixes)[no].length* (sizeof(int*)));
 	    				for(j = 0; j < (*matrixes)[no].length; j++){
    	 					(*matrixes)[no].arr[j]= (int*)malloc((*matrixes)[no].width * sizeof(int));
						}
						for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
   	 							(*matrixes)[no].arr[a][b] = content1[a][b];
			    			}
			    		}
				    	printf("matrix sliced %s %d %d\n", (*matrixes)[no].name,  (*matrixes)[i].length, (*matrixes)[no].width);
				    	for(a = 0; a < (*matrixes)[no].length; a ++){
   	 						for(b = 0; b < (*matrixes)[no].width; b ++){
								printf("%d ",(*matrixes)[no].arr[a][b]);
			    			}
			    			printf("\n");
			    		}	
			    		*countMatrixes = *countMatrixes +1;
  	        		   	*mat_num = *mat_num +1;
    	    			break;
						}
					for(a = 0; a < length1; a++){
						free(content1[a]);
					}
					free(content1);
					}
					else{
						printf("/////error\n");
					}
			}
		}
	free(name1);
	free(name2);
}

void free1(int *countMatrixes, Arrays **matrixes){
	int i, j;
	for( i = 0; i < *countMatrixes; i++){
		for(j = 0; j < (*matrixes)[i].length; j++){
		    free((*matrixes)[i].arr[j]);
		}
		free((*matrixes)[i].arr);
		free((*matrixes)[i].type);
		free((*matrixes)[i].name);
	}
}

int control(char number[]){
	int k = 5;
	if(isalpha(number[0])){
		k = 0;
	}
	else if(isdigit(number[0]) && atoi(number) >= 0){
			k = 1;
	}
	return k;
}

int matrixes_control(Arrays** matrixes, int *countMatrixes, char name[]){
	int a = strlen(name), i, control = 0;
	char *filename;
	filename = (char *) malloc((a+1)*(sizeof(char)));
	strncpy(filename, name, a);
    filename[a] = '\0';
	for(i = 0; i < *countMatrixes; i++){
		if(!(strncmp(filename, (*matrixes)[i].name, strlen(filename)))){
			control = 1;
			break;
		}
	}
	free(filename);
	return control;
}

int type_control(Arrays** matrixes, int *countMatrixes, char name[]){
	int a = strlen(name), i, control = 5;
	char *filename;
	filename = (char *) malloc((a+1)*(sizeof(char)));
	strncpy(filename, name, a);
    filename[a] = '\0';
	for(i = 0; i < *countMatrixes; i++){
		if(!(strncmp(filename, (*matrixes)[i].name, strlen(filename)))){
			if(!(strncmp((*matrixes)[i].type, ".mat", strlen(".mat")))){
				control = 1;
			    break;
			}
			else if(!(strncmp((*matrixes)[i].type,".vec", strlen(".vec")))){
				control = 0;
				break;
			}
		}
	}
	free(filename);
	return control;
}

int length_num(Arrays** matrixes, int *countMatrixes, char name[]){
	int a = strlen(name), i,length = 0;
	char *filename;
	filename = (char *) malloc((a+1)*(sizeof(char)));
	strncpy(filename, name, a);
    filename[a] = '\0';
	for(i = 0; i < *countMatrixes; i++){
		if(!(strncmp(filename, (*matrixes)[i].name, strlen(filename)))){
			length = (*matrixes)[i].length;
		}
	}
	free(filename);
	return length;
}

int width_num(Arrays** matrixes, int *countMatrixes, char name[]){
	int a = strlen(name), i, width = 0;
	char *filename;
	filename = (char *) malloc((a+1)*(sizeof(char)));
	strncpy(filename, name, a);
    filename[a] = '\0';
	for(i = 0; i < *countMatrixes; i++){
		if(!(strncmp(filename, (*matrixes)[i].name, strlen(filename)))){
			width = (*matrixes)[i].width;
		}
	}
	free(filename);
	return width;
}

void commands_File(int charNum, Arrays matrixes[], int *countMatrixes, int *mat_num, int *vec_num, char *commandsFile, char *outputFile){
	char *str;
	str = (char*)malloc(charNum* (sizeof(char)));
	char **splitStrings;
	splitStrings = (char**)malloc(charNum* (sizeof(char*)));
	int j, len, wordNum, i , cnt, p, a, length1, length2, width1, width2;
	for(j = 0; j < charNum; j++){
         	splitStrings[j]= (char*)malloc(charNum * sizeof(char));
	}

    FILE *file, *f ;
    file = fopen(commandsFile,"r");
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
				if(str[p + 1] != ' ' && str[p + 1] != '\0'){ //burasý patlayabilir.
					wordNum++;
				}	
			}
		}
		// i kaç kelime olduðunu tutuyor.
		if( splitStrings[0][0] != '\0' && splitStrings[0][0] != ' '){
			char word1[strlen(splitStrings[0]) +1];
			strncpy( word1, splitStrings[0], strlen(splitStrings[0]));
			word1[strlen(splitStrings[0])] = '\0';
			if( ! strncmp(word1, "veczeros", strlen("veczeros"))){
				if( wordNum == 2 && control(splitStrings[2]) == 1 && atoi(splitStrings[2]) >0 ){
					if(matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 0){
						veczeros(matrixes, countMatrixes, splitStrings, vec_num);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}
			}
			else if(!strncmp(word1, "matzeros", strlen("matzeros"))){
				if( wordNum == 3 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && atoi(splitStrings[2]) >0 && atoi(splitStrings[3]) >0){
					if(matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 0){
						matzeros(matrixes, countMatrixes, splitStrings, mat_num);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if( ! strncmp(word1, "vecread", strlen("vecread"))){
				int a = strlen(splitStrings[1]) -3, i;
				char *name1;
				name1 = (char *) malloc(a*(sizeof(char)));
				for(i = 0; i < a ; i++){
					name1[i] = splitStrings[1][i];
				}
    			name1[a-1] = '\0';
				if(wordNum == 1 && matrixes_control(&matrixes, countMatrixes, name1) == 1 && type_control(&matrixes, countMatrixes, name1) == 0){
						vecread(matrixes, splitStrings, vec_num, countMatrixes);
				}
				else{
					printf("error\n");
				}
				free(name1);
			}
			else if( ! strncmp(word1, "matread", strlen("matread"))){
				int a = strlen(splitStrings[1]) -3, i;
				char *name1;
				name1 = (char *) malloc(a*(sizeof(char)));
				for(i = 0; i < a ; i++){
					name1[i] = splitStrings[1][i];
				}
    			name1[a-1] = '\0';
				if(wordNum == 1 && matrixes_control(&matrixes, countMatrixes, name1) == 1 && type_control(&matrixes, countMatrixes, name1) == 1){
					matread(matrixes, splitStrings, mat_num, countMatrixes);
				}
				else{
					printf("error\n");
				}
				free(name1);
			}
			else if( ! strncmp(word1, "vecstack", strlen("vecstack"))){
				if(wordNum == 4 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					width1 = width_num(&matrixes, countMatrixes, splitStrings[1]);
					width2 = width_num(&matrixes, countMatrixes, splitStrings[2]);
					if(matrixes_control(&matrixes, countMatrixes, splitStrings[4]) == 0 && type_control(&matrixes, countMatrixes, splitStrings[1]) == 0 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 0 && width1 >0 && width2 > 0  && width1 == width2){
						vecstack(matrixes, splitStrings, vec_num, countMatrixes, mat_num, width1, width2 );
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}
			}
			else if( ! strncmp(word1, "matstack", strlen("matstack"))){
				if( wordNum == 3 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						length2 = length_num(&matrixes, countMatrixes, splitStrings[2]);
						matstack(&matrixes, splitStrings, countMatrixes, mat_num, length1, length2);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}
			}
			else if( ! strncmp(word1, "mvstack", strlen("mvstack"))){
				if( wordNum == 3 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 0){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						length2 = length_num(&matrixes, countMatrixes, splitStrings[2]);
						mvstack(&matrixes, splitStrings, countMatrixes, mat_num, vec_num, length1, length2);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if(!strncmp(word1, "pad", strlen(word1))){
				if( wordNum == 4 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						pad(&matrixes, splitStrings, countMatrixes, mat_num, length1);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}	
			}
			else if( ! strncmp(word1, "padval", strlen(word1))){
				if( wordNum == 4 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && atoi(splitStrings[3]) > 0 && splitStrings[2] > 0){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						padval(&matrixes, splitStrings, countMatrixes, mat_num, length1);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if( ! strncmp(word1, "add", strlen(word1))){
				if( wordNum == 2 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 1 ){
						length2 = length_num(&matrixes, countMatrixes, splitStrings[2]);
						add(&matrixes, splitStrings, countMatrixes, mat_num, length2);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}
			}
			else if( ! strncmp(word1, "multiply", strlen("multiply"))){
				if( wordNum == 2 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 1 ){
						length2 = length_num(&matrixes, countMatrixes, splitStrings[2]);
						multiply(&matrixes, splitStrings, countMatrixes, mat_num, length2);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if( ! strncmp(word1, "subtract", strlen("subtract"))){
				if( wordNum == 2 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[2]) == 1){
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[2]) == 1 ){
						length2 = length_num(&matrixes, countMatrixes, splitStrings[2]);
						subtract(&matrixes, splitStrings, countMatrixes, mat_num, length2);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if( ! strncmp(word1, "vecslice", strlen("vecslice"))){
				if( wordNum == 4 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[1]) == 0 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[4]) == 0){
					width1 = width_num(&matrixes, countMatrixes, splitStrings[1]);
					if((atoi(splitStrings[3]) - atoi(splitStrings[2])) > 0 && atoi(splitStrings[3]) <= width1 && atoi(splitStrings[2]) < width1 ){
						vecslice(&matrixes, splitStrings, countMatrixes, vec_num);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}		
			}
			else if( ! strncmp(word1, "matslicecol", strlen("matslicecol"))){
				if( wordNum == 5 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && control(splitStrings[4]) == 1 && type_control(&matrixes, countMatrixes, splitStrings[1]) == 1){
					if(matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[5]) == 0  ){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						width1 = width_num(&matrixes, countMatrixes, splitStrings[1]);
						if((atoi(splitStrings[4]) - atoi(splitStrings[3])) > 0 && atoi(splitStrings[4]) <= width1 && atoi(splitStrings[3]) < width1 && atoi(splitStrings[2]) >= 0 && atoi(splitStrings[2]) < width1){
							matslicecol(&matrixes, splitStrings, countMatrixes, vec_num, mat_num);
						}
						else{
						printf("error\n");
						}
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}				
			}
			else if( ! strncmp(word1, "matslicerow", strlen("matslicerow"))){
				if( wordNum == 5 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && control(splitStrings[4]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[5]) == 0){
					length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
					width1 = width_num(&matrixes, countMatrixes, splitStrings[1]);
					if(type_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && (atoi(splitStrings[4]) - atoi(splitStrings[3])) > 0 && atoi(splitStrings[4]) <= width1 && atoi(splitStrings[3]) < width1 && atoi(splitStrings[2]) >= 0 && atoi(splitStrings[2]) <= length1){
						matslicerow(&matrixes, splitStrings, countMatrixes, vec_num, mat_num);
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}			
			}
			else if( ! strncmp(word1, "matslice", strlen("matslice"))){
				if( wordNum == 6 && control(splitStrings[2]) == 1 && control(splitStrings[3]) == 1 && control(splitStrings[4]) == 1 && control(splitStrings[5]) == 1){
					if(matrixes_control(&matrixes, countMatrixes, splitStrings[1]) == 1 && matrixes_control(&matrixes, countMatrixes, splitStrings[6]) == 0 && type_control(&matrixes, countMatrixes, splitStrings[1]) == 1){
						length1 = length_num(&matrixes, countMatrixes, splitStrings[1]);
						width1 = width_num(&matrixes, countMatrixes, splitStrings[1]);
						int row = atoi(splitStrings[5]) - atoi(splitStrings[4]);
						if(atoi(splitStrings[2]) <= width1 && atoi(splitStrings[3]) <= width1 && atoi(splitStrings[5]) <= length1 && atoi(splitStrings[4]) <= length1 && (atoi(splitStrings[3]) - atoi(splitStrings[2])) > 0 && (atoi(splitStrings[5]) - atoi(splitStrings[4])) > 0 ){
							matslice(&matrixes, splitStrings, countMatrixes, mat_num, row);
						}
						else{
						printf("error\n");
						}
					}
					else{
						printf("error\n");
					}
				}
				else{
					printf("error\n");
				}				
			}
			else{
				printf("error\n");
			}
		}
	}
	fclose(file);
	for(a = 0; a < charNum; a++){
		free(splitStrings[a]);
	}
	free(splitStrings);
	free(str);
}

int main(int argc, char *argv[]){
	char *arrays = argv[1];
	char *commands = argv[2];
	char *output = argv[3];
    int countArray =noItem(arrays); // calculates the number of vec and mat file.
    int charNumber = charNum(commands);
    int countCommands= noCommands(charNumber, commands);
	int i, j, k = 2, m, a; 
	int **arr;// holds row and column numbers
	arr = (int**)malloc(countArray*(sizeof(int*)));
	for(j = 0; j < countArray ; j++){
        arr[j]= (int*)malloc(k * sizeof(int));
	}
    int countArray1 = countArray + countCommands;
    Arrays *matrixes = (Arrays *) malloc(countArray1 *sizeof(Arrays));
    createFileArray(countArray ,k , arr, arrays); //calculating row column numbers.
    rowFunc(countArray ,k, arr, matrixes);  //contains dimensions of matrices and vectors.
	openFile(countArray ,k, arr , matrixes, arrays);
	for(a = 0; a < countArray; a++){
		free(arr[a]);
	}
	free(arr);
	int vec_num = vecNum(matrixes, countArray);
	int mat_num = matNum(matrixes, countArray);
	commands_File(charNumber, matrixes, &countArray, &mat_num, &vec_num, commands, output);
	free1(&countArray, &matrixes);
    free(matrixes);
}
